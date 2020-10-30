package com.rfdetke.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.BatteryRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao.BatterySampleRecord;
import com.rfdetke.digitriadlaboratory.database.entities.BatteryRecord;

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
