package com.contactar.contactarlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.daos.CellRecordDao;
import com.contactar.contactarlaboratory.database.daos.WindowDao;
import com.contactar.contactarlaboratory.database.daos.WindowDao.CellSampleRecord;
import com.contactar.contactarlaboratory.database.entities.CellRecord;

import java.util.List;

public class CellRepository {
    private WindowDao windowDao;
    private CellRecordDao cellRecordDao;

    public CellRepository(AppDatabase database) {
        cellRecordDao = database.getCellRecordDao();
        windowDao = database.getWindowDao();
    }

    public List<CellSampleRecord> getAllSamples(long[] runs) {
        return windowDao.getCellSamplesRecords(runs);
    }

    public LiveData<Long> getLiveCount(long runId) {
        return windowDao.getCellLiveCount(runId);
    }

    public long[] insert(List<CellRecord> cellRecords) {
        return cellRecordDao.insert(cellRecords);
    }
}
