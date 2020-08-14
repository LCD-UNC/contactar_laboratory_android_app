package com.rfdetke.digitriadlaboratory.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "experiment_tag",
        primaryKeys = {"experiment_id", "tag_id"},
        indices = {@Index(value = "experiment_id"),
                    @Index(value = "tag_id")},
        foreignKeys = {@ForeignKey(entity = Experiment.class,
                        parentColumns = "id",
                        childColumns = "experiment_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Tag.class,
                        parentColumns = "id",
                        childColumns = "tag_id",
                        onDelete = ForeignKey.CASCADE)})
public class ExperimentTag {
    @ColumnInfo(name = "experiment_id")
    public long experimentId;
    @ColumnInfo(name = "tag_id")
    public long tagId;

    public ExperimentTag(long experimentId, long tagId) {
        this.experimentId = experimentId;
        this.tagId = tagId;
    }
}
