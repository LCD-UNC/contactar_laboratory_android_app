package com.contactar.contactarlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.daos.BatteryRecordDao;
import com.contactar.contactarlaboratory.database.daos.WindowDao;
import com.contactar.contactarlaboratory.database.daos.WindowDao.BatterySampleRecord;
import com.contactar.contactarlaboratory.database.entities.BatteryRecord;

import java.util.List;

public class BatteryRepository {
    private WindowDao windowDao;
    private BatteryRecordDao batteryRecordDao;

    public BatteryRepository(AppDatabase database) {
        batteryRecordDao = database.getBatteryRecordDao();
        windowDao = database.getWindowDao();
    }

    public List<BatterySampleRecord> getAllSamples(long[] runs) {
        return windowDao.getBatterySamplesRecords(runs);
    }

    public LiveData<Long> getLiveCount(long runId) {
        return windowDao.getBatteryLiveCount(runId);
    }

    public long[] insert(List<BatteryRecord> batteryRecords) {
        return batteryRecordDao.insert(batteryRecords);
    }
}
