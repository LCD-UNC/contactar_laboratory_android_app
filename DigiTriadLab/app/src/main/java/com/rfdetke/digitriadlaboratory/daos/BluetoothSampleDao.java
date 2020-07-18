package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.BluetoothSample;

import java.util.Date;
import java.util.List;

@Dao
public interface BluetoothSampleDao {

    @Query("SELECT timestamp, address, bluetooth_class, bluetooth_major_class, bond_state, type " +
            "FROM bluetooth_sample " +
            "INNER JOIN bluetooth_record " +
            "ON bluetooth_sample.id=bluetooth_record.bluetooth_sample_id " +
            "WHERE run_id==(:runId)")
    List<SampleRecord> getAllSamplesRecordsByRunId(int runId);

    @Insert
    void insert(BluetoothSample bluetoothSample);

    @Delete
    void delete(BluetoothSample bluetoothSample);

    static class SampleRecord {
        public Date timestamp;
        public String address;
        @ColumnInfo(name = "bluetooth_class")
        public String bluetoothClass;
        @ColumnInfo(name = "bluetooth_major_class")
        public String bluetoothMajorClass;
        @ColumnInfo(name = "bond_state")
        public int bondState;
        public String type;
    }
}
