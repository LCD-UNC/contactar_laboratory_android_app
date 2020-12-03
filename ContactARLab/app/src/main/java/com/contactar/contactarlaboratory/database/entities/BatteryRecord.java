package com.contactar.contactarlaboratory.database.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "battery_record",
        indices = {@Index(value = "id", unique = true),
                @Index(value = "window_id")},
        foreignKeys = @ForeignKey(entity = Window.class,
                parentColumns = "id",
                childColumns = "window_id",
                onDelete = ForeignKey.CASCADE))
public class BatteryRecord {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "low_battery")
    public boolean lowBattery;

    @ColumnInfo(name = "is_charging")
    public boolean isCharging;

    @ColumnInfo(name = "usb_charging")
    public boolean usbCharge;

    @ColumnInfo(name = "ac_charging")
    public boolean acCharge;

    @ColumnInfo(name = "charge")
    public float charge;

    @ColumnInfo(name = "window_id")
    public long windowId;


    public BatteryRecord(boolean lowBattery, boolean isCharging, boolean usbCharge, boolean acCharge, float charge, long windowId) {
        this.lowBattery = lowBattery;
        this.isCharging = isCharging;
        this.usbCharge = usbCharge;
        this.acCharge = acCharge;
        this.charge = charge;
        this.windowId = windowId;
    }



}
