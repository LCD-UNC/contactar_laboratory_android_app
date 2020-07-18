package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.rfdetke.digitriadlaboratory.entities.BluetoothRecord;

@Dao
public interface BluetoothRecordDao {

    @Insert
    void insert(BluetoothRecord bluetoothRecord);

    @Delete
    void delete(BluetoothRecord bluetoothRecord);
}
