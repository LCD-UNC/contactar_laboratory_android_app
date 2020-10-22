package com.rfdetke.digitriadlaboratory.views.modelviews;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.BluetoothLeRepository;
import com.rfdetke.digitriadlaboratory.repositories.BluetoothRepository;
import com.rfdetke.digitriadlaboratory.repositories.CellRepository;
import com.rfdetke.digitriadlaboratory.repositories.ConfigurationRepository;
import com.rfdetke.digitriadlaboratory.repositories.GpsRepository;
import com.rfdetke.digitriadlaboratory.repositories.RunRepository;
import com.rfdetke.digitriadlaboratory.repositories.SensorRepository;
import com.rfdetke.digitriadlaboratory.repositories.SourceTypeRepository;
import com.rfdetke.digitriadlaboratory.repositories.WifiRepository;

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

    private LiveData<Long> wifiCount;
    private LiveData<Long> bluetoothCount;
    private LiveData<Long> bluetoothLeCount;
    private LiveData<Long> sensorCount;
    private LiveData<Long> cellCount;
    private LiveData<Long> gpsCount;
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

    public LiveData<Long> getGpsCount() {
        return gpsCount;
    }

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
