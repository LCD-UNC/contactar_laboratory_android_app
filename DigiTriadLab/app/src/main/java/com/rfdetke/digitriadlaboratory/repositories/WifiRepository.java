package com.rfdetke.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao.WifiSampleRecord;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.WifiRecordDao;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.database.entities.WifiRecord;

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

    public List<WifiSampleRecord> getAllSamplesFor(long[] runs) {
        return windowDao.getWifiSamplesRecords(runs);
    }

    public LiveData<Long> getLiveCount(long runId) {
        return windowDao.getWifiLiveCount(runId);
    }
}
