package com.rfdetke.digitriadlaboratory.utils;

import android.hardware.Sensor;

import static com.rfdetke.digitriadlaboratory.constants.SensorTypeEnum.*;

public class EnumMapper {

    public static String mapSensorTypeToString(int sensorType) {
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                return ACCELEROMETER.name();
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return AMBIENT_TEMPERATURE.name();
            case Sensor.TYPE_GRAVITY:
                return GRAVITY.name();
            case Sensor.TYPE_GYROSCOPE:
                return GYROSCOPE.name();
            case Sensor.TYPE_LIGHT:
                return LIGHT.name();
            case Sensor.TYPE_LINEAR_ACCELERATION:
                return LINEAR_ACCELERATION.name();
            case Sensor.TYPE_MAGNETIC_FIELD:
                return MAGNETIC_FIELD.name();
            case Sensor.TYPE_MOTION_DETECT:
                return MOTION_DETECT.name();
            case Sensor.TYPE_PRESSURE:
                return PRESSURE.name();
            case Sensor.TYPE_PROXIMITY:
                return PROXIMITY.name();
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return RELATIVE_HUMIDITY.name();
            case Sensor.TYPE_ROTATION_VECTOR:
                return ROTATION_VECTOR.name();
            default:
                return "NOT_FOUND";
        }

    }
}
