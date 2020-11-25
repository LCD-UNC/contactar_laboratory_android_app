package com.contactar.digitriadlaboratory.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.contactar.digitriadlaboratory.database.daos.ActivityRecordDao;
import com.contactar.digitriadlaboratory.database.daos.BatteryRecordDao;
import com.contactar.digitriadlaboratory.database.daos.BluetoothLeAdvertiseConfigurationDao;
import com.contactar.digitriadlaboratory.database.daos.BluetoothLeRecordDao;
import com.contactar.digitriadlaboratory.database.daos.BluetoothRecordDao;
import com.contactar.digitriadlaboratory.database.daos.CellRecordDao;
import com.contactar.digitriadlaboratory.database.daos.DeviceDao;
import com.contactar.digitriadlaboratory.database.daos.ExperimentDao;
import com.contactar.digitriadlaboratory.database.daos.GpsRecordDao;
import com.contactar.digitriadlaboratory.database.daos.RunDao;
import com.contactar.digitriadlaboratory.database.daos.SensorRecordDao;
import com.contactar.digitriadlaboratory.database.daos.SourceTypeDao;
import com.contactar.digitriadlaboratory.database.daos.TagDao;
import com.contactar.digitriadlaboratory.database.daos.WifiRecordDao;
import com.contactar.digitriadlaboratory.database.daos.WindowConfigurationDao;
import com.contactar.digitriadlaboratory.database.daos.WindowDao;
import com.contactar.digitriadlaboratory.database.entities.ActivityRecord;
import com.contactar.digitriadlaboratory.database.entities.AdvertiseConfiguration;
import com.contactar.digitriadlaboratory.database.entities.BatteryRecord;
import com.contactar.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.contactar.digitriadlaboratory.database.entities.BluetoothLeUuid;
import com.contactar.digitriadlaboratory.database.entities.BluetoothRecord;
import com.contactar.digitriadlaboratory.database.entities.CellRecord;
import com.contactar.digitriadlaboratory.database.entities.Device;
import com.contactar.digitriadlaboratory.database.entities.Experiment;
import com.contactar.digitriadlaboratory.database.entities.ExperimentTag;
import com.contactar.digitriadlaboratory.database.entities.GpsRecord;
import com.contactar.digitriadlaboratory.database.entities.Run;
import com.contactar.digitriadlaboratory.database.entities.SensorRecord;
import com.contactar.digitriadlaboratory.database.entities.SourceType;
import com.contactar.digitriadlaboratory.database.entities.Tag;
import com.contactar.digitriadlaboratory.database.entities.WifiRecord;
import com.contactar.digitriadlaboratory.database.entities.Window;
import com.contactar.digitriadlaboratory.database.entities.WindowConfiguration;
import com.contactar.digitriadlaboratory.database.typeconverters.DateConverter;
import com.contactar.digitriadlaboratory.database.typeconverters.UuidConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {BluetoothLeRecord.class,
                        BluetoothLeUuid.class,
                        AdvertiseConfiguration.class,
                        WindowConfiguration.class,
                        BluetoothRecord.class,
                        SensorRecord.class,
                        CellRecord.class,
                        GpsRecord.class,
                        BatteryRecord.class,
                        ActivityRecord.class,
                        WifiRecord.class,
                        Window.class,
                        Device.class,
                        Experiment.class,
                        ExperimentTag.class,
                        Tag.class,
                        Run.class,
                        SourceType.class},
          version = 19)

@TypeConverters({DateConverter.class, UuidConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract BluetoothLeRecordDao getBluetoothLeRecordDao();
    public abstract BluetoothRecordDao getBluetoothRecordDao();
    public abstract SensorRecordDao getSensorRecordDao();
    public abstract CellRecordDao getCellRecordDao();
    public abstract GpsRecordDao getGpsRecordDao();
    public abstract BatteryRecordDao getBatteryRecordDao();
    public abstract ActivityRecordDao getActivityRecordDao();
    public abstract WifiRecordDao getWifiRecordDao();
    public abstract DeviceDao getDeviceDao();
    public abstract ExperimentDao getExperimentDao();
    public abstract RunDao getRunDao();
    public abstract WindowDao getWindowDao();
    public abstract SourceTypeDao getSourceTypeDao();
    public abstract WindowConfigurationDao getWindowConfigurationDao();
    public abstract BluetoothLeAdvertiseConfigurationDao getBluetoothAdvertiseConfigurationDao();
    public abstract TagDao getTagDao();
}
