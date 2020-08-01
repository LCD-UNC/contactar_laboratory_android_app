package com.rfdetke.digitriadlaboratory.repositories;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.DeviceDao;
import com.rfdetke.digitriadlaboratory.database.entities.Device;

public class DeviceRepository {
    private DeviceDao deviceDao;

    public DeviceRepository(AppDatabase database) {
        this.deviceDao = database.getDeviceDao();
    }

    public Device getDevice() {return deviceDao.getDevice(); }
}
