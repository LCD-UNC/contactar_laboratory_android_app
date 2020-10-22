package com.rfdetke.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.GpsRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao.GpsSampleRecord;
import com.rfdetke.digitriadlaboratory.database.entities.GpsRecord;

import java.util.List;

public class GpsRepository {
    private WindowDao windowDao;
    private GpsRecordDao gpsRecordDao;

    public GpsRepository(AppDatabase database) {
        gpsRecordDao = database.getGpsRecordDao();
        windowDao = database.getWindowDao();
    }

    public List<GpsSampleRecord> getAllSamples(long[] runs) {
        return windowDao.getGpsSamplesRecords(runs);
    }

    public LiveData<Long> getLiveCount(long runId) {
        return windowDao.getGpsLiveCount(runId);
    }

    public long[] insert(List<GpsRecord> gpsRecords) {
        return gpsRecordDao.insert(gpsRecords);
    }
}
