package com.rfdetke.digitriadlaboratory.scanners.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.scanners.ScanScheduler;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao;
import com.rfdetke.digitriadlaboratory.database.daos.SourceTypeDao;
import com.rfdetke.digitriadlaboratory.database.daos.WifiRecordDao;
import com.rfdetke.digitriadlaboratory.database.entities.Sample;
import com.rfdetke.digitriadlaboratory.database.entities.ScanConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.WifiRecord;
import com.rfdetke.digitriadlaboratory.scanners.sensors.SensorDataBucket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WifiScanScheduler extends ScanScheduler {

    private final long runId;
    private final Context context;
    private final WifiRecordDao wifiRecordDao;
    private final SensorRecordDao sensorRecordDao;
    WifiScanDataBucket wifiDataBucket;
    SensorDataBucket sensorDataBucket;


    public WifiScanScheduler(SampleDao sampleDao, SourceTypeDao sourceTypeDao,
                             ScanConfiguration scanConfiguration, WifiRecordDao wifiRecordDao,
                             SensorRecordDao sensorRecordDao, long runId, Context context) {
        super(sampleDao, sourceTypeDao, scanConfiguration);
        this.runId = runId;
        this.context = context;
        this.wifiRecordDao = wifiRecordDao;
        this.sensorRecordDao = sensorRecordDao;
        this.key = SourceTypeEnum.WIFI.toString();
        sensorDataBucket = new SensorDataBucket(context);
    }

    @Override
    protected void registerScanDataBucket() {
        long sampleId = sampleDao.insert(new Sample(new Date(), runId, sourceTypeDao.getSourceTypeByType(key).id));
        IntentFilter wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wifiDataBucket = new WifiScanDataBucket(sampleId, context);
        sensorDataBucket.setSampleId(sampleId);
        context.registerReceiver(wifiDataBucket, wifiIntentFilter);
    };

    @Override
    protected void unregisterScanDataBucket() {
        List<WifiRecord> wifiRecords = new ArrayList<>();
        for (Object record : wifiDataBucket.getRecordsList()) {
            wifiRecords.add((WifiRecord) record);
        }
        wifiRecordDao.insert(wifiRecords);

        List<SensorRecord> sensorRecords = new ArrayList<>();
        for (Object record : sensorDataBucket.getRecordsList()) {
            sensorRecords.add((SensorRecord) record);
        }
        sensorRecordDao.insert(sensorRecords);

        wifiDataBucket = null;
        sensorDataBucket.setSampleId(0);
    };

}