package com.rfdetke.digitriadlaboratory.contacthandlers.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.ParcelUuid;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeUuid;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.repositories.BluetoothLeRepository;
import com.rfdetke.digitriadlaboratory.contacthandlers.Scheduler;
import com.rfdetke.digitriadlaboratory.contacthandlers.sensors.SensorDataBucket;

import java.util.ArrayList;
import java.util.List;

public class BluetoothLeScanScheduler extends Scheduler {

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
    protected void startTask() {
        long sampleId = sampleRepository.insert(runId, key);

        bluetoothLeDataBucket = new BluetoothLeDataBucket(sampleId, context);
        sensorDataBucket.setSampleId(sampleId);

        // TODO: Implementar un mutex para hacer escaneo despues porque no se pueden hacer escaneos
        //       clasicos y low energy al mismo tiempo.
        BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().startScan(bluetoothLeDataBucket);
    }

    @Override
    protected void endTask() {
        BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().stopScan(bluetoothLeDataBucket);

        List<BluetoothLeRecord> bluetoothRecords = new ArrayList<>();
        List<List<ParcelUuid>> uuidLists = bluetoothLeDataBucket.getUuidLists();
        for (Object record : bluetoothLeDataBucket.getRecordsList()) {
            bluetoothRecords.add((BluetoothLeRecord) record);
        }
        long[] recordIds = bluetoothLeRepository.insertBluetoothLe(bluetoothRecords);

        List<BluetoothLeUuid> bluetoothLeUuids = new ArrayList<>();
        for(int i=0; i<recordIds.length; i++) {
            if (uuidLists.get(i) != null)
                for(ParcelUuid uuid : uuidLists.get(i)) {
                    bluetoothLeUuids.add(new BluetoothLeUuid(uuid, recordIds[i]));
                }
        }
        bluetoothLeRepository.insertUuids(bluetoothLeUuids);

        List<SensorRecord> sensorRecords = new ArrayList<>();
        for (Object record : sensorDataBucket.getRecordsList()) {
            sensorRecords.add((SensorRecord) record);
        }
        bluetoothLeRepository.insertSensors(sensorRecords);

        bluetoothLeDataBucket = null;
        sensorDataBucket.setSampleId(0);
    }
}
