package com.rfdetke.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.BluetoothLeRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao.BluetoothLeSampleRecord;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeUuid;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;

import java.util.List;

public class BluetoothLeRepository {
    private SampleDao sampleDao;
    private BluetoothLeRecordDao bluetoothLeRecordDao;
    private SensorRecordDao sensorRecordDao;

    public BluetoothLeRepository(AppDatabase database) {
        sampleDao = database.getSampleDao();
        bluetoothLeRecordDao = database.getBluetoothLeRecordDao();
        sensorRecordDao = database.getSensorRecordDao();
    }

    public long[] insertBluetoothLe(List<BluetoothLeRecord> bluetoothLeRecord) {
        return bluetoothLeRecordDao.insert(bluetoothLeRecord);
    }

    public long[] insertSensors(List<SensorRecord> sensorRecord) {
        return sensorRecordDao.insert(sensorRecord);
    }

    public List<BluetoothLeSampleRecord> getAllSamples(long[] runs) {
        return sampleDao.getBluetoothLeSamplesRecords(runs);
    }

    public long[] insertUuids(List<BluetoothLeUuid> bluetoothLeUuids) {
        return bluetoothLeRecordDao.insertUuids(bluetoothLeUuids);
    }

    public LiveData<Long> getLiveCount(long runId) {
        return sampleDao.getBluetoothLeLiveCount(runId);
    }
}
