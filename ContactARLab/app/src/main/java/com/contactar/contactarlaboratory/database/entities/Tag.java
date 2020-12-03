package com.contactar.contactarlaboratory.database.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tag",
        indices = {@Index(value = "id", unique = true),
                  @Index(value = "tag", unique = true)})
public class Tag {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String tag;

    public Tag(String tag) {
        this.tag = tag;
    }
}
