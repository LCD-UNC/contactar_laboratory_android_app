package com.contactar.contactarlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.contactar.contactarlaboratory.database.entities.BatteryRecord;

import java.util.List;

@Dao
public interface BatteryRecordDao {

    @Insert
    long[] insert(List<BatteryRecord> batteryRecord);

    @Insert
    long insert(BatteryRecord batteryRecord);

    @Delete
    void delete(BatteryRecord batteryRecord);
}
