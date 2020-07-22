package com.rfdetke.digitriadlaboratory.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "wifi_sample",
        indices = {@Index(value = "id", unique = true),
                   @Index(value = "run_id")},
        foreignKeys = @ForeignKey(entity = Run.class,
                parentColumns = "id",
                childColumns = "run_id",
                onDelete = ForeignKey.CASCADE))
public class WifiSample {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public Date timestamp;

    @ColumnInfo(name = "run_id")
    public int runId;

    public WifiSample(Date timestamp, int runId) {
        this.timestamp = timestamp;
        this.runId = runId;
    }
}
