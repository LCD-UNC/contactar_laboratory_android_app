package com.contactar.digitriadlaboratory.scanners.bluetooth;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.ParcelUuid;

import com.contactar.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.contactar.digitriadlaboratory.scanners.DataBucket;

import java.util.ArrayList;
import java.util.List;

public class BluetoothLeDataBucket extends ScanCallback implements DataBucket {

    private final long sampleId;
    private List<Object> records;
    private List<List<ParcelUuid>> uuidLists;

    public BluetoothLeDataBucket(long sampleId, Context context) {
        this.sampleId = sampleId;
        records = new ArrayList<>();
        uuidLists = new ArrayList<>();
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {

        records.add(new BluetoothLeRecord(result, sampleId));
        if(result.getScanRecord() != null)
            uuidLists.add(result.getScanRecord().getServiceUuids());
        else
            uuidLists.add(null);
    }

    @Override
    public List<Object> getRecordsList() {
        return records;
    }

    public List<List<ParcelUuid>> getUuidLists() { return uuidLists; }
}
