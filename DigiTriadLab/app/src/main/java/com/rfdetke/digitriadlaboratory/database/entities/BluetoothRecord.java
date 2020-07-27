package com.rfdetke.digitriadlaboratory.database.entities;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.lang.reflect.Field;

@Entity(tableName = "bluetooth_record",
        indices = {@Index(value = "id", unique = true),
                @Index(value = "sample_id")},
        foreignKeys = @ForeignKey(entity = Sample.class,
                parentColumns = "id",
                childColumns = "sample_id",
                onDelete = ForeignKey.CASCADE))
public class BluetoothRecord {
    @PrimaryKey(autoGenerate = true)
    public long id;


    public String address;
    @ColumnInfo(name = "bluetooth_major_class")
    public String bluetoothMajorClass;
    @ColumnInfo(name = "bond_state")
    public int bondState;
    public String type;

    @ColumnInfo(name = "sample_id")
    public long sampleId;

    public BluetoothRecord(BluetoothDevice device, long sampleId) {
        this.address = device.getAddress();
        this.bluetoothMajorClass = parseBluetoothMajorClass(device.getBluetoothClass().getMajorDeviceClass());

        if(device.getBondState()==BluetoothDevice.BOND_BONDED)
            this.bondState = 1;
        else
            this.bondState = 0;

        this.type = parseType(device.getType());
        this.sampleId = sampleId;
    }

    public BluetoothRecord(String address, String bluetoothMajorClass,
                           int bondState, String type, long sampleId) {
        this.address = address;
        this.bluetoothMajorClass = bluetoothMajorClass;
        this.bondState = bondState;
        this.type = type;
        this.sampleId = sampleId;
    }


    private String parseType(int type) {
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

    private String parseBluetoothMajorClass(int majorDeviceClass) {
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
