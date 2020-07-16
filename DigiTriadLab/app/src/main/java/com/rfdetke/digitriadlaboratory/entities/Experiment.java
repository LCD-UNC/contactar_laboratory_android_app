package com.rfdetke.digitriadlaboratory.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "experiment",
        foreignKeys = @ForeignKey(entity = Device.class,
                parentColumns = "id",
                childColumns = "device_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = {"codename"}, unique = true))
public class Experiment {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String codename;
    public String description;

    @ColumnInfo(name = "device_id")
    public int deviceId;
}
