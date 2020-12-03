package com.contactar.contactarlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;

import com.contactar.contactarlaboratory.database.entities.BluetoothLeRecord;
import com.contactar.contactarlaboratory.database.entities.BluetoothLeUuid;

import java.util.List;

@Dao
public interface BluetoothLeRecordDao {

    @Insert
    long[] insertUuids(List<BluetoothLeUuid> bluetoothLeUuids);

    @Insert
    long[] insert(List<BluetoothLeRecord> bluetoothLeRecord);
}
