package com.rfdetke.digitriadlaboratory.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "bluetooth_le_scan_configuration",
        foreignKeys = @ForeignKey(entity = Experiment.class,
                parentColumns = "id",
                childColumns = "experiment_id",
                onDelete = ForeignKey.CASCADE))
public class BluetoothLeScanConfiguration {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "active_time")
    public int activeTime;
    @ColumnInfo(name = "inactive_time")
    public int inactiveTime;
    @ColumnInfo(name = "windows")
    public int windows;

    @ColumnInfo(name = "experiment_id")
    public int experimentId;

    public BluetoothLeScanConfiguration(int scanPeriod, int scanInterval, int experimentId) {
        this.scanPeriod = scanPeriod;
        this.scanInterval = scanInterval;
        this.experimentId = experimentId;
    }
}
