package com.contactar.contactarlaboratory.database.entities;
import android.location.GnssMeasurement;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "gps_record",
        indices = {@Index(value = "id", unique = true),
                @Index(value = "window_id")},
        foreignKeys = @ForeignKey(entity = Window.class,
                parentColumns = "id",
                childColumns = "window_id",
                onDelete = ForeignKey.CASCADE))
public class GpsRecord {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "snr")
    public double snr;

    @ColumnInfo(name = "satellite_id")
    public int satId;

    public float freq;
    public int constType;

    @ColumnInfo(name = "window_id")
    public long windowId;

    public GpsRecord(GnssMeasurement measurement, long windowId) {
        if(measurement.hasSnrInDb())
            this.snr = measurement.getSnrInDb();
        else
            this.snr = Double.MIN_VALUE;
        this.satId = measurement.getSvid();
        this.freq = measurement.getCarrierFrequencyHz();
        this.constType = measurement.getConstellationType();
        this.windowId = windowId;
    }


    public GpsRecord(int snr, int satId, float freq, int constType, long windowId) {
        this.snr = snr;
        this.satId = satId;
        this.freq = freq;
        this.constType = constType;
        this.windowId = windowId;
    }

}
