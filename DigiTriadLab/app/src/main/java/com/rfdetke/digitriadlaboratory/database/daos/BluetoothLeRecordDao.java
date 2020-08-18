package com.rfdetke.digitriadlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeUuid;

import java.util.List;

@Dao
public interface BluetoothLeRecordDao {

    @Insert
    long[] insertUuids(List<BluetoothLeUuid> bluetoothLeUuids);

    @Insert
    long[] insert(List<BluetoothLeRecord> bluetoothLeRecord);
}
