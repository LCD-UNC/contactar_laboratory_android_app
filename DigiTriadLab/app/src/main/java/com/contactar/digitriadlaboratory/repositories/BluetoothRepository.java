package com.contactar.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.database.daos.BluetoothRecordDao;
import com.contactar.digitriadlaboratory.database.daos.WindowDao;
import com.contactar.digitriadlaboratory.database.daos.WindowDao.BluetoothSampleRecord;
import com.contactar.digitriadlaboratory.database.daos.SensorRecordDao;
import com.contactar.digitriadlaboratory.database.entities.BluetoothRecord;
import com.contactar.digitriadlaboratory.database.entities.SensorRecord;

import java.util.List;

public class BluetoothRepository {
    private WindowDao windowDao;
    private BluetoothRecordDao bluetoothRecordDao;

    public BluetoothRepository(AppDatabase database) {
        windowDao = database.getWindowDao();
        bluetoothRecordDao = database.getBluetoothRecordDao();
    }

    public long[] insertBluetooth(List<BluetoothRecord> bluetoothRecord) {
        return bluetoothRecordDao.insert(bluetoothRecord);
    }

    public List<BluetoothSampleRecord> getAllSamples(long[] runs) {
        return windowDao.getBluetoothSamplesRecords(runs);
    }

    public LiveData<Long> getLiveCount(long runId) {
        return windowDao.getBluetoothLiveCount(runId);
    }
}
