package com.rfdetke.digitriadlaboratory.database.daos;

import android.os.ParcelUuid;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
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

    @Query("SELECT s.run_id, s.id as sample_id, r.timestamp AS record_timestamp, " +
            "r.address, r.bluetooth_major_class, " +
            "r.bond_state, r.type " +
            "FROM window as s " +
            "LEFT JOIN bluetooth_record as r ON r.window_id=s.id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=\"BLUETOOTH\" ORDER BY record_timestamp, run_id, sample_id")
    List<BluetoothSampleRecord> getBluetoothSamplesRecords(long[] runId);

    @Query("SELECT COUNT(DISTINCT r.id) as quantity FROM bluetooth_record AS r " +
            "JOIN window AS s ON  r.window_id=s.id " +
            "WHERE s.run_id == (:runId) GROUP BY s.run_id")
    LiveData<Long> getBluetoothLiveCount(long runId);


    // ------------------- BLUETOOTH LE ------------------------------------------------------------

    @Query("SELECT s.run_id, s.id as sample_id, r.timestamp AS record_timestamp, " +
            "r.address, r.rssi, r.tx_power, " +
            "r.advertising_set_id, r.primary_physical_layer, r.seconary_physical_layer, " +
            "r.periodic_advertising_interval, r.connectable, r.legacy, r.uuid " +
            "FROM window as s " +
            "LEFT JOIN (SELECT r.*, uu.uuid FROM bluetooth_le_record as r " +
                        "LEFT JOIN bluetooth_le_uuid as uu ON r.id=uu.record_id) as r ON r.window_id=s.id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=\"BLUETOOTH_LE\" ORDER BY record_timestamp, run_id, sample_id")
    List<BluetoothLeSampleRecord> getBluetoothLeSamplesRecords(long[] runId);


    @Query("SELECT COUNT(DISTINCT r.id) as quantity FROM bluetooth_le_record AS r " +
            "JOIN window AS s ON  r.window_id=s.id " +
            "WHERE s.run_id == (:runId) GROUP BY s.run_id")
    LiveData<Long> getBluetoothLeLiveCount(long runId);


    // ------------------- WIFI --------------------------------------------------------------------

    @Query("SELECT s.run_id, s.id as sample_id,r.timestamp AS record_timestamp, " +
            "r.address, r.channel_width, " +
            "r.center_frequency_0, r.center_frequency_1, r.frequency, r.level, r.passpoint " +
            "FROM window as s " +
            "LEFT JOIN wifi_record as r ON r.window_id=s.id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=\"WIFI\" ORDER BY record_timestamp, run_id, window_id")
    List<WifiSampleRecord> getWifiSamplesRecords(long[] runId);

    @Query("SELECT COUNT(DISTINCT r.id) as quantity FROM wifi_record AS r " +
            "JOIN window AS s ON  r.window_id=s.id " +
            "WHERE s.run_id == (:runId) GROUP BY s.run_id")
    LiveData<Long> getWifiLiveCount(long runId);


    // ------------------- SENSORS -----------------------------------------------------------------

    @Query("SELECT s.run_id, s.id as window_id, s.timestamp, r.sensor_type, r.value_id, r.value " +
            "FROM sensor_record as r " +
            "INNER JOIN window as s ON r.window_id=s.id " +
            "WHERE s.run_id IN (:runId) ORDER BY s.timestamp, run_id, window_id")
    List<SensorSampleRecord> getSensorSamplesRecords(long[] runId);

    @Query("SELECT COUNT(DISTINCT r.id) as quantity FROM sensor_record AS r " +
            "JOIN window AS s ON  r.window_id=s.id " +
            "WHERE s.run_id == (:runId) GROUP BY s.run_id")
    LiveData<Long> getSensorLiveCount(long runId);

    @Insert
    long insert(Window window);

    @Query("SELECT * FROM window WHERE id==(:id)")
    Window getById(long id);

    static class BluetoothSampleRecord implements CsvExportable {
        @ColumnInfo(name = "record_timestamp")
        public Date recordTimestamp;
        @ColumnInfo(name = "run_id")
        public long runId;
        @ColumnInfo(name = "sample_id")
        public long windowId;
        public String address;
        @ColumnInfo(name = "bluetooth_major_class")
        public String bluetoothMajorClass;
        @ColumnInfo(name = "bond_state")
        public int bondState;
        public String type;

        @Override
        public String csvHeader() {
            return "record_timestamp,run_id,window_id,address,bluetooth_major_class,bond_state,type\n";
        }

        @Override
        public String toCsv() {
            return String.format(Locale.ENGLISH, "%d,%d,%d,%s,%s,%d,%s\n",
                    DateConverter.dateToTimestamp(recordTimestamp),
                    runId, windowId, address, bluetoothMajorClass, bondState, type);
        }
    }

    static class BluetoothLeSampleRecord implements CsvExportable {
        @ColumnInfo(name = "record_timestamp")
        public Date recordTimestamp;
        @ColumnInfo(name = "run_id")
        public long runId;
        @ColumnInfo(name = "sample_id")
        public long windowId;
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
            return "record_timestamp,run_id,window_id,address,rssi," +
                    "tx_power,advertising_set_id,primary_physical_layer,seconary_physical_layer," +
                    "periodic_advertising_interval,connectable,legacy,uuid\n";
        }

        @Override
        public String toCsv() {
            return String.format(Locale.ENGLISH, "%d,%d,%d,%s,%f,%f,%d,%s,%s,%f,%d,%d,%s\n",
                    DateConverter.dateToTimestamp(recordTimestamp), runId, windowId, address,
                    rssi, txPower, advertisingSetId, primaryPhysicalLayer, secondaryPhysicalLayer,
                    periodicAdvertisingInterval, connectable, legacy, UuidConverter.uuidToString(uuid));
        }
    }

    static class WifiSampleRecord implements CsvExportable {
        @ColumnInfo(name = "record_timestamp")
        public Date recordTimestamp;
        @ColumnInfo(name = "run_id")
        public long runId;
        @ColumnInfo(name = "sample_id")
        public long windowId;
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
            return "record_timestamp,run_id,window_id,address,channel_width," +
                    "center_frequency_0,center_frequency_1,frequency,level,passpoint\n";
        }

        @Override
        public String toCsv() {
            return String.format(Locale.ENGLISH, "%d,%d,%d,%s,%s,%d,%d,%d,%d,%d\n",
                    DateConverter.dateToTimestamp(recordTimestamp), runId, windowId, address,
                    channelWidth, centerFrequency0, centerFrequency1, frequency, level, passpoint);
        }
    }

    static class SensorSampleRecord implements CsvExportable {
        public Date timestamp;
        @ColumnInfo(name = "run_id")
        public long runId;
        @ColumnInfo(name = "window_id")
        public long windowId;
        @ColumnInfo(name = "sensor_type")
        public String sensorType;
        @ColumnInfo(name = "value_id")
        public int valueId;
        public double value;

        @Override
        public String csvHeader() {
            return "timestamp,run_id,window_id,sensor_type,value_id,value\n";
        }

        @Override
        public String toCsv() {
            return String.format(Locale.ENGLISH, "%d,%d,%d,%s,%d,%f\n",
                    DateConverter.dateToTimestamp(timestamp), runId, windowId, sensorType,
                    valueId, value);
        }
    }
}
