package com.rfdetke.digitriadlaboratory.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "bluetooth_record",
        foreignKeys = @ForeignKey(entity = BluetoothSample.class,
                parentColumns = "id",
                childColumns = "bluetooth_sample_id",
                onDelete = ForeignKey.CASCADE))
public class BluetoothRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String address;
    @ColumnInfo(name = "bluetooth_class")
    public String bluetoothClass;
    @ColumnInfo(name = "bluetooth_major_class")
    public String bluetoothMajorClass;
    @ColumnInfo(name = "bond_state")
    public int bondState;
    public String type;

    @ColumnInfo(name = "bluetooth_sample_id")
    public int bluetoothSampleId;
}
