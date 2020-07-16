package com.rfdetke.digitriadlaboratory.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "sensor_record",
        foreignKeys = @ForeignKey(entity = SensorsSample.class,
                parentColumns = "id",
                childColumns = "sensors_sample_id",
                onDelete = ForeignKey.CASCADE))
public class SensorRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String sensor_type;
    @ColumnInfo(name = "value_id")
    public int valueId;
    public double value;

    @ColumnInfo(name = "sensors_sample_id")
    public int sensorsSampleId;
}
