package com.contactar.digitriadlaboratory.scanners.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;

import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.database.entities.BatteryRecord;
import com.contactar.digitriadlaboratory.database.entities.WifiRecord;
import com.contactar.digitriadlaboratory.database.entities.WindowConfiguration;
import com.contactar.digitriadlaboratory.repositories.BatteryRepository;
import com.contactar.digitriadlaboratory.repositories.WifiRepository;
import com.contactar.digitriadlaboratory.scanners.Scheduler;
import com.contactar.digitriadlaboratory.scanners.wifi.WifiDataBucket;

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
