package com.contactar.contactarlaboratory.qr;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.entities.AdvertiseConfiguration;
import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.export.representations.ExperimentRepresentation;
import com.contactar.contactarlaboratory.repositories.ConfigurationRepository;

import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QR {

    public static Bitmap getQrExperiment(Experiment experiment,
                                         ConfigurationRepository configurationRepository,
                                         AdvertiseConfiguration advertiseConfiguration, List<String> tagList) {

        WindowConfiguration wifiWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.WIFI.name());
        WindowConfiguration bluetoothWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.BLUETOOTH.name());
        WindowConfiguration bluetoothLeWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.BLUETOOTH_LE.name());
        WindowConfiguration sensorWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.SENSORS.name());
        WindowConfiguration cellWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.CELL.name());
        WindowConfiguration gpsWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.GPS.name());
        WindowConfiguration batteryWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.BATTERY.name());
        WindowConfiguration activityWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.ACTIVITY.name());
        WindowConfiguration advertiseWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name());

        ExperimentRepresentation codedExperimentObject = new ExperimentRepresentation(
                experiment,
                wifiWindow,
                bluetoothWindow,
                bluetoothLeWindow,
                sensorWindow,
                cellWindow,
                gpsWindow,
                batteryWindow,
                activityWindow,
                advertiseWindow,
                advertiseConfiguration,
                tagList,
                null);
        Gson gson = new Gson();
        String jsonExperiment = gson.toJson(codedExperimentObject);

        return new QRGEncoder(jsonExperiment, null, QRGContents.Type.TEXT, 600).getBitmap();
    }
}
