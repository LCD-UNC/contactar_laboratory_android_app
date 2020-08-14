package com.rfdetke.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.BluetoothLeRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao.BluetoothLeSampleRecord;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeUuid;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;

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
