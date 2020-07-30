package com.rfdetke.digitriadlaboratory.scanners.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.rfdetke.digitriadlaboratory.database.entities.WifiRecord;
import com.rfdetke.digitriadlaboratory.scanners.DataBucket;

import java.util.ArrayList;
import java.util.List;

public class WifiDataBucket extends BroadcastReceiver implements DataBucket {

    private final long sampleId;
    private WifiManager wifiManager;
    private List<Object> records;

    public WifiDataBucket(long sampleId, Context context) {
        this.sampleId = sampleId;
        records = new ArrayList<>();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        saveScanResults();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false))
            saveScanResults();
    }

    private void saveScanResults() {
        if(wifiManager != null) {
            ArrayList<ScanResult> results = (ArrayList<ScanResult>) wifiManager.getScanResults();
            for (ScanResult result : results) {
                records.add(new WifiRecord(result, sampleId));
            }
        }
    }

    @Override
    public List<Object> getRecordsList() {
        return records;
    }
}
