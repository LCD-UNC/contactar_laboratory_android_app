package com.rfdetke.digitriadlaboratory.scanners.cell;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;

import com.rfdetke.digitriadlaboratory.database.entities.CellRecord;
import com.rfdetke.digitriadlaboratory.scanners.DataBucket;
import com.rfdetke.digitriadlaboratory.scanners.sensors.TemporarySensorData;

import java.util.ArrayList;
import java.util.List;

public class CellDataBucket implements DataBucket {
    private final long sampleId;
    TelephonyManager telephonyManager;
    private List<Object> records;

    public CellDataBucket(long sampleId, Context context) {
        this.sampleId = sampleId;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

    }

    private void saveCellInfo() {
        if(telephonyManager != null) {
            ArrayList<CellInfo> results = (ArrayList<CellInfo>) telephonyManager.getAllCellInfo();
            for (CellInfo result : results) {
                records.add(new CellRecord(result, sampleId));
            }
        }
    }

    @Override
    public List<Object> getRecordsList() {
        List<Object> records = new ArrayList<>();
        saveCellInfo();
        return records;
    }

}
