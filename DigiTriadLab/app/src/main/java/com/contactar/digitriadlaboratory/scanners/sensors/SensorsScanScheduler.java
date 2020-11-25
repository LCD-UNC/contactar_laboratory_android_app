package com.contactar.digitriadlaboratory.scanners.sensors;

import android.content.Context;

import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.database.entities.SensorRecord;
import com.contactar.digitriadlaboratory.database.entities.WindowConfiguration;
import com.contactar.digitriadlaboratory.repositories.SensorRepository;
import com.contactar.digitriadlaboratory.scanners.Scheduler;

import java.util.ArrayList;
import java.util.List;

public class SensorsScanScheduler extends Scheduler {

    private final SensorRepository sensorRepository;
    SensorDataBucket sensorDataBucket;

    public SensorsScanScheduler(long runId, long randomTime, WindowConfiguration windowConfiguration, Context context,
                                AppDatabase database) {
        super(runId, randomTime, windowConfiguration, context, database);
        this.sensorRepository = new SensorRepository(database);
        this.key = SourceTypeEnum.SENSORS.toString();
        sensorDataBucket = new SensorDataBucket(context);
    }

    @Override
    protected void startTask() {
        long sampleId = windowRepository.insert(runId, this.windowCount, key);
        sensorDataBucket.setSampleId(sampleId);
        List<SensorRecord> sensorRecords = new ArrayList<>();
        for(Object record : sensorDataBucket.getRecordsList()) {
            sensorRecords.add((SensorRecord) record);
        }
        sensorRepository.insert(sensorRecords);
    }

    @Override
    protected void endTask() {

    }

    @Override
    public void stop() {
        super.stop();
        if(sensorDataBucket !=null) {
            sensorDataBucket.unregisterSensors();
            sensorDataBucket = null;
        }
    }
}
