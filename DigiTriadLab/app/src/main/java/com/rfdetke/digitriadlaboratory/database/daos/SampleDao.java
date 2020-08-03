package com.rfdetke.digitriadlaboratory.database.daos;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.database.entities.Sample;
import com.rfdetke.digitriadlaboratory.database.typeconverters.DateConverter;
import com.rfdetke.digitriadlaboratory.export.CsvExportable;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Dao
public interface SampleDao {

    @Query("SELECT s.run_id, s.id as sample_id, s.timestamp, r.id as record_id, r.address, r.bluetooth_major_class, " +
            "r.bond_state, r.type " +
            "FROM sample as s " +
            "LEFT JOIN bluetooth_record as r ON r.sample_id=s.id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=\"BLUETOOTH\" ORDER BY run_id, sample_id, record_id")
    List<BluetoothSampleRecord> getBluetoothSamplesRecords(long[] runId);

    @Query("SELECT DISTINCT s.id as sample_id " +
            "FROM sample as s " +
            "LEFT JOIN bluetooth_record as r ON r.sample_id=s.id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=\"BLUETOOTH\" ORDER BY run_id, sample_id")
    long[] getDistinctBluetoothSampleIds(long[] runId);

    @Query("SELECT s.run_id, s.id as sample_id, s.timestamp, r.id as record_id, r.address, r.rssi, r.tx_power, " +
            "r.advertising_set_id, r.primary_physical_layer, r.seconary_physical_layer, " +
            "r.periodic_advertising_interval, r.connectable, r.legacy " +
            "FROM sample as s " +
            "LEFT JOIN bluetooth_le_record as r ON r.sample_id=s.id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=\"BLUETOOTH_LE\" ORDER BY run_id, sample_id, record_id")
    List<BluetoothLeSampleRecord> getBluetoothLeSamplesRecords(long[] runId);

    @Query("SELECT DISTINCT s.id as sample_id " +
            "FROM sample as s " +
            "LEFT JOIN bluetooth_le_record as r ON r.sample_id=s.id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=\"BLUETOOTH_LE\" ORDER BY run_id, sample_id")
    long[] getDistinctBluetoothLeSampleIds(long[] runId);

    @Query("SELECT s.run_id, s.id as sample_id, s.timestamp, r.id as record_id, r.address, r.channel_width, " +
            "r.center_frequency_0, r.center_frequency_1, r.frequency, r.level, r.passpoint " +
            "FROM sample as s " +
            "LEFT JOIN wifi_record as r ON r.sample_id=s.id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=\"WIFI\" ORDER BY run_id, sample_id, record_id")
    List<WifiSampleRecord> getWifiSamplesRecords(long[] runId);

    @Query("SELECT DISTINCT s.id as sample_id " +
            "FROM sample as s " +
            "LEFT JOIN wifi_record as r ON r.sample_id=s.id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=\"WIFI\" ORDER BY run_id, sample_id")
    long[] getDistinctWifiSampleIds(long[] runId);

    @Query("SELECT s.run_id, s.id as sample_id, s.timestamp, r.id as record_id, r.sensor_type, r.value_id, r.value " +
            "FROM sensor_record as r " +
            "INNER JOIN sample as s ON r.sample_id=s.id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=(:type) ORDER BY run_id, sample_id, record_id")
    List<SensorSampleRecord> getSensorSamplesRecords(long[] runId, String type);

    @Query("SELECT s.run_id, s.id as sample_id, s.timestamp, r.id as record_id, r.sensor_type, r.value_id, r.value " +
            "FROM sensor_record as r " +
            "INNER JOIN sample as s ON r.sample_id=s.id " +
            "WHERE s.run_id IN (:runId) ORDER BY run_id, sample_id, record_id")
    List<SensorSampleRecord> getSensorSamplesRecords(long[] runId);

