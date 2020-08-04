package com.rfdetke.digitriadlaboratory.utils;

import android.graphics.Bitmap;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.entities.AdvertiseConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.SourceType;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.ConfigurationRepository;
import com.rfdetke.digitriadlaboratory.repositories.SourceTypeRepository;

import java.util.List;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRTools {
    public static Bitmap getQrConfiguration(Experiment experiment,
                                            ConfigurationRepository configurationRepository,
                                            AdvertiseConfiguration advertiseConfiguration) {
        String code = String.format(Locale.ENGLISH,"%s;%s;",experiment.codename,experiment.description);

        WindowConfiguration wifiWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.WIFI.name());
        WindowConfiguration bluetoothWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.BLUETOOTH.name());
        WindowConfiguration bluetoothLeWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.BLUETOOTH_LE.name());
        WindowConfiguration advertiseWindow = configurationRepository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name());

        if (wifiWindow != null) {
            code = code.concat(String.format(Locale.ENGLISH,"%d;%d;%d;",
                    wifiWindow.activeTime, wifiWindow.inactiveTime, wifiWindow.windows));
        } else {
            code = code.concat(";;;");
        }

        if (bluetoothWindow != null) {
            code = code.concat(String.format(Locale.ENGLISH,"%d;%d;%d;",
                    bluetoothWindow.activeTime, bluetoothWindow.inactiveTime, bluetoothWindow.windows));
        } else {
            code = code.concat(";;;");
        }

        if (bluetoothLeWindow != null) {
            code = code.concat(String.format(Locale.ENGLISH,"%d;%d;%d;",
                    bluetoothLeWindow.activeTime, bluetoothLeWindow.inactiveTime, bluetoothLeWindow.windows));
        } else {
            code = code.concat(";;;");
        }

        if (advertiseWindow != null) {
            code = code.concat(String.format(Locale.ENGLISH,"%d;%d;%d;%d;%d;",
                    advertiseWindow.activeTime, advertiseWindow.inactiveTime, advertiseWindow.windows,
                    advertiseConfiguration.txPower,advertiseConfiguration.interval));
        } else {
            code = code.concat(";;;;;");
        }

        return new QRGEncoder(code, null, QRGContents.Type.TEXT, 600).getBitmap();
    }
}
