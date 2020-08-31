package com.rfdetke.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.CellRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao.SensorSampleRecord;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao.CellSampleRecord;
import com.rfdetke.digitriadlaboratory.database.entities.CellRecord;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;

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

    /*public LiveData<Long> getLiveCount(long runId) {
        return windowDao.getSensorLiveCount(runId);
    }*/

    public long[] insert(List<CellRecord> cellRecords) {
        return cellRecordDao.insert(cellRecords);
    }
}
