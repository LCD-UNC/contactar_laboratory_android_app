package com.rfdetke.digitriadlaboratory.scanners.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.BatteryRecord;
import com.rfdetke.digitriadlaboratory.database.entities.WifiRecord;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.BatteryRepository;
import com.rfdetke.digitriadlaboratory.repositories.WifiRepository;
import com.rfdetke.digitriadlaboratory.scanners.Scheduler;
import com.rfdetke.digitriadlaboratory.scanners.wifi.WifiDataBucket;

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
        //batteryIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        batteryIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        batteryIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
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
