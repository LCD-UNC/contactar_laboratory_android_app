package com.rfdetke.digitriadlaboratory;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.rfdetke.digitriadlaboratory.daos.BluetoothLeRecordDao;
import com.rfdetke.digitriadlaboratory.daos.BluetoothRecordDao;
import com.rfdetke.digitriadlaboratory.daos.DeviceDao;
import com.rfdetke.digitriadlaboratory.daos.ExperimentDao;
import com.rfdetke.digitriadlaboratory.daos.RunDao;
import com.rfdetke.digitriadlaboratory.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.daos.WifiRecordDao;

import com.rfdetke.digitriadlaboratory.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.entities.ScanConfiguration;
import com.rfdetke.digitriadlaboratory.entities.BluetoothRecord;
import com.rfdetke.digitriadlaboratory.entities.Device;
import com.rfdetke.digitriadlaboratory.entities.Experiment;
import com.rfdetke.digitriadlaboratory.entities.Run;
import com.rfdetke.digitriadlaboratory.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.entities.SourceType;
import com.rfdetke.digitriadlaboratory.entities.WifiRecord;
import com.rfdetke.digitriadlaboratory.entities.Sample;
import com.rfdetke.digitriadlaboratory.typeconverters.DateConverter;

@Database(entities = {BluetoothLeRecord.class,
                        ScanConfiguration.class,
                        BluetoothRecord.class,
                        SensorRecord.class,
                        WifiRecord.class,
                        Sample.class,
                        Device.class,
                        Experiment.class,
                        Run.class,
                        SourceType.class},
          version = 1)

@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract BluetoothLeRecordDao getBluetoothLeRecordDao();
    public abstract BluetoothRecordDao getBluetoothRecordDao();
    public abstract SensorRecordDao getSensorRecordDao();
    public abstract WifiRecordDao getWifiRecordDao();
    public abstract DeviceDao getDeviceDao();
    public abstract ExperimentDao getExperimentDao();
    public abstract RunDao getRunDao();
    //TODO: Agregar Sample, SourceType y ScanConfiguration

}
