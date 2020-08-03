package com.rfdetke.digitriadlaboratory.scanners.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.repositories.WifiRepository;
import com.rfdetke.digitriadlaboratory.scanners.ScanScheduler;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao;
import com.rfdetke.digitriadlaboratory.database.daos.SourceTypeDao;
import com.rfdetke.digitriadlaboratory.database.daos.WifiRecordDao;
import com.rfdetke.digitriadlaboratory.database.entities.Sample;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.WifiRecord;
import com.rfdetke.digitriadlaboratory.scanners.sensors.SensorDataBucket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WifiScanScheduler extends ScanScheduler {

    private final WifiRepository wifiRepository;
    WifiDataBucket wifiDataBucket;
    SensorDataBucket sensorDataBucket;

    public WifiScanScheduler(long runId, WindowConfiguration windowConfiguration, Context context,
                             AppDatabase database) {

        super(runId, windowConfiguration, context, database);
        this.wifiRepository = new WifiRepository(database);
        this.key = SourceTypeEnum.WIFI.toString();
        sensorDataBucket = new SensorDataBucket(context);
    }

    @Override
    protected void registerScanDataBucket() {
        long sampleId = sampleRepository.insert(runId, key);
        IntentFilter wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wifiDataBucket = new WifiDataBucket(sampleId, context);
        sensorDataBucket.setSampleId(sampleId);
        context.registerReceiver(wifiDataBucket, wifiIntentFilter);
    };

    @Override
    protected void unregisterScanDataBucket() {
        List<WifiRecord> wifiRecords = new ArrayList<>();
        for (Object record : wifiDataBucket.getRecordsList()) {
            wifiRecords.add((WifiRecord) record);
        }
        wifiRepository.insertWifi(wifiRecords);

        List<SensorRecord> sensorRecords = new ArrayList<>();
        for (Object record : sensorDataBucket.getRecordsList()) {
            sensorRecords.add((SensorRecord) record);
        }
        wifiRepository.insertSensors(sensorRecords);

        context.unregisterReceiver(wifiDataBucket);
        wifiDataBucket = null;
        sensorDataBucket.setSampleId(0);

    };

}
