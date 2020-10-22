package com.rfdetke.digitriadlaboratory.scanners.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeUuid;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.BluetoothLeRepository;
import com.rfdetke.digitriadlaboratory.scanners.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.rfdetke.digitriadlaboratory.advertisers.BluetoothLeAdvertiseScheduler.EXPERIMENT_SERVICE_UUID;

public class BluetoothLeScanScheduler extends Scheduler {

    public static final ParcelUuid EXPERIMENT_SERVICE_UUID_MASK = new ParcelUuid(UUID.fromString("FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF"));

    private final BluetoothLeRepository bluetoothLeRepository;
    BluetoothLeDataBucket bluetoothLeDataBucket;

    public BluetoothLeScanScheduler(long runId, long randomTime, WindowConfiguration windowConfiguration, Context context,
                                    AppDatabase database) {
        super(runId, randomTime, windowConfiguration, context, database);

        this.bluetoothLeRepository = new BluetoothLeRepository(database);
        this.key = SourceTypeEnum.BLUETOOTH_LE.toString();

    }

    @Override
    protected void startTask() {
        long sampleId = windowRepository.insert(runId, this.windowCount, key);
        bluetoothLeDataBucket = new BluetoothLeDataBucket(sampleId, context);

        ScanFilter filter = new ScanFilter.Builder().setServiceUuid(EXPERIMENT_SERVICE_UUID, EXPERIMENT_SERVICE_UUID_MASK).build();
        ArrayList<ScanFilter> scanFilterList = new ArrayList<>();
        scanFilterList.add(filter);

        ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).setLegacy(false).setReportDelay(0).build();

        // TODO: Implementar un mutex para hacer escaneo despues porque no se pueden hacer escaneos
        //       clasicos y low energy al mismo tiempo.
        BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().startScan(scanFilterList, scanSettings, bluetoothLeDataBucket);
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

    @Override
    public void stop() {
        super.stop();
        if(bluetoothLeDataBucket!=null) {
            BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().stopScan(bluetoothLeDataBucket);
            bluetoothLeDataBucket = null;
        }
    }
}
