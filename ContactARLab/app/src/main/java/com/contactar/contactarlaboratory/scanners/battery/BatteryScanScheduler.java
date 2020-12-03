package com.contactar.contactarlaboratory.scanners.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.entities.BatteryRecord;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.repositories.BatteryRepository;
import com.contactar.contactarlaboratory.scanners.Scheduler;

import java.util.ArrayList;
import java.util.List;

public class BatteryScanScheduler extends Scheduler {

    private final BatteryRepository batteryRepository;
    BatteryDataBucket batteryDataBucket;

    public BatteryScanScheduler(long runId, long randomTime, WindowConfiguration windowConfiguration, Context context,
                                AppDatabase database) {

        super(runId, randomTime, windowConfiguration, context, database);
        this.batteryRepository = new BatteryRepository(database);
        this.key = SourceTypeEnum.BATTERY.toString();
    }

    @Override
    protected void startTask() {
        long sampleId = windowRepository.insert(runId, this.windowCount, key);
        IntentFilter batteryIntentFilter = new IntentFilter();
        batteryIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        batteryIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        batteryIntentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        batteryIntentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
        batteryIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        batteryDataBucket = new BatteryDataBucket(sampleId, context);
        context.registerReceiver(batteryDataBucket, batteryIntentFilter);
    };

    @Override
    protected void endTask() {
        List<BatteryRecord> batteryRecords = new ArrayList<>();
        for (Object record : batteryDataBucket.getRecordsList()) {
            batteryRecords.add((BatteryRecord) record);
        }
        batteryRepository.insert(batteryRecords);

        context.unregisterReceiver(batteryDataBucket);
        batteryDataBucket = null;
    }

    @Override
    public void stop() {
        super.stop();
        if(batteryDataBucket!=null) {
            context.unregisterReceiver(batteryDataBucket);
            batteryDataBucket = null;
        }
    }
}
