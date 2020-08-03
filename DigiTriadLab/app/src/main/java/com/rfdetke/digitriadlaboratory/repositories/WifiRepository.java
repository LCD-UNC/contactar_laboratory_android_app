package com.rfdetke.digitriadlaboratory.repositories;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao.WifiSampleRecord;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.WifiRecordDao;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.database.entities.WifiRecord;

import java.util.List;

public class WifiRepository {
    private SampleDao sampleDao;
    private WifiRecordDao wifiRecordDao;
    private SensorRecordDao sensorRecordDao;

    public WifiRepository(AppDatabase database) {
        sampleDao = database.getSampleDao();
        wifiRecordDao = database.getWifiRecordDao();
        sensorRecordDao = database.getSensorRecordDao();
    }

    public long[] insertWifi(List<WifiRecord> wifiRecord) {
        return wifiRecordDao.insert(wifiRecord);
    }

    public long[] insertSensors(List<SensorRecord> sensorRecord) {
        return sensorRecordDao.insert(sensorRecord);
    }

    public List<WifiSampleRecord> getAllSamplesFor(long[] runs) {
        return sampleDao.getWifiSamplesRecords(runs);
    }
}
