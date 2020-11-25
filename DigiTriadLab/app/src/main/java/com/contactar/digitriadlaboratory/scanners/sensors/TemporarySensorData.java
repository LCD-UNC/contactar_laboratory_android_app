package com.contactar.digitriadlaboratory.scanners.sensors;

import android.hardware.SensorEvent;

import com.contactar.digitriadlaboratory.database.entities.SensorRecord;

import java.util.ArrayList;
import java.util.List;

public class TemporarySensorData {

    private List<SensorRecord> records;
    private String sensorType;

    public TemporarySensorData(int sensorType) {
        records = new ArrayList<>();
        this.sensorType = SensorRecord.parseSensorType(sensorType);
    }

    public List<SensorRecord> getRecords() {
        return records;
    }

    public void setSampleId(long sampleId) {
        for(SensorRecord record : records) {
            record.windowId = sampleId;
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
