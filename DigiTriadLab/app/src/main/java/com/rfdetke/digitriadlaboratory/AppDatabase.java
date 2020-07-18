package com.rfdetke.digitriadlaboratory;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.rfdetke.digitriadlaboratory.daos.BluetoothLeRecordDao;
import com.rfdetke.digitriadlaboratory.daos.BluetoothLeSampleDao;
import com.rfdetke.digitriadlaboratory.daos.BluetoothLeScanConfigurationDao;
import com.rfdetke.digitriadlaboratory.daos.BluetoothRecordDao;
import com.rfdetke.digitriadlaboratory.daos.BluetoothSampleDao;
import com.rfdetke.digitriadlaboratory.daos.BluetoothScanConfigurationDao;
import com.rfdetke.digitriadlaboratory.daos.DeviceDao;
import com.rfdetke.digitriadlaboratory.daos.ExperimentDao;
import com.rfdetke.digitriadlaboratory.daos.RunDao;
import com.rfdetke.digitriadlaboratory.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.daos.SensorsSampleDao;
import com.rfdetke.digitriadlaboratory.daos.SensorsScanConfigurationDao;
import com.rfdetke.digitriadlaboratory.daos.WifiRecordDao;
import com.rfdetke.digitriadlaboratory.daos.WifiSampleDao;
import com.rfdetke.digitriadlaboratory.daos.WifiScanConfigurationDao;
import com.rfdetke.digitriadlaboratory.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.entities.BluetoothLeSample;
import com.rfdetke.digitriadlaboratory.entities.BluetoothLeScanConfiguration;
import com.rfdetke.digitriadlaboratory.entities.BluetoothRecord;
import com.rfdetke.digitriadlaboratory.entities.BluetoothSample;
import com.rfdetke.digitriadlaboratory.entities.BluetoothScanConfiguration;
import com.rfdetke.digitriadlaboratory.entities.Device;
import com.rfdetke.digitriadlaboratory.entities.Experiment;
import com.rfdetke.digitriadlaboratory.entities.Run;
import com.rfdetke.digitriadlaboratory.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.entities.SensorsSample;
import com.rfdetke.digitriadlaboratory.entities.SensorsScanConfiguration;
import com.rfdetke.digitriadlaboratory.entities.WifiRecord;
import com.rfdetke.digitriadlaboratory.entities.WifiSample;
import com.rfdetke.digitriadlaboratory.entities.WifiScanConfiguration;
import com.rfdetke.digitriadlaboratory.typeconverters.DateConverter;

@Database(entities = {BluetoothLeRecord.class,
                        BluetoothLeSample.class,
                        BluetoothLeScanConfiguration.class,
                        BluetoothRecord.class,
                        BluetoothSample.class,
                        BluetoothScanConfiguration.class,
                        SensorRecord.class,
                        SensorsSample.class,
                        SensorsScanConfiguration.class,
                        WifiRecord.class,
                        WifiSample.class,
                        WifiScanConfiguration.class,
                        Device.class,
                        Experiment.class,
                        Run.class},
          version = 1)

@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract BluetoothLeRecordDao getBluetoothLeRecordDao();
    public abstract BluetoothLeSampleDao getBluetoothLeSampleDao();
    public abstract BluetoothLeScanConfigurationDao getBluetoothLeScanConfigurationDao();
    public abstract BluetoothRecordDao getBluetoothRecordDao();
    public abstract BluetoothSampleDao getBluetoothSampleDao();
    public abstract BluetoothScanConfigurationDao getBluetoothScanConfigurationDao();
    public abstract SensorRecordDao getSensorRecordDao();
    public abstract SensorsSampleDao getSensorsSampleDao();
    public abstract SensorsScanConfigurationDao getSensorsScanConfigurationDao();
    public abstract WifiRecordDao getWifiRecordDao();
    public abstract WifiSampleDao getWifiSampleDao();
    public abstract WifiScanConfigurationDao getWifiScanConfigurationDao();
    public abstract DeviceDao getDeviceDao();
    public abstract ExperimentDao getExperimentDao();
    public abstract RunDao getRunDao();

}
