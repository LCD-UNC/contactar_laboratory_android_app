package com.rfdetke.digitriadlaboratory.repositories;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.BluetoothRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothRecord;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;

import java.util.List;

public class BluetoothRepository {
    private BluetoothRecordDao bluetoothRecordDao;
    private SensorRecordDao sensorRecordDao;

    public BluetoothRepository(AppDatabase database) {
        bluetoothRecordDao = database.getBluetoothRecordDao();
        sensorRecordDao = database.getSensorRecordDao();
    }

    public long[] insertBluetooth(List<BluetoothRecord> bluetoothRecord) {
        return bluetoothRecordDao.insert(bluetoothRecord);
    }

    public long[] insertSensors(List<SensorRecord> sensorRecord) {
        return sensorRecordDao.insert(sensorRecord);
    }
}
