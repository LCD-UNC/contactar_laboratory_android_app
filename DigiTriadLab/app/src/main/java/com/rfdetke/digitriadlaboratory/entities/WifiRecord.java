package com.rfdetke.digitriadlaboratory.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "wifi_record",
        foreignKeys = @ForeignKey(entity = BluetoothSample.class,
                parentColumns = "id",
                childColumns = "wifi_sample_id",
                onDelete = ForeignKey.CASCADE))
public class WifiRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

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

    @ColumnInfo(name = "wifi_sample_id")
    public int wifiSampleId;
}
