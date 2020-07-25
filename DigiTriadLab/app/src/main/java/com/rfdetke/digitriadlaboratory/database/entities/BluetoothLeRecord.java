package com.rfdetke.digitriadlaboratory.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "bluetooth_le_record",
        indices = {@Index(value = "id", unique = true),
                   @Index(value = "sample_id")},
        foreignKeys = @ForeignKey(entity = Sample.class,
                parentColumns = "id",
                childColumns = "sample_id",
                onDelete = ForeignKey.CASCADE))
public class BluetoothLeRecord {
    @PrimaryKey(autoGenerate = true)
    public long id;

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

    @ColumnInfo(name = "sample_id")
    public long sampleId;

}
