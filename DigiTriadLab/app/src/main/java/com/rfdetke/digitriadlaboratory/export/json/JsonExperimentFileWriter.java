package com.rfdetke.digitriadlaboratory.export.json;

import android.content.Context;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.AdvertiseConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.export.FileWriter;
import com.rfdetke.digitriadlaboratory.export.representations.ExperimentRepresentation;
import com.rfdetke.digitriadlaboratory.repositories.ConfigurationRepository;
import com.rfdetke.digitriadlaboratory.repositories.ExperimentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JsonExperimentFileWriter extends JsonFileWriter {

    private final static String FILE_NAME = "experiment.json";

    private final ExperimentRepresentation representation;
    private final Experiment experiment;

    public JsonExperimentFileWriter(long experimentId, AppDatabase database, Context context) {
        super(context, database);
        ExperimentRepository experimentRepository = new ExperimentRepository(database);
        experiment = experimentRepository.getById(experimentId);
        ConfigurationRepository configurationRepository = new ConfigurationRepository(database);
        WindowConfiguration wifiWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.WIFI.name());
        WindowConfiguration bluetoothWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.BLUETOOTH.name());
        WindowConfiguration bluetoothLeWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.BLUETOOTH_LE.name());
        WindowConfiguration sensorWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.SENSORS.name());
        WindowConfiguration advertiseWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name());
        AdvertiseConfiguration advertiseConfiguration = configurationRepository.getBluetoothLeAdvertiseConfigurationFor(experimentId);
        List<String> tags =  experimentRepository.getTagList(experimentId);

        representation = new ExperimentRepresentation(experiment, wifiWindow, bluetoothWindow,
                bluetoothLeWindow, sensorWindow, advertiseWindow, advertiseConfiguration, tags);
    }

    @Override
    public List<JsonExportable> getExportableData() {
        List<JsonExportable> exportables = new ArrayList<>();
        exportables.add(representation);
        return exportables;
    }

    @Override
    public String getPath() {
        return String.format(Locale.ENGLISH, "exports/%s/", experiment.codename.toLowerCase());
    }

    @Override
    public String getFileName() {
        return FILE_NAME;
    }

}
