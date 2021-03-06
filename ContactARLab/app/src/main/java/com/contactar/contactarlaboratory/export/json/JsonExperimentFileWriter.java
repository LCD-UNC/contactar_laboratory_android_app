package com.contactar.contactarlaboratory.export.json;

import android.content.Context;

import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.entities.AdvertiseConfiguration;
import com.contactar.contactarlaboratory.database.entities.Device;
import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.export.representations.ExperimentRepresentation;
import com.contactar.contactarlaboratory.repositories.ConfigurationRepository;
import com.contactar.contactarlaboratory.repositories.DeviceRepository;
import com.contactar.contactarlaboratory.repositories.ExperimentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JsonExperimentFileWriter extends JsonFileWriter {

    private final ExperimentRepresentation representation;
    private final Experiment experiment;
    private final Device device;

    public JsonExperimentFileWriter(long experimentId, AppDatabase database, Context context) {
        super(context, database);
        ExperimentRepository experimentRepository = new ExperimentRepository(database);
        DeviceRepository deviceRepository = new DeviceRepository(database);
        device = deviceRepository.getDevice();
        experiment = experimentRepository.getById(experimentId);
        ConfigurationRepository configurationRepository = new ConfigurationRepository(database);
        WindowConfiguration wifiWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.WIFI.name());
        WindowConfiguration bluetoothWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.BLUETOOTH.name());
        WindowConfiguration bluetoothLeWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.BLUETOOTH_LE.name());
        WindowConfiguration sensorWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.SENSORS.name());
        WindowConfiguration cellWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.CELL.name());
        WindowConfiguration gpsWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.GPS.name());
        WindowConfiguration batteryWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.BATTERY.name());
        WindowConfiguration activityWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.ACTIVITY.name());
        WindowConfiguration advertiseWindow = configurationRepository.getConfigurationForExperimentByType(experimentId, SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name());
        AdvertiseConfiguration advertiseConfiguration = configurationRepository.getBluetoothLeAdvertiseConfigurationFor(experimentId);
        List<String> tags =  experimentRepository.getTagList(experimentId);

        representation = new ExperimentRepresentation(experiment, wifiWindow, bluetoothWindow,
                bluetoothLeWindow, sensorWindow, cellWindow, gpsWindow, batteryWindow, activityWindow ,advertiseWindow, advertiseConfiguration, tags, device);
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
        return String.format(Locale.ENGLISH, "X-%s_%s.json", experiment.codename.toLowerCase(), device.codename);
    }

}
