package com.contactar.contactarlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.contactar.contactarlaboratory.database.entities.SensorRecord;

import java.util.List;

@Dao
public interface SensorRecordDao {

    @Insert
    long[] insert(List<SensorRecord> sensorRecord);

    @Insert
    long insert(SensorRecord sensorRecord);

    @Delete
    void delete(SensorRecord sensorRecord);
}
