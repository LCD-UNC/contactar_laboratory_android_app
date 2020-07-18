package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.WifiSample;

import java.util.Date;
import java.util.List;

@Dao
public interface WifiSampleDao {

    @Query("SELECT timestamp, address, channel_width, center_frequency_0," +
                  "center_frequency_1, frequency, level, passpoint  " +
            "FROM wifi_sample " +
            "INNER JOIN wifi_record " +
            "ON wifi_sample.id=wifi_record.wifi_sample_id " +
            "WHERE run_id==(:runId)")
    List<SampleRecord> getAllSamplesRecordsByRunId(int runId);

    @Insert
    void insert(WifiSample wifiSample);

    @Delete
    void delete(WifiSample wifiSample);

    static class SampleRecord {
        public Date timestamp;
        public String address;
        @ColumnInfo(name = "channel_width")
        public String channelWidth;
        @ColumnInfo(name = "center_frequency_0")
        public int centerFrequency0;
        @ColumnInfo(name = "center_frequency_1")
        public int centerFrequency1;
        public int frequency;
        public int level;
        public int passpoint;
    }
}
