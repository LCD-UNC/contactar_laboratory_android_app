package com.rfdetke.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.BluetoothRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao.BluetoothSampleRecord;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothRecord;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;

import java.util.List;

public class BluetoothRepository {
    private SampleDao sampleDao;
    private BluetoothRecordDao bluetoothRecordDao;
    private SensorRecordDao sensorRecordDao;

    public BluetoothRepository(AppDatabase database) {
        sampleDao = database.getSampleDao();
        bluetoothRecordDao = database.getBluetoothRecordDao();
        sensorRecordDao = database.getSensorRecordDao();
    }

    public long[] insertBluetooth(List<BluetoothRecord> bluetoothRecord) {
        return bluetoothRecordDao.insert(bluetoothRecord);
    }

    public long[] insertSensors(List<SensorRecord> sensorRecord) {
        return sensorRecordDao.insert(sensorRecord);
    }

    public List<BluetoothSampleRecord> getAllSamples(long[] runs) {
        return sampleDao.getBluetoothSamplesRecords(runs);
    }

    public LiveData<Long> getLiveCount(long runId) {
        return sampleDao.getBluetoothLiveCount(runId);
    }
}
