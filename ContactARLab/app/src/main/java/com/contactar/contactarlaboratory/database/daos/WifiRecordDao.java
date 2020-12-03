package com.contactar.contactarlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.contactar.contactarlaboratory.database.entities.WifiRecord;

import java.util.List;

@Dao
public interface WifiRecordDao {

    @Insert
    long[] insert(List<WifiRecord> wifiRecord);

    @Insert
    long insert(WifiRecord wifiRecord);

    @Delete
    void delete(WifiRecord wifiRecord);
}
