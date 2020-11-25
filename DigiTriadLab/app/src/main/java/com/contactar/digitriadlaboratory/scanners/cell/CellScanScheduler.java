package com.contactar.digitriadlaboratory.scanners.cell;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.database.entities.CellRecord;
import com.contactar.digitriadlaboratory.database.entities.SensorRecord;
import com.contactar.digitriadlaboratory.database.entities.WindowConfiguration;
import com.contactar.digitriadlaboratory.repositories.CellRepository;
import com.contactar.digitriadlaboratory.repositories.WifiRepository;
import com.contactar.digitriadlaboratory.scanners.Scheduler;
import com.contactar.digitriadlaboratory.scanners.wifi.WifiDataBucket;

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