package com.contactar.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.database.daos.WindowDao;
import com.contactar.digitriadlaboratory.database.daos.WindowDao.WifiSampleRecord;
import com.contactar.digitriadlaboratory.database.daos.SensorRecordDao;
import com.contactar.digitriadlaboratory.database.daos.WifiRecordDao;
import com.contactar.digitriadlaboratory.database.entities.SensorRecord;
import com.contactar.digitriadlaboratory.database.entities.WifiRecord;

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
