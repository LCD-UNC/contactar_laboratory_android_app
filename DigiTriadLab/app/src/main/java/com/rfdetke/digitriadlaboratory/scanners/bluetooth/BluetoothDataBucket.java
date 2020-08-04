package com.rfdetke.digitriadlaboratory.scanners.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rfdetke.digitriadlaboratory.database.entities.BluetoothRecord;
import com.rfdetke.digitriadlaboratory.scanners.DataBucket;

import java.util.ArrayList;
import java.util.List;

public class BluetoothDataBucket extends BroadcastReceiver implements DataBucket {

    private final long sampleId;
    private List<Object> records;

    public BluetoothDataBucket(long sampleId, Context context) {
        this.sampleId = sampleId;
        records = new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device != null) {
                records.add(new BluetoothRecord(device, sampleId));
            }

        }
    }

    @Override
    public List<Object> getRecordsList() {
        return records;
    }
}
