package com.contactar.digitriadlaboratory.database.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity_record",
        indices = {@Index(value = "id", unique = true),
                @Index(value = "window_id")},
        foreignKeys = @ForeignKey(entity = Window.class,
                parentColumns = "id",
                childColumns = "window_id",
                onDelete = ForeignKey.CASCADE))
public class ActivityRecord {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public int activity;
    public int transition;


    @ColumnInfo(name = "window_id")
    public long windowId;


    public ActivityRecord(int activity, int transition, long windowId) {
        this.activity = activity;
        this.transition = transition;
        this.windowId = windowId;
    }



}
