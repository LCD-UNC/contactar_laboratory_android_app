package com.rfdetke.digitriadlaboratory.scanners.sensors;

import android.content.Context;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.SensorRepository;
import com.rfdetke.digitriadlaboratory.scanners.Scheduler;

import java.util.ArrayList;
import java.util.List;

public class SensorsScanScheduler extends Scheduler {

    private final SensorRepository sensorRepository;
    SensorDataBucket sensorDataBucket;

    public SensorsScanScheduler(long runId, WindowConfiguration windowConfiguration, Context context,
                                AppDatabase database) {
        super(runId, windowConfiguration, context, database);
        this.sensorRepository = new SensorRepository(database);
        this.key = SourceTypeEnum.SENSORS.toString();
        sensorDataBucket = new SensorDataBucket(context);
    }

    @Override
    protected void startTask() {
        long sampleId = windowRepository.insert(runId, key);
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
