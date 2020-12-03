package com.contactar.contactarlaboratory.scanners.gps;

import android.content.Context;

import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.entities.GpsRecord;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.repositories.GpsRepository;
import com.contactar.contactarlaboratory.scanners.Scheduler;

import java.util.ArrayList;
import java.util.List;

public class GpsScanScheduler extends Scheduler {

    private final GpsRepository gpsRepository;
    GpsDataBucket gpsDataBucket;

    public GpsScanScheduler(long runId, long randomTime, WindowConfiguration windowConfiguration, Context context,
                            AppDatabase database) {
        super(runId, randomTime, windowConfiguration, context, database);
        this.gpsRepository = new GpsRepository(database);
        this.key = SourceTypeEnum.GPS.toString();
        gpsDataBucket = new GpsDataBucket(context);
    }

    @Override
    protected void startTask() {
        long sampleId = windowRepository.insert(runId, this.windowCount, key);
        gpsDataBucket.setSampleId(sampleId);
    }

    @Override
    protected void endTask() {
        gpsDataBucket.stop();
        List<GpsRecord> gpsRecords = new ArrayList<>();
        List<Object> records = gpsDataBucket.getRecordsList();
        for(Object record : records) {
            gpsRecords.add((GpsRecord) record);
        }
        gpsRepository.insert(gpsRecords);
    }

}