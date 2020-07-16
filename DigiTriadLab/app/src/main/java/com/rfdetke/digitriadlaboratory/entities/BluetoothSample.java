package com.rfdetke.digitriadlaboratory.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "bluetooth_sample",
        foreignKeys = @ForeignKey(entity = Run.class,
                parentColumns = "id",
                childColumns = "run_id",
                onDelete = ForeignKey.CASCADE))
public class BluetoothSample {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public Date timestamp;

    @ColumnInfo(name = "run_id")
    public int runId;
}
