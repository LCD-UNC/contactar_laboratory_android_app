package com.contactar.contactarlaboratory.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "run",
        indices = {@Index(value = "id", unique = true),
                   @Index(value = "experiment_id")},
        foreignKeys = @ForeignKey(entity = Experiment.class,
                parentColumns = "id",
                childColumns = "experiment_id",
                onDelete = ForeignKey.CASCADE))

public class Run {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public Date start;
    public long number;
    public String state;

    @ColumnInfo(name = "experiment_id")
    public long experimentId;

    public Run(Date start, long number, String state, long experimentId) {
        this.start = start;
        this.number = number;
        this.state = state;
        this.experimentId = experimentId;
    }
}