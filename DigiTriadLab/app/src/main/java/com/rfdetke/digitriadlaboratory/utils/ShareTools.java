package com.rfdetke.digitriadlaboratory.utils;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.entities.AdvertiseConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.export.representations.ExperimentRepresentation;
import com.rfdetke.digitriadlaboratory.repositories.ConfigurationRepository;

import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ShareTools {

    public static Bitmap getQrExperiment(Experiment experiment,
                                         ConfigurationRepository configurationRepository,
                                         AdvertiseConfiguration advertiseConfiguration, List<String> tagList) {

        WindowConfiguration wifiWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.WIFI.name());
        WindowConfiguration bluetoothWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.BLUETOOTH.name());
        WindowConfiguration bluetoothLeWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.BLUETOOTH_LE.name());
        WindowConfiguration sensorWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.SENSORS.name());
        WindowConfiguration cellWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.CELL.name());
        WindowConfiguration advertiseWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name());

        ExperimentRepresentation codedExperimentObject = new ExperimentRepresentation(experiment, wifiWindow, bluetoothWindow, bluetoothLeWindow, sensorWindow, cellWindow, advertiseWindow, advertiseConfiguration, tagList, null);
        Gson gson = new Gson();
        String jsonExperiment = gson.toJson(codedExperimentObject);

        return new QRGEncoder(jsonExperiment, null, QRGContents.Type.TEXT, 600).getBitmap();
    }

    public static ExperimentRepresentation getCodedExperiment(String jsonExperiment) {
        Gson gson = new Gson();
        return gson.fromJson(jsonExperiment, ExperimentRepresentation.class);
    }
}
