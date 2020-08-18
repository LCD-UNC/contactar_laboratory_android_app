package com.rfdetke.digitriadlaboratory.scanners.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.ParcelUuid;
import android.telephony.CellSignalStrength;
import android.util.Log;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeUuid;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.repositories.BluetoothLeRepository;
import com.rfdetke.digitriadlaboratory.scanners.Scheduler;
import com.rfdetke.digitriadlaboratory.scanners.sensors.SensorDataBucket;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BluetoothLeScanScheduler extends Scheduler {

    private final BluetoothLeRepository bluetoothLeRepository;
    BluetoothLeDataBucket bluetoothLeDataBucket;

    public BluetoothLeScanScheduler(long runId, WindowConfiguration windowConfiguration, Context context,
                                    AppDatabase database) {
        super(runId, windowConfiguration, context, database);

        this.bluetoothLeRepository = new BluetoothLeRepository(database);
        this.key = SourceTypeEnum.BLUETOOTH_LE.toString();

    }

    @Override
    protected void startTask() {
        long sampleId = windowRepository.insert(runId, key);
        bluetoothLeDataBucket = new BluetoothLeDataBucket(sampleId, context);

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
            else
                Log.d("BluetoothLeScanScheduler", String.format(Locale.ENGLISH, "No UUIDs for record %d", recordIds[i]));
        }
        bluetoothLeRepository.insertUuids(bluetoothLeUuids);

        bluetoothLeDataBucket = null;
    }
}
