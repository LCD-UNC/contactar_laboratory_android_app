package com.rfdetke.digitriadlaboratory.scanners.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.daos.BluetoothRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.SourceTypeDao;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothRecord;
import com.rfdetke.digitriadlaboratory.database.entities.Sample;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.scanners.ScanScheduler;
import com.rfdetke.digitriadlaboratory.scanners.sensors.SensorDataBucket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BluetoothScanScheduler extends ScanScheduler {

    private final BluetoothRecordDao bluetoothRecordDao;
    private final SensorRecordDao sensorRecordDao;

    BluetoothDataBucket bluetoothDataBucket;
    SensorDataBucket sensorDataBucket;

    public BluetoothScanScheduler(long runId, WindowConfiguration windowConfiguration, Context context,
                                  SampleDao sampleDao, SourceTypeDao sourceTypeDao,
                                  BluetoothRecordDao bluetoothRecordDao,
                                  SensorRecordDao sensorRecordDao) {
        super(runId, windowConfiguration, context, sampleDao, sourceTypeDao);

        this.bluetoothRecordDao = bluetoothRecordDao;
        this.sensorRecordDao = sensorRecordDao;
        this.key = SourceTypeEnum.BLUETOOTH.toString();
        sensorDataBucket = new SensorDataBucket(context);
    }

    @Override
    protected void registerScanDataBucket() {
        long sampleId = sampleDao.insert(new Sample(new Date(), runId,
                sourceTypeDao.getSourceTypeByType(key).id));

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
    protected void unregisterScanDataBucket() {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

        List<BluetoothRecord> bluetoothRecords = new ArrayList<>();
        for (Object record : bluetoothDataBucket.getRecordsList()) {
            bluetoothRecords.add((BluetoothRecord) record);
        }
        bluetoothRecordDao.insert(bluetoothRecords);

        List<SensorRecord> sensorRecords = new ArrayList<>();
        for (Object record : sensorDataBucket.getRecordsList()) {
            sensorRecords.add((SensorRecord) record);
        }
        sensorRecordDao.insert(sensorRecords);

        context.unregisterReceiver(bluetoothDataBucket);
        bluetoothDataBucket = null;
        sensorDataBucket.setSampleId(0);
    }
}
