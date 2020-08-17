package com.rfdetke.digitriadlaboratory.database.entities;

import android.hardware.Sensor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static com.rfdetke.digitriadlaboratory.constants.SensorTypeEnum.ACCELEROMETER;
import static com.rfdetke.digitriadlaboratory.constants.SensorTypeEnum.AMBIENT_TEMPERATURE;
import static com.rfdetke.digitriadlaboratory.constants.SensorTypeEnum.GRAVITY;
import static com.rfdetke.digitriadlaboratory.constants.SensorTypeEnum.GYROSCOPE;
import static com.rfdetke.digitriadlaboratory.constants.SensorTypeEnum.LIGHT;
import static com.rfdetke.digitriadlaboratory.constants.SensorTypeEnum.LINEAR_ACCELERATION;
import static com.rfdetke.digitriadlaboratory.constants.SensorTypeEnum.MAGNETIC_FIELD;
import static com.rfdetke.digitriadlaboratory.constants.SensorTypeEnum.MOTION_DETECT;
import static com.rfdetke.digitriadlaboratory.constants.SensorTypeEnum.PRESSURE;
import static com.rfdetke.digitriadlaboratory.constants.SensorTypeEnum.PROXIMITY;
import static com.rfdetke.digitriadlaboratory.constants.SensorTypeEnum.RELATIVE_HUMIDITY;
import static com.rfdetke.digitriadlaboratory.constants.SensorTypeEnum.ROTATION_VECTOR;

@Entity(tableName = "sensor_record",
        indices = {@Index(value = "id", unique = true),
                   @Index(value = "window_id")},
        foreignKeys = @ForeignKey(entity = Window.class,
                parentColumns = "id",
                childColumns = "window_id",
                onDelete = ForeignKey.CASCADE))
public class SensorRecord {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "sensor_type")
    public String sensorType;
    @ColumnInfo(name = "value_id")
    public int valueId;
    public double value;

    @ColumnInfo(name = "window_id")
    public long windowId;

    public SensorRecord(String sensorType, int valueId, double value) {
        this.sensorType = sensorType;
        this.valueId = valueId;
        this.value = value;
    }

    public static String parseSensorType(int sensorType) {
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                return "ACCELEROMETER";
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return "AMBIENT TEMPERATURE";
            case Sensor.TYPE_GRAVITY:
                return "GRAVITY";
            case Sensor.TYPE_GYROSCOPE:
                return "GYROSCOPE";
            case Sensor.TYPE_LIGHT:
                return "LIGHT";
            case Sensor.TYPE_LINEAR_ACCELERATION:
                return "LINEAR ACCELERATION";
            case Sensor.TYPE_MAGNETIC_FIELD:
                return "MAGNETIC FIELD";
            case Sensor.TYPE_MOTION_DETECT:
                return "MOTION DETECT";
            case Sensor.TYPE_PRESSURE:
                return "PRESSURE";
            case Sensor.TYPE_PROXIMITY:
                return "PROXIMITY";
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return "RELATIVE HUMIDITY";
            case Sensor.TYPE_ROTATION_VECTOR:
                return "ROTATION VECTOR";
            default:
                return "NOT FOUND";
        }
    }
}
