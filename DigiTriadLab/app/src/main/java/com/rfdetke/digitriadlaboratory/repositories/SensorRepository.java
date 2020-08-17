package com.rfdetke.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao.SensorSampleRecord;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;

import java.util.List;

public class SensorRepository {
    private WindowDao windowDao;
    private SensorRecordDao sensorRecordDao;

    public SensorRepository(AppDatabase database) {
        sensorRecordDao = database.getSensorRecordDao();
        windowDao = database.getWindowDao();
    }

    public List<SensorSampleRecord> getAllSamples(long[] runs) {
        return windowDao.getSensorSamplesRecords(runs);
    }

    public LiveData<Long> getLiveCount(long runId) {
        return windowDao.getSensorLiveCount(runId);
    }

    public long[] insert(List<SensorRecord> sensorRecords) {
        return sensorRecordDao.insert(sensorRecords);
    }
}
