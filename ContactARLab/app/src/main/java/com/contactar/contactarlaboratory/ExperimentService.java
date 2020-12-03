package com.contactar.contactarlaboratory;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.contactar.contactarlaboratory.constants.RunStateEnum;
import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.advertisers.BluetoothLeAdvertiseScheduler;
import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.DatabaseSingleton;
import com.contactar.contactarlaboratory.database.entities.AdvertiseConfiguration;
import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.database.entities.Run;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.export.csv.ActivityCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.BatteryCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.BluetoothCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.BluetoothLeCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.CellCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.GpsCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.SensorCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.WifiCsvFileWriter;
import com.contactar.contactarlaboratory.repositories.ConfigurationRepository;
import com.contactar.contactarlaboratory.repositories.ExperimentRepository;
import com.contactar.contactarlaboratory.repositories.RunRepository;
import com.contactar.contactarlaboratory.scanners.TaskObserver;
import com.contactar.contactarlaboratory.scanners.Scheduler;
import com.contactar.contactarlaboratory.scanners.activity.ActivityScanScheduler;
import com.contactar.contactarlaboratory.scanners.battery.BatteryScanScheduler;
import com.contactar.contactarlaboratory.scanners.bluetooth.BluetoothLeScanScheduler;
import com.contactar.contactarlaboratory.scanners.bluetooth.BluetoothScanScheduler;
import com.contactar.contactarlaboratory.scanners.cell.CellScanScheduler;
import com.contactar.contactarlaboratory.scanners.gps.GpsScanScheduler;
import com.contactar.contactarlaboratory.scanners.gps.myLocationListener;
import com.contactar.contactarlaboratory.scanners.sensors.SensorsScanScheduler;
import com.contactar.contactarlaboratory.scanners.wifi.WifiScanScheduler;
import com.contactar.contactarlaboratory.views.NewRunActivity;

import java.util.HashMap;
import java.util.Map;

public class ExperimentService extends Service implements TaskObserver {

