package com.contactar.contactarlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.daos.BluetoothLeRecordDao;
import com.contactar.contactarlaboratory.database.daos.WindowDao;
import com.contactar.contactarlaboratory.database.daos.WindowDao.BluetoothLeSampleRecord;
import com.contactar.contactarlaboratory.database.entities.BluetoothLeRecord;
import com.contactar.contactarlaboratory.database.entities.BluetoothLeUuid;

import java.util.List;

public class BluetoothLeRepository {
    private WindowDao windowDao;
    private BluetoothLeRecordDao bluetoothLeRecordDao;

    public BluetoothLeRepository(AppDatabase database) {
        windowDao = database.getWindowDao();
        bluetoothLeRecordDao = database.getBluetoothLeRecordDao();
    }

    public long[] insertBluetoothLe(List<BluetoothLeRecord> bluetoothLeRecord) {
        return bluetoothLeRecordDao.insert(bluetoothLeRecord);
    }

    public List<BluetoothLeSampleRecord> getAllSamples(long[] runs) {
        return windowDao.getBluetoothLeSamplesRecords(runs);
    }

    public long[] insertUuids(List<BluetoothLeUuid> bluetoothLeUuids) {
        return bluetoothLeRecordDao.insertUuids(bluetoothLeUuids);
    }

    public LiveData<Long> getLiveCount(long runId) {
        return windowDao.getBluetoothLeLiveCount(runId);
    }
}