    @Query("SELECT DISTINCT s.id as sample_id " +
            "FROM sensor_record as r " +
            "INNER JOIN sample as s ON r.sample_id=s.id " +
            "INNER JOIN source_type AS st ON s.source_type=st.id " +
            "WHERE s.run_id IN(:runId) AND st.type=(:type) ORDER BY run_id, sample_id")
    long[] getDistinctSensorSampleIds(long[] runId, String type);


    @Insert
    long insert(Sample sample);

    @Delete
    void delete(Sample sample);

    static class BluetoothSampleRecord implements CsvExportable {
        public Date timestamp;
        @ColumnInfo(name = "run_id")
        public long runId;
        @ColumnInfo(name = "sample_id")
        public long sampleId;
        @ColumnInfo(name = "record_id")
        public long recordId;
        public String address;
        @ColumnInfo(name = "bluetooth_major_class")
        public String bluetoothMajorClass;
        @ColumnInfo(name = "bond_state")
        public int bondState;
        public String type;

        @Override
        public String csvHeader() {
            return "timestamp,run_id,sample_id,record_id,address,bluetooth_major_class,bond_state,type\n";
        }

        @Override
        public String toCsv() {
            return String.format(Locale.ENGLISH, "%s,%d,%d,%d,%s,%s,%d,%s\n",
                    DateConverter.dateToTimestamp(timestamp), runId, sampleId, recordId, address,
                    bluetoothMajorClass, bondState, type);
        }
    }

    static class BluetoothLeSampleRecord implements CsvExportable {
        public Date timestamp;
        @ColumnInfo(name = "run_id")
        public long runId;
        @ColumnInfo(name = "sample_id")
        public long sampleId;
        @ColumnInfo(name = "record_id")
        public long recordId;
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


        @Override
        public String csvHeader() {
            return "timestamp,run_id,sample_id,record_id,address,rssi,tx_power,advertising_set_id," +
                    "primary_physical_layer,seconary_physical_layer,periodic_advertising_interval," +
                    "connectable,legacy\n";
        }

        @Override
        public String toCsv() {
            return String.format(Locale.ENGLISH, "%s,%d,%d,%d,%s,%f,%f,%d,%s,%s,%f,%d,%d\n",
                    DateConverter.dateToTimestamp(timestamp), runId, sampleId, recordId, address,
                    rssi, txPower, advertisingSetId, primaryPhysicalLayer, secondaryPhysicalLayer,
                    periodicAdvertisingInterval, connectable, legacy);
        }
    }

    static class WifiSampleRecord implements CsvExportable {
        public Date timestamp;
        @ColumnInfo(name = "run_id")
        public long runId;
        @ColumnInfo(name = "sample_id")
        public long sampleId;
        @ColumnInfo(name = "record_id")
        public long recordId;
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
            return "timestamp,run_id,sample_id,record_id,address,channel_width,center_frequency_0," +
                    "center_frequency_1,frequency,level,passpoint\n";
        }

        @Override
        public String toCsv() {
            return String.format(Locale.ENGLISH, "%s,%d,%d,%d,%s,%s,%d,%d,%d,%d,%d\n",
                    DateConverter.dateToTimestamp(timestamp), runId, sampleId, recordId, address,
                    channelWidth, centerFrequency0, centerFrequency1, frequency, level, passpoint);
        }
    }

    static class SensorSampleRecord implements CsvExportable {
        public Date timestamp;
        @ColumnInfo(name = "run_id")
        public long runId;
        @ColumnInfo(name = "sample_id")
        public long sampleId;
        @ColumnInfo(name = "record_id")
        public long recordId;
        @ColumnInfo(name = "sensor_type")
        public String sensorType;
        @ColumnInfo(name = "value_id")
        public int valueId;
        public double value;


        @Override
        public String csvHeader() {
            return "timestamp,run_id,sample_id,record_id,sensor_type,value_id,value\n";
        }

        @Override
        public String toCsv() {
            return String.format(Locale.ENGLISH, "%s,%d,%d,%d,%s,%d,%f\n",
                    DateConverter.dateToTimestamp(timestamp), runId, sampleId, recordId, sensorType,
                    valueId, value);
        }
    }
}
