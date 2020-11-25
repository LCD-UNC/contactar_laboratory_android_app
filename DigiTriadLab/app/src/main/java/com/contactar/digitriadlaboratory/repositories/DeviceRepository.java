package com.contactar.digitriadlaboratory.repositories;

import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.database.daos.DeviceDao;
import com.contactar.digitriadlaboratory.database.entities.Device;

public class DeviceRepository {
    private DeviceDao deviceDao;

    public DeviceRepository(AppDatabase database) {
        this.deviceDao = database.getDeviceDao();
    }

    public Device getDevice() {return deviceDao.getDevice(); }

    public long insert(Device device) {
        return deviceDao.insert(device);
    }

    public void update(Device device) {
        deviceDao.update(device.id, device.codename, device.uuid);
    }
}
