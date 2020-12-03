package com.contactar.contactarlaboratory.database.entities;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "bluetooth_record",
        indices = {@Index(value = "id", unique = true),
                @Index(value = "window_id")},
        foreignKeys = @ForeignKey(entity = Window.class,
                parentColumns = "id",
                childColumns = "window_id",
                onDelete = ForeignKey.CASCADE))
public class BluetoothRecord {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public Date timestamp;
    public String address;
    @ColumnInfo(name = "bluetooth_major_class")
    public String bluetoothMajorClass;
    @ColumnInfo(name = "bond_state")
    public int bondState;
    public String type;

    @ColumnInfo(name = "window_id")
    public long windowId;

    public BluetoothRecord(BluetoothDevice device, long windowId) {
        this.address = device.getAddress();
        this.bluetoothMajorClass = parseBluetoothMajorClass(device.getBluetoothClass().getMajorDeviceClass());

        if(device.getBondState()==BluetoothDevice.BOND_BONDED)
            this.bondState = 1;
        else
            this.bondState = 0;

        this.type = parseType(device.getType());
        this.windowId = windowId;
        this.timestamp = new Date();
    }

    public BluetoothRecord(String address, String bluetoothMajorClass,
                           int bondState, String type, long windowId) {
        this.address = address;
        this.bluetoothMajorClass = bluetoothMajorClass;
        this.bondState = bondState;
        this.type = type;
        this.windowId = windowId;
    }


    public static String parseType(int type) {
        switch (type) {
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                return "CLASSIC";
            case BluetoothDevice.DEVICE_TYPE_LE:
                return "LOW ENERGY";
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                return "DUAL";
            default:
                return "UNKNOWN";
        }
    }

    public static String parseBluetoothMajorClass(int majorDeviceClass) {
        switch (majorDeviceClass) {
            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                return "AUDIO VIDEO";
            case BluetoothClass.Device.Major.COMPUTER:
                return "COMPUTER";
            case BluetoothClass.Device.Major.HEALTH:
                return "HEALTH";
            case BluetoothClass.Device.Major.IMAGING:
                return "IMAGING";
            case BluetoothClass.Device.Major.MISC:
                return "MISC";
            case BluetoothClass.Device.Major.NETWORKING:
                return "NETWORKING";
            case BluetoothClass.Device.Major.PERIPHERAL:
                return "PERIPHERAL";
            case BluetoothClass.Device.Major.PHONE:
                return "PHONE";
            case BluetoothClass.Device.Major.TOY:
                return "TOY";
            case BluetoothClass.Device.Major.WEARABLE:
                return "WEARABLE";
            default:
                return "UNCATEGORIZED";

        }
    }

}
