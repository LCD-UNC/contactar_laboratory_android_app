package com.contactar.contactarlaboratory.database.entities;

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
        indices = {@Index(value = {"id","codename", }, unique = true), @Index(value = "device_id")})
public class Experiment {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String codename;
    public String description;
    public long maxRandomTime;

    @ColumnInfo(name = "device_id")
    public long deviceId;

    public Experiment(String codename, String description, long deviceId, long maxRandomTime) {
        this.codename = codename;
        this.description = description;
        this.deviceId = deviceId;
        this.maxRandomTime = maxRandomTime;
    }
}
