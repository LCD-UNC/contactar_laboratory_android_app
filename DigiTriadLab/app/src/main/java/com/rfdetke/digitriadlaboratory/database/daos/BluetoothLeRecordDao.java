package com.rfdetke.digitriadlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeRecord;

@Dao
public interface BluetoothLeRecordDao {

    @Insert
    long insert(BluetoothLeRecord bluetoothLeRecord);

    @Delete
    void delete(BluetoothLeRecord bluetoothLeRecord);
}