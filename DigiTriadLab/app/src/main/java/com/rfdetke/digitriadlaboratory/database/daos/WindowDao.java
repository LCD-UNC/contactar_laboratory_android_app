package com.rfdetke.digitriadlaboratory.database.daos;

import android.os.ParcelUuid;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.database.entities.Window;
import com.rfdetke.digitriadlaboratory.database.typeconverters.DateConverter;
import com.rfdetke.digitriadlaboratory.database.typeconverters.UuidConverter;
import com.rfdetke.digitriadlaboratory.export.csv.CsvExportable;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@Dao
public interface WindowDao {

    // ------------------- BLUETOOTH ---------------------------------------------------------------

    @Query("SELECT rn.number as run, s.number as window, r.timestamp AS record_timestamp, " +
            "r.address, r.bluetooth_major_class, " +
            "r.bond_state, r.type " +
            "FROM window as s " +
            "LEFT JOIN bluetooth_record as r ON r.window_id=s.id " +
            "INNER JOIN run as rn ON rn.id=s.run_id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=\"BLUETOOTH\" ORDER BY record_timestamp, run, window")
    List<BluetoothSampleRecord> getBluetoothSamplesRecords(long[] runId);

    @Query("SELECT COUNT(DISTINCT r.id) as quantity FROM bluetooth_record AS r " +
            "JOIN window AS s ON  r.window_id=s.id " +
            "WHERE s.run_id == (:runId) GROUP BY s.run_id")
    LiveData<Long> getBluetoothLiveCount(long runId);


    // ------------------- BLUETOOTH LE ------------------------------------------------------------

    @Query("SELECT rn.number as run, s.number as window, r.timestamp AS record_timestamp, " +
            "r.address, r.rssi, r.tx_power, " +
            "r.advertising_set_id, r.primary_physical_layer, r.seconary_physical_layer, " +
            "r.periodic_advertising_interval, r.connectable, r.legacy, r.uuid " +
            "FROM window as s " +
            "LEFT JOIN (SELECT r.*, uu.uuid FROM bluetooth_le_record as r " +
                        "LEFT JOIN bluetooth_le_uuid as uu ON r.id=uu.record_id) as r ON r.window_id=s.id " +
            "INNER JOIN run as rn ON rn.id=s.run_id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=\"BLUETOOTH_LE\" ORDER BY record_timestamp, run, window")
    List<BluetoothLeSampleRecord> getBluetoothLeSamplesRecords(long[] runId);


    @Query("SELECT COUNT(DISTINCT r.id) as quantity FROM bluetooth_le_record AS r " +
            "JOIN window AS s ON  r.window_id=s.id " +
            "WHERE s.run_id == (:runId) GROUP BY s.run_id")
    LiveData<Long> getBluetoothLeLiveCount(long runId);


    // ------------------- WIFI --------------------------------------------------------------------

    @Query("SELECT rn.number as run, s.number as window, r.timestamp AS record_timestamp, " +
            "r.address, r.channel_width, " +
            "r.center_frequency_0, r.center_frequency_1, r.frequency, r.level, r.passpoint " +
            "FROM window as s " +
            "LEFT JOIN wifi_record as r ON r.window_id=s.id " +
            "INNER JOIN run as rn ON rn.id=s.run_id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=\"WIFI\" ORDER BY record_timestamp, run, window")
    List<WifiSampleRecord> getWifiSamplesRecords(long[] runId);

    @Query("SELECT COUNT(DISTINCT r.id) as quantity FROM wifi_record AS r " +
            "JOIN window AS s ON  r.window_id=s.id " +
            "WHERE s.run_id == (:runId) GROUP BY s.run_id")
    LiveData<Long> getWifiLiveCount(long runId);


    // ------------------- SENSORS -----------------------------------------------------------------

    @Query("SELECT rn.number as run, s.number as window, s.timestamp, r.sensor_type, r.value_id, r.value " +
            "FROM sensor_record as r " +
            "INNER JOIN window as s ON r.window_id=s.id " +
            "INNER JOIN run as rn ON rn.id=s.run_id " +
            "WHERE s.run_id IN (:runId) ORDER BY s.timestamp, run, window")
    List<SensorSampleRecord> getSensorSamplesRecords(long[] runId);

    @Query("SELECT COUNT(DISTINCT r.id) as quantity FROM sensor_record AS r " +
            "JOIN window AS s ON  r.window_id=s.id " +
            "WHERE s.run_id == (:runId) GROUP BY s.run_id")
    LiveData<Long> getSensorLiveCount(long runId);

    // ------------------- CELL -----------------------------------------------------------------

    @Query("SELECT rn.number as run, s.number as window, s.timestamp, r.technology, r.dbm, r.asu_level " +
            "FROM window as s " +
            "LEFT JOIN cell_record as r ON r.window_id=s.id " +
            "INNER JOIN run as rn ON rn.id=s.run_id " +
            "WHERE s.run_id IN (:runId) ORDER BY s.timestamp, run, window")
    List<CellSampleRecord> getCellSamplesRecords(long[] runId);

