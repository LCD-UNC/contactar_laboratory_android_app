package com.rfdetke.digitriadlaboratory.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "sensor_record",
        indices = {@Index(value = "id", unique = true),
                   @Index(value = "window_id")},
        foreignKeys = @ForeignKey(entity = Window.class,
                parentColumns = "id",
                childColumns = "window_id",
                onDelete = ForeignKey.CASCADE))
public class SensorRecord {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "sensor_type")
    public String sensorType;
    @ColumnInfo(name = "value_id")
    public int valueId;
    public double value;

    @ColumnInfo(name = "window_id")
    public long windowId;

    public SensorRecord(String sensorType, int valueId, double value) {
        this.sensorType = sensorType;
        this.valueId = valueId;
        this.value = value;
    }
}
