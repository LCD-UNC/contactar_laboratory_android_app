package com.contactar.contactarlaboratory.scanners.cell;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;

import com.contactar.contactarlaboratory.database.entities.CellRecord;
import com.contactar.contactarlaboratory.scanners.DataBucket;

import java.util.ArrayList;
import java.util.List;

public class CellDataBucket implements DataBucket {
    private final long sampleId;
    TelephonyManager telephonyManager;

    public CellDataBucket(long sampleId, Context context) {
        this.sampleId = sampleId;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public List<Object> getRecordsList() {
        List<Object> records = new ArrayList<>();
        if(telephonyManager != null) {
            ArrayList<CellInfo> results = (ArrayList<CellInfo>) telephonyManager.getAllCellInfo();
            for (CellInfo result : results) {
                records.add(new CellRecord(result, sampleId));
            }
        }
        return records;
    }

}
