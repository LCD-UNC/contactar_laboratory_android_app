package com.contactar.contactarlaboratory.scanners.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.Task;
import com.contactar.contactarlaboratory.BuildConfig;
import com.contactar.contactarlaboratory.database.entities.ActivityRecord;
import com.contactar.contactarlaboratory.scanners.DataBucket;

import java.util.ArrayList;
import java.util.List;

public class ActivityDataBucket extends BroadcastReceiver implements DataBucket {

    private final long windowId;
    private List<Object> records;
    private List<ActivityTransition> activityTransitionList;
    private PendingIntent mActivityTransitionsPendingIntent;

    private final String TRANSITIONS_RECEIVER_ACTION =
            BuildConfig.APPLICATION_ID + "TRANSITIONS_RECEIVER_ACTION";

    public ActivityDataBucket(long WindowId, Context context) {
        this.windowId = WindowId;
        records = new ArrayList<>();

        activityTransitionList = new ArrayList<>();

        addTransition(DetectedActivity.IN_VEHICLE,ActivityTransition.ACTIVITY_TRANSITION_ENTER);
        addTransition(DetectedActivity.IN_VEHICLE,ActivityTransition.ACTIVITY_TRANSITION_EXIT);
        addTransition(DetectedActivity.ON_BICYCLE,ActivityTransition.ACTIVITY_TRANSITION_ENTER);
        addTransition(DetectedActivity.ON_BICYCLE,ActivityTransition.ACTIVITY_TRANSITION_EXIT);
        addTransition(DetectedActivity.ON_FOOT,ActivityTransition.ACTIVITY_TRANSITION_ENTER);
        addTransition(DetectedActivity.ON_FOOT,ActivityTransition.ACTIVITY_TRANSITION_EXIT);
        addTransition(DetectedActivity.RUNNING,ActivityTransition.ACTIVITY_TRANSITION_ENTER);
        addTransition(DetectedActivity.RUNNING,ActivityTransition.ACTIVITY_TRANSITION_EXIT);
        addTransition(DetectedActivity.STILL,ActivityTransition.ACTIVITY_TRANSITION_ENTER);
        addTransition(DetectedActivity.STILL,ActivityTransition.ACTIVITY_TRANSITION_EXIT);
        addTransition(DetectedActivity.WALKING,ActivityTransition.ACTIVITY_TRANSITION_ENTER);
        addTransition(DetectedActivity.WALKING,ActivityTransition.ACTIVITY_TRANSITION_EXIT);

        Intent intent = new Intent(TRANSITIONS_RECEIVER_ACTION);
        mActivityTransitionsPendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, 0);
        context.registerReceiver(this, new IntentFilter(TRANSITIONS_RECEIVER_ACTION));

    }

    private void addTransition(int activityType, int activityTransition) {
        activityTransitionList.add(
                new ActivityTransition.Builder()
                        .setActivityType(activityType)
                        .setActivityTransition(activityTransition)
                        .build());
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("reg", "onReceive(): " + intent);

        if (!TextUtils.equals(TRANSITIONS_RECEIVER_ACTION, intent.getAction())) {
            Log.e("activity", "Received an unsupported action in TransitionsReceiver: action = " +
                    intent.getAction());
            return;
        }

        if (ActivityTransitionResult.hasResult(intent)) {

            ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);

            for (ActivityTransitionEvent event : result.getTransitionEvents()) {
                records.add(new ActivityRecord(event.getActivityType(), event.getTransitionType(), windowId));
            }
        }
    }
    @Override
    public List<Object> getRecordsList() {
        return records;
    }

    public void enableActivityTransitions(Context context) {

        Log.d("reg", "enableActivityTransitions()");

        ActivityTransitionRequest request = new ActivityTransitionRequest(activityTransitionList);
        Task<Void> task =
                ActivityRecognition.getClient(context)
                        .requestActivityTransitionUpdates(request, mActivityTransitionsPendingIntent);


        task.addOnSuccessListener(
                result -> Log.d("reg", "Transitions Api was successfully registered."));
        task.addOnFailureListener(
                e -> Log.d("reg", "Transitions Api could NOT be registered: " + e));

    }
}
