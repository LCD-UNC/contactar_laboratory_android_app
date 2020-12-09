package com.contactar.contactarlaboratory.database.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.DetectedActivity;

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

    public String activity;
    public String transition;

    @ColumnInfo(name = "window_id")
    public long windowId;


    public ActivityRecord(int activity, int transition, long windowId) {
        this.activity = toActivityString(activity);
        this.transition = toTransitionType(transition);
        this.windowId = windowId;
    }

    public ActivityRecord(String activity, String transition, long windowId) {
        this.activity = activity;
        this.transition = transition;
        this.windowId = windowId;
    }


    private static String toActivityString(int activity) {
        switch (activity) {
            case DetectedActivity.IN_VEHICLE:
                return "IN_VEHICLE";
            case DetectedActivity.ON_BICYCLE:
                return "ON_BICYCLE";
            case DetectedActivity.ON_FOOT:
                return "ON_FOOT";
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.WALKING:
                return "WALKING";
            case DetectedActivity.RUNNING:
                return "RUNNING";
            default:
                return "UNKNOWN";

        }
    }
    private static String toTransitionType(int transitionType) {
        switch (transitionType) {
            case ActivityTransition.ACTIVITY_TRANSITION_ENTER:
                return "ENTER";
            case ActivityTransition.ACTIVITY_TRANSITION_EXIT:
                return "EXIT";
            default:
                return "UNKNOWN";
        }
    }

}
