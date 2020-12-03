package com.contactar.contactarlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.daos.ActivityRecordDao;
import com.contactar.contactarlaboratory.database.daos.WindowDao;
import com.contactar.contactarlaboratory.database.daos.WindowDao.ActivitySampleRecord;
import com.contactar.contactarlaboratory.database.entities.ActivityRecord;

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
