package com.rfdetke.digitriadlaboratory.repositories;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao;
import com.rfdetke.digitriadlaboratory.database.daos.SourceTypeDao;
import com.rfdetke.digitriadlaboratory.database.entities.Sample;

import java.util.Date;

public class SampleRepository {
    private SampleDao sampleDao;
    private SourceTypeDao sourceTypeDao;

    public SampleRepository(AppDatabase database) {
        sampleDao = database.getSampleDao();
        sourceTypeDao = database.getSourceTypeDao();
    }

    public long insert(long runId, String type) {
        return sampleDao.insert(new Sample(new Date(), runId, sourceTypeDao.getSourceTypeByType(type).id));
    }
}
