package com.contactar.contactarlaboratory.scanners.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.repositories.WifiRepository;
import com.contactar.contactarlaboratory.scanners.Scheduler;
import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.database.entities.WifiRecord;

import java.util.ArrayList;
import java.util.List;

public class WifiScanScheduler extends Scheduler {

    private final WifiRepository wifiRepository;
    WifiDataBucket wifiDataBucket;

    public WifiScanScheduler(long runId, long randomTime, WindowConfiguration windowConfiguration, Context context,
                             AppDatabase database) {

        super(runId, randomTime, windowConfiguration, context, database);
        this.wifiRepository = new WifiRepository(database);
        this.key = SourceTypeEnum.WIFI.toString();
    }

    @Override
    protected void startTask() {
        long sampleId = windowRepository.insert(runId, this.windowCount, key);
        IntentFilter wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wifiDataBucket = new WifiDataBucket(sampleId, context);
        context.registerReceiver(wifiDataBucket, wifiIntentFilter);
    };

    @Override
    protected void endTask() {
        List<WifiRecord> wifiRecords = new ArrayList<>();
        for (Object record : wifiDataBucket.getRecordsList()) {
            wifiRecords.add((WifiRecord) record);
        }
        wifiRepository.insertWifi(wifiRecords);

        context.unregisterReceiver(wifiDataBucket);
        wifiDataBucket = null;
    }

    @Override
    public void stop() {
        super.stop();
        if(wifiDataBucket!=null) {
            context.unregisterReceiver(wifiDataBucket);
            wifiDataBucket = null;
        }
    }
}
