package com.contactar.digitriadlaboratory.scanners.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.database.entities.ActivityRecord;
import com.contactar.digitriadlaboratory.database.entities.WindowConfiguration;
import com.contactar.digitriadlaboratory.repositories.ActivityRepository;
import com.contactar.digitriadlaboratory.scanners.Scheduler;

import java.util.ArrayList;
import java.util.List;

public class ActivityScanScheduler extends Scheduler {

    private final ActivityRepository activityRepository;
    ActivityDataBucket activityDataBucket;

    public ActivityScanScheduler(long runId, long randomTime, WindowConfiguration windowConfiguration, Context context,
                                 AppDatabase database) {

        super(runId, randomTime, windowConfiguration, context, database);
        this.activityRepository = new ActivityRepository(database);
        this.key = SourceTypeEnum.ACTIVITY.toString();
    }

    @Override
    protected void startTask() {
        long sampleId = windowRepository.insert(runId, this.windowCount, key);

        activityDataBucket = new ActivityDataBucket(sampleId, context);
        activityDataBucket.enableActivityTransitions(context);
    };

    @Override
    protected void endTask() {
        List<ActivityRecord> activityRecords = new ArrayList<>();
        for (Object record : activityDataBucket.getRecordsList()) {
            activityRecords.add((ActivityRecord) record);
        }
        activityRepository.insert(activityRecords);
        context.unregisterReceiver(activityDataBucket);
        activityDataBucket = null;
    }

    @Override
    public void stop() {
        super.stop();
        if(activityDataBucket !=null) {
            context.unregisterReceiver(activityDataBucket);
            activityDataBucket = null;
        }
    }

}
