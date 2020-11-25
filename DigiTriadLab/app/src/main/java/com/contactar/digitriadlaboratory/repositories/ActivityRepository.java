package com.contactar.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.database.daos.ActivityRecordDao;
import com.contactar.digitriadlaboratory.database.daos.WindowDao;
import com.contactar.digitriadlaboratory.database.daos.WindowDao.ActivitySampleRecord;
import com.contactar.digitriadlaboratory.database.entities.ActivityRecord;

import java.util.List;

public class ActivityRepository {
    private WindowDao windowDao;
    private ActivityRecordDao activityRecordDao;

    public ActivityRepository(AppDatabase database) {
        activityRecordDao = database.getActivityRecordDao();
        windowDao = database.getWindowDao();
    }

    public List<ActivitySampleRecord> getAllSamples(long[] runs) {
        return windowDao.getActivitySamplesRecords(runs);
    }

    public LiveData<Long> getLiveCount(long runId) {
        return windowDao.getActivityLiveCount(runId);
    }

    public long[] insert(List<ActivityRecord> activityRecords) {
        return activityRecordDao.insert(activityRecords);
    }
}
