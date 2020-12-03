package com.contactar.contactarlaboratory.scanners.cell;

import android.content.Context;

import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.entities.CellRecord;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.repositories.CellRepository;
import com.contactar.contactarlaboratory.scanners.Scheduler;

import java.util.ArrayList;
import java.util.List;

public class CellScanScheduler extends Scheduler {

    private final CellRepository cellRepository;
    CellDataBucket cellDataBucket;

    public CellScanScheduler(long runId, long randomTime, WindowConfiguration windowConfiguration, Context context,
                             AppDatabase database) {
        super(runId, randomTime, windowConfiguration, context, database);
        this.cellRepository = new CellRepository(database);
        this.key = SourceTypeEnum.CELL.toString();
    }

    @Override
    protected void startTask() {
        long sampleId = windowRepository.insert(runId, this.windowCount, key);
        cellDataBucket = new CellDataBucket(sampleId,context);
        List<CellRecord> cellRecords = new ArrayList<>();
        for(Object record : cellDataBucket.getRecordsList()) {
            cellRecords.add((CellRecord) record);
        }
        cellRepository.insert(cellRecords);
    }

    @Override
    protected void endTask() {

    }

}