package com.rfdetke.digitriadlaboratory.scanners.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothRecord;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.repositories.BluetoothRepository;
import com.rfdetke.digitriadlaboratory.scanners.Scheduler;
import com.rfdetke.digitriadlaboratory.scanners.sensors.SensorDataBucket;

import java.util.ArrayList;
import java.util.List;

public class BluetoothScanScheduler extends Scheduler {

    private final BluetoothRepository bluetoothRepository;
    BluetoothDataBucket bluetoothDataBucket;
    SensorDataBucket sensorDataBucket;

    public BluetoothScanScheduler(long runId, WindowConfiguration windowConfiguration, Context context,
                                  AppDatabase database) {
        super(runId, windowConfiguration, context, database);

        this.bluetoothRepository = new BluetoothRepository(database);
        this.key = SourceTypeEnum.BLUETOOTH.toString();
        sensorDataBucket = new SensorDataBucket(context);
    }

    @Override
    protected void startTask() {
        long sampleId = sampleRepository.insert(runId, key);
        IntentFilter bluetoothIntentFilter = new IntentFilter();
        bluetoothIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        bluetoothDataBucket = new BluetoothDataBucket(sampleId, context);
        sensorDataBucket.setSampleId(sampleId);
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

        List<SensorRecord> sensorRecords = new ArrayList<>();
        for (Object record : sensorDataBucket.getRecordsList()) {
            sensorRecords.add((SensorRecord) record);
        }
        bluetoothRepository.insertSensors(sensorRecords);

        context.unregisterReceiver(bluetoothDataBucket);
        bluetoothDataBucket = null;
        sensorDataBucket.setSampleId(0);
    }
}
