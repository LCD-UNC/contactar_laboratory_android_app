package com.rfdetke.digitriadlaboratory.entities;

import android.net.wifi.ScanResult;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "wifi_record",
        indices = {@Index(value = "id", unique = true),
                   @Index(value = "sample_id")},
        foreignKeys = @ForeignKey(entity = Sample.class,
                parentColumns = "id",
                childColumns = "sample_id",
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

    @ColumnInfo(name = "sample_id")
    public long sampleId;

    public WifiRecord(ScanResult result, long sampleId) {
        this.address = result.BSSID;
        this.channelWidth = parseChannelWidth(result.channelWidth);
        this.centerFrequency0 = result.centerFreq0;
        this.centerFrequency1 = result.centerFreq1;
        this.frequency = result.frequency;
        this.level = result.level;

        if(result.isPasspointNetwork())
            this.passpoint = 1;
        else
            this.passpoint = 0;

        this.sampleId = sampleId;
    }

    public WifiRecord(String address, String channelWidth, int centerFrequency0,
                      int centerFrequency1, int frequency, int level, int passpoint, long sampleId) {
        this.address = address;
        this.channelWidth = channelWidth;
        this.centerFrequency0 = centerFrequency0;
        this.centerFrequency1 = centerFrequency1;
        this.frequency = frequency;
        this.level = level;
        this.passpoint = passpoint;
        this.sampleId = sampleId;
    }

    private String parseChannelWidth (int channelWidth) {
        switch (channelWidth) {
            case ScanResult.CHANNEL_WIDTH_20MHZ:
                return "20MHz";
            case ScanResult.CHANNEL_WIDTH_40MHZ:
                return "40MHz";
            case ScanResult.CHANNEL_WIDTH_80MHZ:
                return "80MHz";
            case ScanResult.CHANNEL_WIDTH_160MHZ:
                return "160MHz";
            case ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ:
                return "80MHz + 80MHz";
            default:
                throw new IllegalStateException("Unexpected value: " + channelWidth);
        }
    }
}
