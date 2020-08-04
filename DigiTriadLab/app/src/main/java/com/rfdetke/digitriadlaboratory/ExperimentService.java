package com.rfdetke.digitriadlaboratory;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.rfdetke.digitriadlaboratory.constants.RunStateEnum;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.advertisers.BluetoothLeAdvertiseScheduler;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.database.entities.AdvertiseConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.export.BluetoothCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.BluetoothLeCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.SensorCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.WifiCsvFileWriter;
import com.rfdetke.digitriadlaboratory.repositories.ConfigurationRepository;
import com.rfdetke.digitriadlaboratory.repositories.ExperimentRepository;
import com.rfdetke.digitriadlaboratory.repositories.RunRepository;
import com.rfdetke.digitriadlaboratory.repositories.SourceTypeRepository;
import com.rfdetke.digitriadlaboratory.scanners.ScanObserver;
import com.rfdetke.digitriadlaboratory.scanners.Scheduler;
import com.rfdetke.digitriadlaboratory.scanners.bluetooth.BluetoothLeScanScheduler;
import com.rfdetke.digitriadlaboratory.scanners.bluetooth.BluetoothScanScheduler;
import com.rfdetke.digitriadlaboratory.scanners.wifi.WifiScanScheduler;
import com.rfdetke.digitriadlaboratory.views.NewRunActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExperimentService extends Service implements ScanObserver {

    private static final String CHANNEL_ID = "ExperimentServiceChannel";
    Map<String, Boolean> doneMap;
    private RunRepository runRepository;
    private Run currentRun;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        AppDatabase database = DatabaseSingleton.getInstance(getApplicationContext());

        runRepository = new RunRepository(database);
        ConfigurationRepository configurationRepository = new ConfigurationRepository(database);
        SourceTypeRepository sourceTypeRepository = new SourceTypeRepository(database);
        ExperimentRepository experimentRepository = new ExperimentRepository(database);

        long runId = intent.getLongExtra(NewRunActivity.EXTRA_RUN_ID, 0);
        currentRun = runRepository.getById(runId);
        List<WindowConfiguration> configurations = configurationRepository.getConfigurationsForExperiment(currentRun.experimentId);
        WindowConfiguration wifiConfiguration = configurationRepository.getConfigurationForExperimentByType(currentRun.experimentId, SourceTypeEnum.WIFI.name());
        WindowConfiguration bluetoothConfiguration = configurationRepository.getConfigurationForExperimentByType(currentRun.experimentId, SourceTypeEnum.BLUETOOTH.name());
        WindowConfiguration bluetoothLeConfiguration = configurationRepository.getConfigurationForExperimentByType(currentRun.experimentId, SourceTypeEnum.BLUETOOTH_LE.name());
        WindowConfiguration bluetoothLeAdvertiseWindowConfiguration = configurationRepository.getConfigurationForExperimentByType(currentRun.experimentId, SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name());

        Experiment currentExperiment = experimentRepository.getById(currentRun.experimentId);

        String notificationText = String.format(Locale.ENGLISH, "%s run: %d", currentExperiment.codename, currentRun.number);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.ic_baseline_bulb_24)
                .setContentIntent(pendingIntent)
                .build();

        doneMap = new HashMap<>();
        runRepository.updateState(currentRun.id, RunStateEnum.RUNNING.name());

        startForeground(1, notification);

        if (wifiConfiguration != null) {
            WifiScanScheduler scanScheduler = new WifiScanScheduler(currentRun.id, wifiConfiguration, this, database);
            registerScheduler(scanScheduler);
        }

        if (bluetoothConfiguration != null) {
            BluetoothScanScheduler scanScheduler = new BluetoothScanScheduler(currentRun.id, bluetoothConfiguration, this, database);
            registerScheduler(scanScheduler);
        }

        if (bluetoothLeConfiguration != null) {
            BluetoothLeScanScheduler scanScheduler = new BluetoothLeScanScheduler(currentRun.id, bluetoothLeConfiguration, this, database);
            registerScheduler(scanScheduler);
        }

        if (bluetoothLeAdvertiseWindowConfiguration != null) {
            AdvertiseConfiguration advertiseConfiguration = configurationRepository.getBluetoothLeAdvertiseConfigurationFor(currentRun.experimentId);
            BluetoothLeAdvertiseScheduler advertiseScheduler = new BluetoothLeAdvertiseScheduler(
                    currentRun.id, bluetoothLeAdvertiseWindowConfiguration,
                    advertiseConfiguration, this, database);
            registerScheduler(advertiseScheduler);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        runRepository.updateState(currentRun.id, RunStateEnum.DONE.name());
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void update(Object scannerKey) {
        doneMap.put((String)scannerKey, Boolean.TRUE);
        for(Boolean done : doneMap.values()) {
            if(!done)
                return;
        }
        for(String key : doneMap.keySet()) {
            if (key.equals(SourceTypeEnum.WIFI.name())) {
                new WifiCsvFileWriter(currentRun.id, DatabaseSingleton.getInstance(getApplicationContext()), getApplicationContext()).execute();
            } else if (key.equals(SourceTypeEnum.BLUETOOTH.name())) {
                new BluetoothCsvFileWriter(currentRun.id, DatabaseSingleton.getInstance(getApplicationContext()), getApplicationContext()).execute();
            } else if (key.equals(SourceTypeEnum.BLUETOOTH_LE.name())) {
                new BluetoothLeCsvFileWriter(currentRun.id, DatabaseSingleton.getInstance(getApplicationContext()), getApplicationContext()).execute();
            }
        }
        new SensorCsvFileWriter(currentRun.id, DatabaseSingleton.getInstance(getApplicationContext()), getApplicationContext()).execute();
        runRepository.updateState(currentRun.id, RunStateEnum.DONE.name());
        stopForeground(true);
    }

    private void registerScheduler(Scheduler scheduler) {
        scheduler.addObserver(this);
        doneMap.put(scheduler.getKey(), Boolean.FALSE);
    }

    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                getResources().getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
