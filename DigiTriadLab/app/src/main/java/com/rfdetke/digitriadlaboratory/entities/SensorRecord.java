package com.rfdetke.digitriadlaboratory.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "sensor_record",
        indices = {@Index(value = "id", unique = true),
                   @Index(value = "sample_id")},
        foreignKeys = @ForeignKey(entity = Sample.class,
                parentColumns = "id",
                childColumns = "sample_id",
                onDelete = ForeignKey.CASCADE))
public class SensorRecord {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "sensor_type")
    public String sensorType;
    @ColumnInfo(name = "value_id")
    public int valueId;
    public double value;

    @ColumnInfo(name = "sample_id")
    public long sampleId;

}
