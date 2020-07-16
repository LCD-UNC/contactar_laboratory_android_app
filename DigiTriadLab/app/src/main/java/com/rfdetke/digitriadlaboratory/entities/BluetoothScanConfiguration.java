package com.rfdetke.digitriadlaboratory.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "bluetooth_scan_configuration",
        foreignKeys = @ForeignKey(entity = Experiment.class,
                parentColumns = "id",
                childColumns = "experiment_id",
                onDelete = ForeignKey.CASCADE))
public class BluetoothScanConfiguration {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "scan_period")
    public int scanPeriod;
    @ColumnInfo(name = "scan_interval")
    public int scanInterval;

    @ColumnInfo(name = "experiment_id")
    public int experimentId;
}
