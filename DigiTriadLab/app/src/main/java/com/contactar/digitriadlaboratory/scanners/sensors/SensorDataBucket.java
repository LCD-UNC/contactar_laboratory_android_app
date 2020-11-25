package com.contactar.digitriadlaboratory.scanners.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.contactar.digitriadlaboratory.scanners.DataBucket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SensorDataBucket implements SensorEventListener, DataBucket {

    public static final int[] SENSOR_TYPES = {
            Sensor.TYPE_ACCELEROMETER,
            Sensor.TYPE_AMBIENT_TEMPERATURE,
            Sensor.TYPE_GRAVITY,
            Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_LIGHT,
            Sensor.TYPE_LINEAR_ACCELERATION,
            Sensor.TYPE_MOTION_DETECT,
            Sensor.TYPE_MAGNETIC_FIELD,
            Sensor.TYPE_PRESSURE,
            Sensor.TYPE_PROXIMITY,
            Sensor.TYPE_RELATIVE_HUMIDITY,
            Sensor.TYPE_ROTATION_VECTOR
    };

    private SensorManager sensorManager;

    private Map<Integer, TemporarySensorData> temporarySensorDataList;

    public SensorDataBucket(Context context) {
        temporarySensorDataList = new HashMap<>();
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        for (int sensorType: SENSOR_TYPES) {
            Sensor sensor = Objects.requireNonNull(sensorManager).getDefaultSensor(sensorType);
            temporarySensorDataList.putIfAbsent(sensorType, new TemporarySensorData(sensorType));
            sensorManager.registerListener( this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    public void setSampleId(long sampleId) {
        if (!temporarySensorDataList.isEmpty()) {
            for (TemporarySensorData temporarySensorData : temporarySensorDataList.values()) {
                temporarySensorData.setSampleId(sampleId);
            }
        }
    }

    @Override
    public List<Object> getRecordsList() {
        List<Object> records = new ArrayList<>();
        for(TemporarySensorData sensorData : temporarySensorDataList.values()) {
            records.addAll(sensorData.getRecords());
        }
        return records;
    }

    public void unregisterSensors(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Objects.requireNonNull(temporarySensorDataList.get(event.sensor.getType())).updateRecordValues(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
