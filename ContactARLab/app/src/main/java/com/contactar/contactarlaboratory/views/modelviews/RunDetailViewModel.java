package com.contactar.contactarlaboratory.views.modelviews;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.DatabaseSingleton;
import com.contactar.contactarlaboratory.database.entities.Run;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.repositories.ActivityRepository;
import com.contactar.contactarlaboratory.repositories.BatteryRepository;
import com.contactar.contactarlaboratory.repositories.BluetoothLeRepository;
import com.contactar.contactarlaboratory.repositories.BluetoothRepository;
import com.contactar.contactarlaboratory.repositories.CellRepository;
import com.contactar.contactarlaboratory.repositories.ConfigurationRepository;
import com.contactar.contactarlaboratory.repositories.GpsRepository;
import com.contactar.contactarlaboratory.repositories.RunRepository;
import com.contactar.contactarlaboratory.repositories.SensorRepository;
import com.contactar.contactarlaboratory.repositories.SourceTypeRepository;
import com.contactar.contactarlaboratory.repositories.WifiRepository;

import java.util.ArrayList;
import java.util.List;

public class RunDetailViewModel  extends AndroidViewModel {

    private final WifiRepository wifiRepository;
    private final BluetoothRepository bluetoothRepository;
    private final BluetoothLeRepository bluetoothLeRepository;
    private final SensorRepository sensorRepository;
    private final RunRepository runRepository;
    private final ConfigurationRepository configurationRepository;
    private final SourceTypeRepository sourceTypeRepository;
    private final CellRepository cellRepository;
    private final GpsRepository gpsRepository;
    private final BatteryRepository batteryRepository;
    private final ActivityRepository activityRepository;

    private LiveData<Long> wifiCount;
    private LiveData<Long> bluetoothCount;
    private LiveData<Long> bluetoothLeCount;
    private LiveData<Long> sensorCount;
    private LiveData<Long> cellCount;
    private LiveData<Long> gpsCount;
    private LiveData<Long> batteryCount;
    private LiveData<Long> activityCount;
    private LiveData<Run> currentLiveRun;
    private Run currentRun;

    private List<String> modules;

    public RunDetailViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = DatabaseSingleton.getInstance(application.getApplicationContext());
        runRepository = new RunRepository(database);
        wifiRepository = new WifiRepository(database);
        bluetoothRepository = new BluetoothRepository(database);
        bluetoothLeRepository = new BluetoothLeRepository(database);
        sensorRepository = new SensorRepository(database);
        cellRepository = new CellRepository(database);
        gpsRepository = new GpsRepository(database);
        batteryRepository = new BatteryRepository(database);
        activityRepository = new ActivityRepository(database);
        configurationRepository = new ConfigurationRepository(database);
        sourceTypeRepository = new SourceTypeRepository(database);
    }

    public void setRun(long runId) {
        currentLiveRun = runRepository.getLiveById(runId);
        currentRun = runRepository.getById(runId);
        wifiCount = wifiRepository.getLiveCount(runId);
        bluetoothCount = bluetoothRepository.getLiveCount(runId);
        bluetoothLeCount = bluetoothLeRepository.getLiveCount(runId);
        sensorCount = sensorRepository.getLiveCount(runId);
        cellCount = cellRepository.getLiveCount(runId);
        batteryCount = batteryRepository.getLiveCount(runId);
        activityCount = activityRepository.getLiveCount(runId);
        gpsCount = gpsRepository.getLiveCount(runId);
        modules = new ArrayList<>();
        for (WindowConfiguration configuration :
                configurationRepository.getConfigurationsForExperiment(currentRun.experimentId)) {
            modules.add(sourceTypeRepository.getById(configuration.sourceType).type);
        }
    }

    public LiveData<Long> getWifiCount() {
        return wifiCount;
    }

    public LiveData<Long> getBluetoothCount() {
        return bluetoothCount;
    }

    public LiveData<Long> getBluetoothLeCount() {
        return bluetoothLeCount;
    }

    public LiveData<Long> getSensorCount() {
        return sensorCount;
    }

    public LiveData<Long> getCellCount() {
        return cellCount;
    }

    public LiveData<Long> getGpsCount() { return gpsCount; }

    public LiveData<Long> getBatteryCount() { return batteryCount; }

    public LiveData<Long> getActivityCount() { return activityCount; }

    public LiveData<Run> getCurrentLiveRun() {
        return currentLiveRun;
    }

    public List<String> getModules() {
        return modules;
    }

    public Run getCurrentRun() {
        return currentRun;
    }

    public void delete() {
        runRepository.delete(currentRun);
    }

    public void updateState(String state) {
        runRepository.updateState(currentRun.id, state);
    }
}
