package com.contactar.contactarlaboratory.database.entities;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.os.SystemClock;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "bluetooth_le_record",
        indices = {@Index(value = "id", unique = true),
                @Index(value = "window_id")},
        foreignKeys = @ForeignKey(entity = Window.class,
                parentColumns = "id",
                childColumns = "window_id",
                onDelete = ForeignKey.CASCADE))
public class BluetoothLeRecord {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public Date timestamp;
    public String address;
    public double rssi;
    @ColumnInfo(name = "tx_power")
    public double txPower;
    @ColumnInfo(name = "advertising_set_id")
    public int advertisingSetId;
    @ColumnInfo(name = "primary_physical_layer")
    public String primaryPhysicalLayer;
    @ColumnInfo(name = "seconary_physical_layer")
    public String secondaryPhysicalLayer;
    @ColumnInfo(name = "periodic_advertising_interval")
    public double periodicAdvertisingInterval;
    public int connectable;
    public int legacy;

    @ColumnInfo(name = "window_id")
    public long windowId;

    public BluetoothLeRecord(ScanResult result, long windowId) {
        this.address = result.getDevice().getAddress();
        this.rssi = result.getRssi();
        if(result.getScanRecord() != null) {
            this.txPower = result.getScanRecord().getTxPowerLevel();
        } else {
            this.txPower = Integer.MIN_VALUE;
        }
        this.advertisingSetId = result.getAdvertisingSid();
        this.primaryPhysicalLayer = parsePhysicalLayer(result.getPrimaryPhy());
        this.secondaryPhysicalLayer = parsePhysicalLayer(result.getSecondaryPhy());
        this.periodicAdvertisingInterval = 1.25*result.getPeriodicAdvertisingInterval();

        if(result.isConnectable())
            this.connectable = 1;
        else
            this.connectable = 0;

        if(result.isLegacy())
            this.legacy = 1;
        else
            this.legacy = 0;

        this.windowId = windowId;
        this.timestamp = new Date(System.currentTimeMillis() - SystemClock.elapsedRealtime() + (result.getTimestampNanos() / 1000000));
    }

    public static String parsePhysicalLayer(int layer) {
        switch (layer) {
            case BluetoothDevice.PHY_LE_1M:
                return "LE 1M";
            case BluetoothDevice.PHY_LE_2M:
                return "LE 2M";
            case BluetoothDevice.PHY_LE_CODED:
                return "LE CODED";
            default:
                return "UNUSED";
        }
    }

    public BluetoothLeRecord(String address, double rssi, double txPower, int advertisingSetId,
                             String primaryPhysicalLayer, String secondaryPhysicalLayer,
                             double periodicAdvertisingInterval, int connectable,
                             int legacy, long windowId) {
        this.address = address;
        this.rssi = rssi;
        this.txPower = txPower;
        this.advertisingSetId = advertisingSetId;
        this.primaryPhysicalLayer = primaryPhysicalLayer;
        this.secondaryPhysicalLayer = secondaryPhysicalLayer;
        this.periodicAdvertisingInterval = periodicAdvertisingInterval;
        this.connectable = connectable;
        this.legacy = legacy;
        this.windowId = windowId;
    }


}