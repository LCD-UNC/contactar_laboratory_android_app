package com.contactar.contactarlaboratory.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.contactar.contactarlaboratory.database.daos.ActivityRecordDao;
import com.contactar.contactarlaboratory.database.daos.BatteryRecordDao;
import com.contactar.contactarlaboratory.database.daos.BluetoothLeAdvertiseConfigurationDao;
import com.contactar.contactarlaboratory.database.daos.BluetoothLeRecordDao;
import com.contactar.contactarlaboratory.database.daos.BluetoothRecordDao;
import com.contactar.contactarlaboratory.database.daos.CellRecordDao;
import com.contactar.contactarlaboratory.database.daos.DeviceDao;
import com.contactar.contactarlaboratory.database.daos.ExperimentDao;
import com.contactar.contactarlaboratory.database.daos.GpsRecordDao;
import com.contactar.contactarlaboratory.database.daos.RunDao;
import com.contactar.contactarlaboratory.database.daos.SensorRecordDao;
import com.contactar.contactarlaboratory.database.daos.SourceTypeDao;
import com.contactar.contactarlaboratory.database.daos.TagDao;
import com.contactar.contactarlaboratory.database.daos.WifiRecordDao;
import com.contactar.contactarlaboratory.database.daos.WindowConfigurationDao;
import com.contactar.contactarlaboratory.database.daos.WindowDao;
import com.contactar.contactarlaboratory.database.entities.ActivityRecord;
import com.contactar.contactarlaboratory.database.entities.AdvertiseConfiguration;
import com.contactar.contactarlaboratory.database.entities.BatteryRecord;
import com.contactar.contactarlaboratory.database.entities.BluetoothLeRecord;
import com.contactar.contactarlaboratory.database.entities.BluetoothLeUuid;
import com.contactar.contactarlaboratory.database.entities.BluetoothRecord;
import com.contactar.contactarlaboratory.database.entities.CellRecord;
import com.contactar.contactarlaboratory.database.entities.Device;
import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.database.entities.ExperimentTag;
import com.contactar.contactarlaboratory.database.entities.GpsRecord;
import com.contactar.contactarlaboratory.database.entities.Run;
import com.contactar.contactarlaboratory.database.entities.SensorRecord;
import com.contactar.contactarlaboratory.database.entities.SourceType;
import com.contactar.contactarlaboratory.database.entities.Tag;
import com.contactar.contactarlaboratory.database.entities.WifiRecord;
import com.contactar.contactarlaboratory.database.entities.Window;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.database.typeconverters.DateConverter;
import com.contactar.contactarlaboratory.database.typeconverters.UuidConverter;

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
