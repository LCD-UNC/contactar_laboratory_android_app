package com.contactar.contactarlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.contactar.contactarlaboratory.database.entities.BluetoothRecord;

import java.util.List;

@Dao
public interface BluetoothRecordDao {

    @Insert
    long[] insert(List<BluetoothRecord> bluetoothRecord);

    @Insert
    long insert(BluetoothRecord bluetoothRecord);

    @Delete
    void delete(BluetoothRecord bluetoothRecord);
}
