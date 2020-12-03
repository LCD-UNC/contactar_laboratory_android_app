package com.contactar.contactarlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.daos.SensorRecordDao;
import com.contactar.contactarlaboratory.database.daos.WindowDao;
import com.contactar.contactarlaboratory.database.daos.WindowDao.SensorSampleRecord;
import com.contactar.contactarlaboratory.database.entities.SensorRecord;

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
