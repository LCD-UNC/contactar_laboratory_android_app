package com.rfdetke.digitriadlaboratory.scanners.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.ActivityRecord;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.ActivityRepository;
import com.rfdetke.digitriadlaboratory.scanners.Scheduler;

import java.util.ArrayList;
import java.util.List;

public class ActivityScanScheduler extends Scheduler {

    private final ActivityRepository activityRepository;
    ActivityDataBucket activityDataBucket;

    private static final int PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 45;

    private boolean runningQOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

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
        enableActivityRecognition();
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

    public void enableActivityRecognition() {

        if (activityRecognitionPermissionApproved()) {
                activityDataBucket.enableActivityTransitions(context);

        } else {
            // Request permission and start activity for result. If the permission is approved, we
            // want to make sure we start activity recognition tracking.
            Log.d("reg", "registrando permisos");
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    PERMISSION_REQUEST_ACTIVITY_RECOGNITION);

        }

    }



    private boolean activityRecognitionPermissionApproved() {

        if (runningQOrLater) {

            return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACTIVITY_RECOGNITION
            );
        } else {
            return true;
        }
    }
}
