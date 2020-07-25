package com.rfdetke.digitriadlaboratory.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "sample",
        indices = {@Index(value = "id", unique = true),
                   @Index(value = "run_id"),
                   @Index(value = "source_type")},
        foreignKeys = {@ForeignKey(entity = Run.class,
                            parentColumns = "id",
                            childColumns = "run_id",
                            onDelete = ForeignKey.CASCADE),
                       @ForeignKey(entity = SourceType.class,
                            parentColumns = "id",
                            childColumns = "source_type",
                            onDelete = ForeignKey.CASCADE)})
public class Sample {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public Date timestamp;

    @ColumnInfo(name = "run_id")
    public long runId;
    @ColumnInfo(name = "source_type")
    public long sourceType;

    public Sample(Date timestamp, long runId, long sourceType) {
        this.timestamp = timestamp;
        this.runId = runId;
        this.sourceType = sourceType;
    }
}
