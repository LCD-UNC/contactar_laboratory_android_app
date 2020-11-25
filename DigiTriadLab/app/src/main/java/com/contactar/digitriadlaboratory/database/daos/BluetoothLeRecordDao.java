package com.contactar.digitriadlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.contactar.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.contactar.digitriadlaboratory.database.entities.BluetoothLeUuid;

import java.util.List;

@Dao
public interface BluetoothLeRecordDao {

    @Insert
    long[] insertUuids(List<BluetoothLeUuid> bluetoothLeUuids);

    @Insert
    long[] insert(List<BluetoothLeRecord> bluetoothLeRecord);
}
