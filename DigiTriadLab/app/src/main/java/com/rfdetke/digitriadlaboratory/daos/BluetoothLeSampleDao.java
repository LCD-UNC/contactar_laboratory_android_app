package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.BluetoothLeSample;

import java.util.Date;
import java.util.List;

@Dao
public interface BluetoothLeSampleDao {

    @Query("SELECT timestamp, address, rssi, tx_power, advertising_set_id, primary_physical_layer, " +
                  "seconary_physical_layer, periodic_advertising_interval, connectable, legacy " +
            "FROM bluetooth_le_sample " +
            "INNER JOIN bluetooth_le_record " +
            "ON bluetooth_le_sample.id=bluetooth_le_record.bluetooth_le_sample_id " +
            "WHERE run_id==(:runId)")
    List<SampleRecord> getAllSamplesRecordsByRunId(int runId);

    @Insert
    void insert(BluetoothLeSample bluetoothLeSample);

    @Delete
    void delete(BluetoothLeSample bluetoothLeSample);

    static class SampleRecord {
        public Date timestamp;
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
    }
}
