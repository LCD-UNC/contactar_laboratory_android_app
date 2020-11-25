package com.contactar.digitriadlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.contactar.digitriadlaboratory.database.entities.CellRecord;
import com.contactar.digitriadlaboratory.database.entities.SensorRecord;

import java.util.List;

@Dao
public interface CellRecordDao {

    @Insert
    long[] insert(List<CellRecord> cellRecord);

    @Insert
    long insert(CellRecord cellRecord);

    @Delete
    void delete(CellRecord cellRecord);
}
