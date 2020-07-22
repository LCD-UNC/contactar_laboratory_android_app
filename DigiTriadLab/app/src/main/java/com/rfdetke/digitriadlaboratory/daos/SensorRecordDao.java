package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.rfdetke.digitriadlaboratory.entities.SensorRecord;

@Dao
public interface SensorRecordDao {

    @Insert
    long insert(SensorRecord sensorRecord);

    @Delete
    void delete(SensorRecord sensorRecord);
}
