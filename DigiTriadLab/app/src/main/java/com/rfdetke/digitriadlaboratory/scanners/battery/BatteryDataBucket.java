package com.rfdetke.digitriadlaboratory.scanners.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.util.Log;

import com.rfdetke.digitriadlaboratory.database.entities.BatteryRecord;
import com.rfdetke.digitriadlaboratory.database.entities.WifiRecord;
import com.rfdetke.digitriadlaboratory.scanners.DataBucket;

import java.util.ArrayList;
import java.util.List;

public class BatteryDataBucket extends BroadcastReceiver implements DataBucket {

    private final long sampleId;
    private BatteryManager batteryManager;
    private List<Object> records;

    public BatteryDataBucket(long sampleId, Context context) {
        this.sampleId = sampleId;
        records = new ArrayList<>();
        batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);

        //saveScanResults();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction != null) {
            switch (intentAction) {
                case Intent.ACTION_POWER_CONNECTED:
                    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    Intent batteryStatus = context.registerReceiver(null, ifilter);
                    int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                    float batteryCharge = level * 100 / (float)scale;

                    boolean lowBattery = batteryStatus.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW, false);
                    boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL;
                    boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                    boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
                    records.add(new BatteryRecord(lowBattery,isCharging, usbCharge, acCharge,batteryCharge,sampleId));
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    records.add(new BatteryRecord(false,false, false, false,-999,sampleId));
                    break;
                case Intent.ACTION_BATTERY_CHANGED:
                    /*int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                    float batteryCharge = level * 100 / (float)scale;

                    boolean lowBattery = intent.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW, false);
                    boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL;
                    boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                    boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

                    records.add(new BatteryRecord(lowBattery,isCharging, usbCharge, acCharge,batteryCharge,sampleId));*/
                    break;
            }
        }

    }

    @Override
    public List<Object> getRecordsList() {
        return records;
    }
}
