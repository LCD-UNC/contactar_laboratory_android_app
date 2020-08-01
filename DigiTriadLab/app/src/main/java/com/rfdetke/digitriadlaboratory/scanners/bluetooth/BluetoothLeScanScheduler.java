package com.rfdetke.digitriadlaboratory.scanners.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.daos.BluetoothLeRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.SourceTypeDao;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.database.entities.Sample;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;
import com.rfdetke.digitriadlaboratory.scanners.ScanScheduler;
import com.rfdetke.digitriadlaboratory.scanners.sensors.SensorDataBucket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BluetoothLeScanScheduler extends ScanScheduler {

    private final BluetoothLeRecordDao bluetoothLeRecordDao;
    private final SensorRecordDao sensorRecordDao;

    BluetoothLeDataBucket bluetoothLeDataBucket;
    SensorDataBucket sensorDataBucket;

    public BluetoothLeScanScheduler(long runId, WindowConfiguration windowConfiguration, Context context,
                                    SampleDao sampleDao, SourceTypeDao sourceTypeDao,
                                    BluetoothLeRecordDao bluetoothLeRecordDao,
                                    SensorRecordDao sensorRecordDao) {
        super(runId, windowConfiguration, context, sampleDao, sourceTypeDao);

        this.bluetoothLeRecordDao = bluetoothLeRecordDao;
        this.sensorRecordDao = sensorRecordDao;
        this.key = SourceTypeEnum.BLUETOOTH_LE.toString();

        sensorDataBucket = new SensorDataBucket(context);
    }

    @Override
    protected void registerScanDataBucket() {
        long sampleId = sampleDao.insert(new Sample(new Date(), runId,
                sourceTypeDao.getSourceTypeByType(key).id));

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
        bluetoothLeRecordDao.insert(bluetoothRecords);

        List<SensorRecord> sensorRecords = new ArrayList<>();
        for (Object record : sensorDataBucket.getRecordsList()) {
            sensorRecords.add((SensorRecord) record);
        }
        sensorRecordDao.insert(sensorRecords);

        bluetoothLeDataBucket = null;
        sensorDataBucket.setSampleId(0);
    }
}
