package com.contactar.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.database.daos.CellRecordDao;
import com.contactar.digitriadlaboratory.database.daos.SensorRecordDao;
import com.contactar.digitriadlaboratory.database.daos.WindowDao;
import com.contactar.digitriadlaboratory.database.daos.WindowDao.SensorSampleRecord;
import com.contactar.digitriadlaboratory.database.daos.WindowDao.CellSampleRecord;
import com.contactar.digitriadlaboratory.database.entities.CellRecord;
import com.contactar.digitriadlaboratory.database.entities.SensorRecord;

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
