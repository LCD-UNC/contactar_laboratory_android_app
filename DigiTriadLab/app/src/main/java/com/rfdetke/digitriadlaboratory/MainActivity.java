package com.rfdetke.digitriadlaboratory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rfdetke.digitriadlaboratory.database.DatabasePopulator;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.database.entities.ScanConfiguration;
import com.rfdetke.digitriadlaboratory.scanners.sensors.SensorDataBucket;
import com.rfdetke.digitriadlaboratory.scanners.wifi.WifiScanScheduler;

import java.util.Date;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScanConfiguration scanConfiguration = new ScanConfiguration();
        scanConfiguration.activeTime = 1000;
        scanConfiguration.inactiveTime = 1000;
        scanConfiguration.windows = 2;
        DatabasePopulator.prepopulate(getApplicationContext(), true);

        long deviceId = DatabaseSingleton.getMemoryInstance(getApplicationContext()).getDeviceDao().insert(new Device("T-1", "SAMSUNG", "A50"));
        long experimentId = DatabaseSingleton.getMemoryInstance(getApplicationContext()).getExperimentDao().insert(new Experiment("EXP-001", "aaaa", deviceId));
        long runId = DatabaseSingleton.getMemoryInstance(getApplicationContext()).getRunDao().insert(new Run(new Date(), 1, experimentId));

        WifiScanScheduler scanScheduler = new WifiScanScheduler(
                DatabaseSingleton.getMemoryInstance(getApplicationContext()).getSampleDao(),
                DatabaseSingleton.getMemoryInstance(getApplicationContext()).getSourceTypeDao(),
                scanConfiguration,
                DatabaseSingleton.getMemoryInstance(getApplicationContext()).getWifiRecordDao(),
                DatabaseSingleton.getMemoryInstance(getApplicationContext()).getSensorRecordDao(),
                runId,
                getApplicationContext());

        SensorDataBucket bucket = new SensorDataBucket(getApplicationContext());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bucket.setSampleId(10);
    }
}
