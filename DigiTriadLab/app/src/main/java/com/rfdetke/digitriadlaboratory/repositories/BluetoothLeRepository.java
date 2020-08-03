package com.rfdetke.digitriadlaboratory.repositories;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.BluetoothLeRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.entities.BluetoothLeRecord;
import com.rfdetke.digitriadlaboratory.database.entities.SensorRecord;

import java.util.List;

public class BluetoothLeRepository {
    private BluetoothLeRecordDao bluetoothLeRecordDao;
    private SensorRecordDao sensorRecordDao;

    public BluetoothLeRepository(AppDatabase database) {
        bluetoothLeRecordDao = database.getBluetoothLeRecordDao();
        sensorRecordDao = database.getSensorRecordDao();
    }

    public long[] insertBluetoothLe(List<BluetoothLeRecord> bluetoothLeRecord) {
        return bluetoothLeRecordDao.insert(bluetoothLeRecord);
    }

    public long[] insertSensors(List<SensorRecord> sensorRecord) {
        return sensorRecordDao.insert(sensorRecord);
    }
}
