package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.SensorsSample;

import java.util.Date;
import java.util.List;

@Dao
public interface SensorsSampleDao {

    @Query("SELECT timestamp, sensor_type, value_id, value " +
            "FROM sensors_sample " +
            "INNER JOIN sensor_record " +
            "ON sensors_sample.id=sensor_record.sensors_sample_id " +
            "WHERE run_id==(:runId)")
    List<SampleRecord> getAllSamplesRecordsByRunId(int runId);

    @Insert
    void insert(SensorsSample sensorsSample);

    @Delete
    void delete(SensorsSample sensorsSample);

    static class SampleRecord {
        public Date timestamp;
        @ColumnInfo(name = "sensor_type")
        public String sensorType;
        @ColumnInfo(name = "value_id")
        public int valueId;
        public double value;
    }
}
