package com.rfdetke.digitriadlaboratory.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "advertise_configuration",
        indices = {@Index(value = "id", unique = true),
                @Index(value = "experiment_id")},
        foreignKeys = @ForeignKey(entity = Experiment.class,
                parentColumns = "id",
                childColumns = "experiment_id",
                onDelete = ForeignKey.CASCADE))
public class AdvertiseConfiguration {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "tx_power")
    public int txPower;
    public int interval;

    @ColumnInfo(name = "experiment_id")
    public long experimentId;

    public AdvertiseConfiguration(int txPower, int interval, long experimentId) {
        this.txPower = txPower;
        this.interval = interval;
        this.experimentId = experimentId;
    }
}
