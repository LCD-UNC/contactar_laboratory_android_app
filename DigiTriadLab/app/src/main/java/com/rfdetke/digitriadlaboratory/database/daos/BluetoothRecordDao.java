package com.rfdetke.digitriadlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.rfdetke.digitriadlaboratory.database.entities.BluetoothRecord;

@Dao
public interface BluetoothRecordDao {

    @Insert
    long insert(BluetoothRecord bluetoothRecord);

    @Delete
    void delete(BluetoothRecord bluetoothRecord);
}
