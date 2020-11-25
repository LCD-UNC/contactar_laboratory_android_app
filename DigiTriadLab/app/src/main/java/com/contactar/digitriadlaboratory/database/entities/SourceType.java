package com.contactar.digitriadlaboratory.database.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "source_type",
        indices = {@Index(value = "id", unique = true),
                   @Index(value = "type", unique = true)})
public class SourceType {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String type;

    public SourceType(String type) {
        this.type = type;
    }
}
