package com.rfdetke.digitriadlaboratory.views.modelviews;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;

import com.rfdetke.digitriadlaboratory.database.entities.AdvertiseConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.export.representations.ExperimentRepresentation;
import com.rfdetke.digitriadlaboratory.repositories.ConfigurationRepository;
import com.rfdetke.digitriadlaboratory.repositories.ExperimentRepository;
import com.rfdetke.digitriadlaboratory.repositories.RunRepository;
import com.rfdetke.digitriadlaboratory.repositories.SourceTypeRepository;
import com.rfdetke.digitriadlaboratory.qr.QR;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExperimentDetailViewModel extends AndroidViewModel {

    private final ExperimentRepository experimentRepository;
    private final ConfigurationRepository configurationRepository;
    private final SourceTypeRepository sourceTypeRepository;
    private final RunRepository runRepository;

    private LiveData<List<Run>> runsForExperiment;
    private Experiment currentExperiment;
    private List<WindowConfiguration> configurations;
    private AdvertiseConfiguration advertiseConfiguration;
    private List<String> tagList;
    private Bitmap experimentQr;
    private AppDatabase database;

    private List<String> modules;

    public List<String> getModules() {
        return modules;
    }

    public ExperimentDetailViewModel(@NonNull Application application) {
        super(application);
        database = DatabaseSingleton.getInstance(application.getApplicationContext());
        runRepository = new RunRepository(database);
        experimentRepository = new ExperimentRepository(database);
        configurationRepository = new ConfigurationRepository(database);
        sourceTypeRepository = new SourceTypeRepository(database);
    }

    public void setExperiment(long experimentId) {
        this.currentExperiment = experimentRepository.getById(experimentId);
        this.configurations = configurationRepository.getConfigurationsForExperiment(currentExperiment.id);
        this.advertiseConfiguration = configurationRepository.getBluetoothLeAdvertiseConfigurationFor(currentExperiment.id);
        this.runsForExperiment = runRepository.getLiveRunsForExperiment(experimentId);
        this.tagList = experimentRepository.getTagList(experimentId);
        this.experimentQr = QR.getQrExperiment(currentExperiment, configurationRepository,
                advertiseConfiguration, tagList);
        ConfigurationRepository configurationRepository = new ConfigurationRepository(database);
        SourceTypeRepository sourceTypeRepository = new SourceTypeRepository(database);
        modules = new ArrayList<>();
        for (WindowConfiguration configuration :
                configurationRepository.getConfigurationsForExperiment(currentExperiment.id)) {
            modules.add(sourceTypeRepository.getById(configuration.sourceType).type);
        }
    }

    public LiveData<List<Run>> getRunsForExperiment() {
        return runsForExperiment;
    }

    public long insert(Run run) { return runRepository.insert(run); }

    public void delete() { experimentRepository.delete(currentExperiment); }

    public Experiment getCurrentExperiment() {
        return currentExperiment;
    }

    public boolean hasOngoingRuns(long id) {
        return experimentRepository.getOngoingRunsForExperiment(id).size()>0;
    }

    public String getConfigurationString() {
        String configurationString = "";
        for(WindowConfiguration configuration : configurations) {
            configurationString = configurationString.concat(String.format(Locale.ENGLISH,
                    "      * %s:\n            Active: %d s. Inactive: %d s. Windows: %d.\n",
                    sourceTypeRepository.getById(configuration.sourceType).type.replace('_',' '),
                    configuration.activeTime, configuration.inactiveTime, configuration.windows));
            if(sourceTypeRepository.getById(configuration.sourceType).type.equals(SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name())) {
                configurationString = configurationString.concat(String.format(Locale.ENGLISH,
                        "            Tx Power: %d dBm. Interval %d ms.\n", advertiseConfiguration.txPower, advertiseConfiguration.interval));
            }
        }
        return configurationString;
    }
    public Bitmap getExperimentQr() {
        return experimentQr;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public long[] getRunIdsForExperiment(long id) {
        return runRepository.getRunIdsForExperiment(id);
    }

    public ExperimentRepresentation getExperimentRepresentation() {
        WindowConfiguration wifiWindow = configurationRepository.getConfigurationForExperimentByType(currentExperiment.id, SourceTypeEnum.WIFI.name());
        WindowConfiguration bluetoothWindow = configurationRepository.getConfigurationForExperimentByType(currentExperiment.id, SourceTypeEnum.BLUETOOTH.name());
        WindowConfiguration bluetoothLeWindow = configurationRepository.getConfigurationForExperimentByType(currentExperiment.id, SourceTypeEnum.BLUETOOTH_LE.name());
        WindowConfiguration sensorWindow = configurationRepository.getConfigurationForExperimentByType(currentExperiment.id, SourceTypeEnum.SENSORS.name());
        WindowConfiguration cellWindow = configurationRepository.getConfigurationForExperimentByType(currentExperiment.id, SourceTypeEnum.CELL.name());
        WindowConfiguration gpsWindow = configurationRepository.getConfigurationForExperimentByType(currentExperiment.id, SourceTypeEnum.GPS.name());
        WindowConfiguration advertiseWindow = configurationRepository.getConfigurationForExperimentByType(currentExperiment.id, SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name());
        AdvertiseConfiguration advertiseConfiguration = configurationRepository.getBluetoothLeAdvertiseConfigurationFor(currentExperiment.id);
        List<String> tags =  experimentRepository.getTagList(currentExperiment.id);

        return new ExperimentRepresentation(currentExperiment, wifiWindow, bluetoothWindow,
                bluetoothLeWindow, sensorWindow, cellWindow, gpsWindow, advertiseWindow, advertiseConfiguration, tags, null);

    }
}
