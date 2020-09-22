package com.rfdetke.digitriadlaboratory.scanners.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothRecord;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.BluetoothRepository;
import com.rfdetke.digitriadlaboratory.scanners.Scheduler;

import java.util.ArrayList;
import java.util.List;

public class BluetoothScanScheduler extends Scheduler {

    private final BluetoothRepository bluetoothRepository;
    BluetoothDataBucket bluetoothDataBucket;

    public BluetoothScanScheduler(long runId, WindowConfiguration windowConfiguration, Context context,
                                  AppDatabase database) {
        super(runId, windowConfiguration, context, database);

        this.bluetoothRepository = new BluetoothRepository(database);
        this.key = SourceTypeEnum.BLUETOOTH.toString();
    }

    @Override
    protected void startTask() {
        long sampleId = windowRepository.insert(runId, this.windowCount, key);
        IntentFilter bluetoothIntentFilter = new IntentFilter();
        bluetoothIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        bluetoothDataBucket = new BluetoothDataBucket(sampleId, context);
        context.registerReceiver(bluetoothDataBucket, bluetoothIntentFilter);

        // TODO: Implementar un mutex para hacer escaneo despues porque no se pueden hacer escaneos
        //       clasicos y low energy al mismo tiempo.
        BluetoothAdapter.getDefaultAdapter().startDiscovery();
    }

    @Override
    protected void endTask() {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

        List<BluetoothRecord> bluetoothRecords = new ArrayList<>();
        for (Object record : bluetoothDataBucket.getRecordsList()) {
            bluetoothRecords.add((BluetoothRecord) record);
        }
        bluetoothRepository.insertBluetooth(bluetoothRecords);

        context.unregisterReceiver(bluetoothDataBucket);
        bluetoothDataBucket = null;
    }

    @Override
    public void stop() {
        super.stop();
        if(bluetoothDataBucket!=null) {
            context.unregisterReceiver(bluetoothDataBucket);
            bluetoothDataBucket = null;
        }
    }
}
