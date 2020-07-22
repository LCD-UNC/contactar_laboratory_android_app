package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.rfdetke.digitriadlaboratory.entities.WifiRecord;

@Dao
public interface WifiRecordDao {

    @Insert
    long insert(WifiRecord wifiRecord);

    @Delete
    void delete(WifiRecord wifiRecord);
}
