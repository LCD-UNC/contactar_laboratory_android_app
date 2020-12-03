package com.contactar.contactarlaboratory.scanners.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;

import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.entities.BluetoothRecord;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.repositories.BluetoothRepository;
import com.contactar.contactarlaboratory.scanners.Scheduler;

import java.util.ArrayList;
import java.util.List;

public class BluetoothScanScheduler extends Scheduler {

    private final BluetoothRepository bluetoothRepository;
    BluetoothDataBucket bluetoothDataBucket;

    public BluetoothScanScheduler(long runId, long randomTime, WindowConfiguration windowConfiguration, Context context,
                                  AppDatabase database) {
        super(runId, randomTime, windowConfiguration, context, database);

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
