package com.rfdetke.digitriadlaboratory.scanners.cell;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.CellRecord;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.CellRepository;
import com.rfdetke.digitriadlaboratory.repositories.WifiRepository;
import com.rfdetke.digitriadlaboratory.scanners.Scheduler;
import com.rfdetke.digitriadlaboratory.scanners.wifi.WifiDataBucket;

import java.util.ArrayList;
import java.util.List;

public class CellScanScheduler extends Scheduler {

    private final CellRepository cellRepository;
    CellDataBucket cellDataBucket;

    public CellScanScheduler(long runId, WindowConfiguration windowConfiguration, Context context,
                             AppDatabase database) {
        super(runId, windowConfiguration, context, database);
        this.cellRepository = new CellRepository(database);
        this.key = SourceTypeEnum.CELL.toString();
    }

    @Override
    protected void startTask() {
        long sampleId = windowRepository.insert(runId, key);
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