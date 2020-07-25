package com.rfdetke.digitriadlaboratory.scanners.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.rfdetke.digitriadlaboratory.utils.EnumMapper;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;

import java.util.ArrayList;
import java.util.List;

public class TemporarySensorData {

    private List<SensorRecord> records;
    private String sensorType;

    public TemporarySensorData(int sensorType) {
        records = new ArrayList<>();
        this.sensorType = EnumMapper.mapSensorTypeToString(sensorType);
    }

    public List<SensorRecord> getRecords() {
        return records;
    }

    public void setSampleId(long sampleId) {
        for(SensorRecord record : records) {
            record.sampleId = sampleId;
        }
    }

    public void updateRecordValues(SensorEvent event) {
        if(records.isEmpty()) {
            for(int i=0; i<event.values.length; i++) {
                records.add(new SensorRecord(sensorType, i, event.values[i]));
            }
        } else {
            for(int i=0; i<event.values.length; i++) {
                records.get(i).value = event.values[i];
            }
        }
    }
}