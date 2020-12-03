package com.contactar.contactarlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.daos.WindowDao;
import com.contactar.contactarlaboratory.database.daos.WindowDao.WifiSampleRecord;
import com.contactar.contactarlaboratory.database.daos.WifiRecordDao;
import com.contactar.contactarlaboratory.database.entities.WifiRecord;

import java.util.List;

public class WifiRepository {
    private WindowDao windowDao;
    private WifiRecordDao wifiRecordDao;

    public WifiRepository(AppDatabase database) {
        windowDao = database.getWindowDao();
        wifiRecordDao = database.getWifiRecordDao();
    }

    public long[] insertWifi(List<WifiRecord> wifiRecord) {
        return wifiRecordDao.insert(wifiRecord);
    }

    public List<WifiSampleRecord> getAllSamples(long[] runs) {
        return windowDao.getWifiSamplesRecords(runs);
    }

    public LiveData<Long> getLiveCount(long runId) {
        return windowDao.getWifiLiveCount(runId);
    }
}
