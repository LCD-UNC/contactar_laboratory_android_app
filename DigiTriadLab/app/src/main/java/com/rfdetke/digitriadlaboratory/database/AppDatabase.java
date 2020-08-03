package com.rfdetke.digitriadlaboratory.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.rfdetke.digitriadlaboratory.database.daos.BluetoothLeRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.BluetoothRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.DeviceDao;
import com.rfdetke.digitriadlaboratory.database.daos.ExperimentDao;
import com.rfdetke.digitriadlaboratory.database.daos.RunDao;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowConfigurationDao;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.SourceTypeDao;
import com.rfdetke.digitriadlaboratory.database.daos.WifiRecordDao;

import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeUuid;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothRecord;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.database.entities.SourceType;
import com.rfdetke.digitriadlaboratory.database.entities.WifiRecord;
import com.rfdetke.digitriadlaboratory.database.entities.Sample;
import com.rfdetke.digitriadlaboratory.database.typeconverters.DateConverter;
import com.rfdetke.digitriadlaboratory.database.typeconverters.UuidConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {BluetoothLeRecord.class,
                        BluetoothLeUuid.class,
                        WindowConfiguration.class,
                        BluetoothRecord.class,
                        SensorRecord.class,
                        WifiRecord.class,
                        Sample.class,
                        Device.class,
                        Experiment.class,
                        Run.class,
                        SourceType.class},
          version = 1)

@TypeConverters({DateConverter.class, UuidConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract BluetoothLeRecordDao getBluetoothLeRecordDao();
    public abstract BluetoothRecordDao getBluetoothRecordDao();
    public abstract SensorRecordDao getSensorRecordDao();
    public abstract WifiRecordDao getWifiRecordDao();
    public abstract DeviceDao getDeviceDao();
    public abstract ExperimentDao getExperimentDao();
    public abstract RunDao getRunDao();
    public abstract SampleDao getSampleDao();
    public abstract SourceTypeDao getSourceTypeDao();
    public abstract WindowConfigurationDao getWindowConfigurationDao();

}
