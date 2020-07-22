package com.rfdetke.digitriadlaboratory.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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
    @ColumnInfo(name = "bluetooth_class")
    public String bluetoothClass;
    @ColumnInfo(name = "bluetooth_major_class")
    public String bluetoothMajorClass;
    @ColumnInfo(name = "bond_state")
    public int bondState;
    public String type;

    @ColumnInfo(name = "sample_id")
    public long sampleId;
}
