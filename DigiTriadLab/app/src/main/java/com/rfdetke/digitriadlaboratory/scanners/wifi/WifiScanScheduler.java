package com.rfdetke.digitriadlaboratory.scanners.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.repositories.WifiRepository;
import com.rfdetke.digitriadlaboratory.scanners.Scheduler;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.WifiRecord;

import java.util.ArrayList;
import java.util.List;

public class WifiScanScheduler extends Scheduler {

    private final WifiRepository wifiRepository;
    WifiDataBucket wifiDataBucket;

    public WifiScanScheduler(long runId, WindowConfiguration windowConfiguration, Context context,
                             AppDatabase database) {

        super(runId, windowConfiguration, context, database);
        this.wifiRepository = new WifiRepository(database);
        this.key = SourceTypeEnum.WIFI.toString();
    }

    @Override
    protected void startTask() {
        long sampleId = windowRepository.insert(runId, key);
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