    private static final String CHANNEL_ID = "ExperimentServiceChannel";
    Map<String, Boolean> doneMap;
    private RunRepository runRepository;
    private Run currentRun;
    private AppDatabase database;
    private WifiScanScheduler wifiScanScheduler;
    private BluetoothScanScheduler bluetoothScanScheduler;
    private BluetoothLeScanScheduler bluetoothLeScanScheduler;
    private SensorsScanScheduler sensorsScanScheduler;
    private BluetoothLeAdvertiseScheduler bluetoothLeAdvertiseScheduler;
    private CellScanScheduler cellScanScheduler;
    private GpsScanScheduler gpsScanScheduler;
    private BatteryScanScheduler batteryScanScheduler;
    private ActivityScanScheduler activityScanScheduler;
    private Experiment currentExperiment;
    private LocationManager locationManager;
    private myLocationListener locationListener;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        String notificationText = "Running an experiment...";
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setColor(getColor(R.color.colorAccent))
                .setColorized(true)
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.ic_baseline_bulb_24)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        database = DatabaseSingleton.getInstance(getApplicationContext());

        runRepository = new RunRepository(database);
        ConfigurationRepository configurationRepository = new ConfigurationRepository(database);
        ExperimentRepository experimentRepository = new ExperimentRepository(database);

        long runId = intent.getLongExtra(NewRunActivity.EXTRA_RUN_ID, 0);
        currentRun = runRepository.getById(runId);

        if (currentRun != null) {
            currentExperiment = experimentRepository.getById(currentRun.experimentId);
            WindowConfiguration wifiConfiguration = configurationRepository.getConfigurationForExperimentByType(currentRun.experimentId, SourceTypeEnum.WIFI.name());
            WindowConfiguration bluetoothConfiguration = configurationRepository.getConfigurationForExperimentByType(currentRun.experimentId, SourceTypeEnum.BLUETOOTH.name());
            WindowConfiguration bluetoothLeConfiguration = configurationRepository.getConfigurationForExperimentByType(currentRun.experimentId, SourceTypeEnum.BLUETOOTH_LE.name());
            WindowConfiguration sensorConfiguration = configurationRepository.getConfigurationForExperimentByType(currentRun.experimentId, SourceTypeEnum.SENSORS.name());
            WindowConfiguration cellConfiguration = configurationRepository.getConfigurationForExperimentByType(currentRun.experimentId, SourceTypeEnum.CELL.name());
            WindowConfiguration gpsConfiguration = configurationRepository.getConfigurationForExperimentByType(currentRun.experimentId, SourceTypeEnum.GPS.name());
            WindowConfiguration batteryConfiguration = configurationRepository.getConfigurationForExperimentByType(currentRun.experimentId, SourceTypeEnum.BATTERY.name());
            WindowConfiguration activityConfiguration = configurationRepository.getConfigurationForExperimentByType(currentRun.experimentId, SourceTypeEnum.ACTIVITY.name());
            WindowConfiguration bluetoothLeAdvertiseWindowConfiguration = configurationRepository.getConfigurationForExperimentByType(currentRun.experimentId, SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name());

            doneMap = new HashMap<>();
            runRepository.updateState(currentRun.id, RunStateEnum.RUNNING.name());

            if (wifiConfiguration != null) {
                wifiScanScheduler = new WifiScanScheduler(currentRun.id, currentExperiment.maxRandomTime, wifiConfiguration, this, database);
                registerScheduler(wifiScanScheduler);
            }

            if (bluetoothConfiguration != null) {
                bluetoothScanScheduler = new BluetoothScanScheduler(currentRun.id, currentExperiment.maxRandomTime, bluetoothConfiguration, this, database);
                registerScheduler(bluetoothScanScheduler);
            }

            if (bluetoothLeConfiguration != null) {
                bluetoothLeScanScheduler = new BluetoothLeScanScheduler(currentRun.id, currentExperiment.maxRandomTime, bluetoothLeConfiguration, this, database);
                registerScheduler(bluetoothLeScanScheduler);
            }

            if (sensorConfiguration != null) {
                sensorsScanScheduler = new SensorsScanScheduler(currentRun.id, currentExperiment.maxRandomTime, sensorConfiguration, this, database);
                registerScheduler(sensorsScanScheduler);
            }

            if (cellConfiguration != null) {
                cellScanScheduler = new CellScanScheduler(currentRun.id, currentExperiment.maxRandomTime, cellConfiguration, this, database);
                registerScheduler(cellScanScheduler);
            }

            if (gpsConfiguration != null) {
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                locationListener = new myLocationListener();
                if(locationManager != null)
                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) locationListener);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) locationListener);
                    } catch (SecurityException e) {
                        stopForeground(true);
                        stopSelf();
                    }

                gpsScanScheduler = new GpsScanScheduler(currentRun.id, currentExperiment.maxRandomTime, gpsConfiguration, this, database);
                registerScheduler(gpsScanScheduler);
            }

            if (batteryConfiguration != null) {
                batteryScanScheduler = new BatteryScanScheduler(currentRun.id, currentExperiment.maxRandomTime, batteryConfiguration, this, database);
                registerScheduler(batteryScanScheduler);
            }

            if (activityConfiguration != null) {
                activityScanScheduler = new ActivityScanScheduler(currentRun.id, currentExperiment.maxRandomTime, activityConfiguration, this, database);
                registerScheduler(activityScanScheduler);
            }

            if (bluetoothLeAdvertiseWindowConfiguration != null) {
                AdvertiseConfiguration advertiseConfiguration = configurationRepository.getBluetoothLeAdvertiseConfigurationFor(currentRun.experimentId);
                bluetoothLeAdvertiseScheduler = new BluetoothLeAdvertiseScheduler(
                        currentRun.id, currentExperiment.maxRandomTime,
                        bluetoothLeAdvertiseWindowConfiguration,
                        advertiseConfiguration, this, database);
                registerScheduler(bluetoothLeAdvertiseScheduler);
            }
        } else {
            stopForeground(true);
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        if(wifiScanScheduler != null) {
            wifiScanScheduler.stop();
        }

        if(bluetoothScanScheduler != null) {
            bluetoothScanScheduler.stop();
        }

        if(bluetoothLeScanScheduler != null) {
            bluetoothLeScanScheduler.stop();
        }

        if(sensorsScanScheduler != null) {
            sensorsScanScheduler.stop();
        }

        if(cellScanScheduler != null) {
            cellScanScheduler.stop();
        }

        if(gpsScanScheduler != null) {
            locationManager.removeUpdates(locationListener);
            gpsScanScheduler.stop();
        }

        if(batteryScanScheduler != null) {
            batteryScanScheduler.stop();
        }

        if(activityScanScheduler != null) {
            activityScanScheduler.stop();
        }

        if(bluetoothLeAdvertiseScheduler != null) {
            bluetoothLeAdvertiseScheduler.stop();
        }
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

        long[] runs = {currentRun.id};

        if (doneMap.containsKey(SourceTypeEnum.WIFI.name()))
            new WifiCsvFileWriter(runs, database, getApplicationContext()).execute();

        if (doneMap.containsKey(SourceTypeEnum.BLUETOOTH.name()))
            new BluetoothCsvFileWriter(runs, database, getApplicationContext()).execute();

        if (doneMap.containsKey(SourceTypeEnum.BLUETOOTH_LE.name()))
            new BluetoothLeCsvFileWriter(runs, database, getApplicationContext()).execute();

        if (doneMap.containsKey(SourceTypeEnum.SENSORS.name()))
            new SensorCsvFileWriter(runs, database, getApplicationContext()).execute();

        if (doneMap.containsKey(SourceTypeEnum.CELL.name()))
            new CellCsvFileWriter(runs, database, getApplicationContext()).execute();

        if (doneMap.containsKey(SourceTypeEnum.GPS.name()))
            new GpsCsvFileWriter(runs, database, getApplicationContext()).execute();

        if (doneMap.containsKey(SourceTypeEnum.BATTERY.name()))
            new BatteryCsvFileWriter(runs, database, getApplicationContext()).execute();

        if (doneMap.containsKey(SourceTypeEnum.ACTIVITY.name()))
            new ActivityCsvFileWriter(runs, database, getApplicationContext()).execute();

        runRepository.updateState(currentRun.id, RunStateEnum.DONE.name());
        stopForeground(true);
        stopSelf();
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
