package com.rfdetke.digitriadlaboratory.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "run",
        foreignKeys = @ForeignKey(entity = Experiment.class,
                parentColumns = "id",
                childColumns = "experiment_id",
                onDelete = ForeignKey.CASCADE))

public class Run {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public Date start;
    public Date end;
    public int number;

    @ColumnInfo(name = "experiment_id")
    public int experimentId;
}