package com.rfdetke.digitriadlaboratory.repositories;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao.SensorSampleRecord;

import java.util.List;

public class SensorRepository {
    private SampleDao sampleDao;

    public SensorRepository(AppDatabase database) {
        sampleDao = database.getSampleDao();
    }

    public List<SensorSampleRecord> getAllSamplesFor(long[] runs) {
        return sampleDao.getSensorSamplesRecords(runs);
    }
}
