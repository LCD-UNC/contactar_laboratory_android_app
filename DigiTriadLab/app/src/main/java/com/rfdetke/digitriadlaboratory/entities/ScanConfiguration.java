package com.rfdetke.digitriadlaboratory.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "scan_configuration",
        indices = {@Index(value = "id", unique = true),
                   @Index(value = "experiment_id"),
                   @Index(value = "source_type")},
        foreignKeys = {@ForeignKey(entity = Experiment.class,
                            parentColumns = "id",
                            childColumns = "experiment_id",
                            onDelete = ForeignKey.CASCADE),
                       @ForeignKey(entity = SourceType.class,
                            parentColumns = "id",
                            childColumns = "source_type",
                            onDelete = ForeignKey.CASCADE)})
public class ScanConfiguration {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "active_time")
    public long activeTime;
    @ColumnInfo(name = "inactive_time")
    public long inactiveTime;
    @ColumnInfo(name = "windows")
    public long windows;
    @ColumnInfo(name = "source_type")
    public long sourceType;


    @ColumnInfo(name = "experiment_id")
    public long experimentId;
}
