package com.rfdetke.digitriadlaboratory.scanners.bluetooth;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;

import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.scanners.DataBucket;

import java.util.ArrayList;
import java.util.List;

public class BluetoothLeDataBucket extends ScanCallback implements DataBucket {

    private final long sampleId;
    private List<Object> records;

    public BluetoothLeDataBucket(long sampleId, Context context) {
        this.sampleId = sampleId;
        records = new ArrayList<>();
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
        if(result.getDevice().getName().equals("R"))
            records.add(new BluetoothLeRecord(result, sampleId));
    }

    @Override
    public List<Object> getRecordsList() {
        return records;
    }
}