    @Query("SELECT COUNT(DISTINCT r.id) as quantity FROM cell_record AS r " +
            "JOIN window AS s ON  r.window_id=s.id " +
            "WHERE s.run_id == (:runId) GROUP BY s.run_id")
    LiveData<Long> getCellLiveCount(long runId);

    @Insert
    long insert(Window window);

    @Query("SELECT * FROM window WHERE id==(:id)")
    Window getById(long id);



    class BluetoothSampleRecord implements CsvExportable {
        @ColumnInfo(name = "record_timestamp")
        public Date recordTimestamp;
        public long run;
        public long window;
        public String address;
        @ColumnInfo(name = "bluetooth_major_class")
        public String bluetoothMajorClass;
        @ColumnInfo(name = "bond_state")
        public int bondState;
        public String type;

        @Override
        public String csvHeader() {
            return "record_timestamp,run,window,address,bluetooth_major_class,bond_state,type\n";
        }

        @Override
        public String toCsv() {
            return String.format(Locale.ENGLISH, "%d,%d,%d,%s,%s,%d,%s\n",
                    DateConverter.dateToTimestamp(recordTimestamp),
                    run, window, address, bluetoothMajorClass, bondState, type);
        }
    }

    class BluetoothLeSampleRecord implements CsvExportable {
        @ColumnInfo(name = "record_timestamp")
        public Date recordTimestamp;
        public long run;
        public long window;
        public String address;
        public double rssi;
        @ColumnInfo(name = "tx_power")
        public double txPower;
        @ColumnInfo(name = "advertising_set_id")
        public int advertisingSetId;
        @ColumnInfo(name = "primary_physical_layer")
        public String primaryPhysicalLayer;
        @ColumnInfo(name = "seconary_physical_layer")
        public String secondaryPhysicalLayer;
        @ColumnInfo(name = "periodic_advertising_interval")
        public double periodicAdvertisingInterval;
        public int connectable;
        public int legacy;
        public ParcelUuid uuid;

        @Override
        public String csvHeader() {
            return "record_timestamp,run,window,address,rssi," +
                    "tx_power,advertising_set_id,primary_physical_layer,seconary_physical_layer," +
                    "periodic_advertising_interval,connectable,legacy,uuid\n";
        }

        @Override
        public String toCsv() {
            return String.format(Locale.ENGLISH, "%d,%d,%d,%s,%f,%f,%d,%s,%s,%f,%d,%d,%s\n",
                    DateConverter.dateToTimestamp(recordTimestamp), run, window, address,
                    rssi, txPower, advertisingSetId, primaryPhysicalLayer, secondaryPhysicalLayer,
                    periodicAdvertisingInterval, connectable, legacy, UuidConverter.uuidToString(uuid));
        }
    }

    class WifiSampleRecord implements CsvExportable {
        @ColumnInfo(name = "record_timestamp")
        public Date recordTimestamp;
        public long run;
        public long window;
        public String address;
        @ColumnInfo(name = "channel_width")
        public String channelWidth;
        @ColumnInfo(name = "center_frequency_0")
        public int centerFrequency0;
        @ColumnInfo(name = "center_frequency_1")
        public int centerFrequency1;
        public int frequency;
        public int level;
        public int passpoint;

        @Override
        public String csvHeader() {
            return "record_timestamp,run,window,address,channel_width," +
                    "center_frequency_0,center_frequency_1,frequency,level,passpoint\n";
        }

        @Override
        public String toCsv() {
            return String.format(Locale.ENGLISH, "%d,%d,%d,%s,%s,%d,%d,%d,%d,%d\n",
                    DateConverter.dateToTimestamp(recordTimestamp), run, window, address,
                    channelWidth, centerFrequency0, centerFrequency1, frequency, level, passpoint);
        }
    }

    class SensorSampleRecord implements CsvExportable {
        public Date timestamp;
        public long run;
        public long window;
        @ColumnInfo(name = "sensor_type")
        public String sensorType;
        @ColumnInfo(name = "value_id")
        public int valueId;
        public double value;

        @Override
        public String csvHeader() {
            return "timestamp,run,window_id,sensor_type,value_id,value\n";
        }

        @Override
        public String toCsv() {
            return String.format(Locale.ENGLISH, "%d,%d,%d,%s,%d,%f\n",
                    DateConverter.dateToTimestamp(timestamp), run, window, sensorType,
                    valueId, value);
        }
    }

    class CellSampleRecord implements CsvExportable {
        public Date timestamp;
        public long run;
        public long window;
        @ColumnInfo(name = "technology")
        public String technology;
        @ColumnInfo(name = "dbm")
        public int dbm;
        @ColumnInfo(name = "asu_level")
        public int asuLevel;

        @Override
        public String csvHeader() {
            return "timestamp,run,window,technology,dbm,asu_level\n";
        }

        @Override
        public String toCsv() {
            return String.format(Locale.ENGLISH, "%d,%d,%d,%s,%d,%d\n",
                    DateConverter.dateToTimestamp(timestamp), run, window, technology,
                    dbm, asuLevel);
        }
    }
}
