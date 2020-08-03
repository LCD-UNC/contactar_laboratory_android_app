package com.rfdetke.digitriadlaboratory.scanners.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.repositories.BluetoothLeRepository;
import com.rfdetke.digitriadlaboratory.scanners.ScanScheduler;
import com.rfdetke.digitriadlaboratory.scanners.sensors.SensorDataBucket;

import java.util.ArrayList;
import java.util.List;

public class BluetoothLeScanScheduler extends ScanScheduler {

    private final BluetoothLeRepository bluetoothLeRepository;

    BluetoothLeDataBucket bluetoothLeDataBucket;
    SensorDataBucket sensorDataBucket;

    public BluetoothLeScanScheduler(long runId, WindowConfiguration windowConfiguration, Context context,
                                    AppDatabase database) {
        super(runId, windowConfiguration, context, database);

        this.bluetoothLeRepository = new BluetoothLeRepository(database);
        this.key = SourceTypeEnum.BLUETOOTH_LE.toString();

        sensorDataBucket = new SensorDataBucket(context);
    }

    @Override
    protected void registerScanDataBucket() {
        long sampleId = sampleRepository.insert(runId, key);

        bluetoothLeDataBucket = new BluetoothLeDataBucket(sampleId, context);
        sensorDataBucket.setSampleId(sampleId);

        // TODO: Implementar un mutex para hacer escaneo despues porque no se pueden hacer escaneos
        //       clasicos y low energy al mismo tiempo.
        BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().startScan(bluetoothLeDataBucket);
    }

    @Override
    protected void unregisterScanDataBucket() {
        BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().stopScan(bluetoothLeDataBucket);

        List<BluetoothLeRecord> bluetoothRecords = new ArrayList<>();
        for (Object record : bluetoothLeDataBucket.getRecordsList()) {
            bluetoothRecords.add((BluetoothLeRecord) record);
        }
        bluetoothLeRepository.insertBluetoothLe(bluetoothRecords);

        List<SensorRecord> sensorRecords = new ArrayList<>();
        for (Object record : sensorDataBucket.getRecordsList()) {
            sensorRecords.add((SensorRecord) record);
        }
        bluetoothLeRepository.insertSensors(sensorRecords);

        bluetoothLeDataBucket = null;
        sensorDataBucket.setSampleId(0);
    }
}
