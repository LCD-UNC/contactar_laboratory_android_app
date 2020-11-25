package com.contactar.digitriadlaboratory.views.modelviews;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.database.DatabaseSingleton;
import com.contactar.digitriadlaboratory.database.entities.Run;
import com.contactar.digitriadlaboratory.database.entities.WindowConfiguration;
import com.contactar.digitriadlaboratory.repositories.ActivityRepository;
import com.contactar.digitriadlaboratory.repositories.BatteryRepository;
import com.contactar.digitriadlaboratory.repositories.BluetoothLeRepository;
import com.contactar.digitriadlaboratory.repositories.BluetoothRepository;
import com.contactar.digitriadlaboratory.repositories.CellRepository;
import com.contactar.digitriadlaboratory.repositories.ConfigurationRepository;
import com.contactar.digitriadlaboratory.repositories.GpsRepository;
import com.contactar.digitriadlaboratory.repositories.RunRepository;
import com.contactar.digitriadlaboratory.repositories.SensorRepository;
import com.contactar.digitriadlaboratory.repositories.SourceTypeRepository;
import com.contactar.digitriadlaboratory.repositories.WifiRepository;

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
