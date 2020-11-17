package com.rfdetke.digitriadlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.rfdetke.digitriadlaboratory.database.entities.ActivityRecord;

import java.util.List;

@Dao
public interface ActivityRecordDao {

    @Insert
    long[] insert(List<ActivityRecord> activityRecord);

    @Insert
    long insert(ActivityRecord activityRecord);

    @Delete
    void delete(ActivityRecord activityRecord);
}
