package com.rfdetke.digitriadlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.rfdetke.digitriadlaboratory.database.entities.GpsRecord;

import java.util.List;

@Dao
public interface GpsRecordDao {

    @Insert
    long[] insert(List<GpsRecord> gpsRecord);

    @Insert
    long insert(GpsRecord gpsRecord);

    @Delete
    void delete(GpsRecord gpsRecord);
}
