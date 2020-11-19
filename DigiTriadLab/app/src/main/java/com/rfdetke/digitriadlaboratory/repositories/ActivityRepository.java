package com.rfdetke.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.ActivityRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao.ActivitySampleRecord;
import com.rfdetke.digitriadlaboratory.database.entities.ActivityRecord;

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
