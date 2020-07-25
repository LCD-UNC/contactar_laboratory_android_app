package com.rfdetke.digitriadlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.database.entities.Device;

import java.util.List;

@Dao
public interface DeviceDao {

    @Query("SELECT * FROM device WHERE id == (:deviceId) LIMIT 1")
    Device getDeviceById(int deviceId);

    @Query("SELECT * FROM device")
    List<Device> getAllDevices();

    @Insert
    long insert(Device device);

    @Delete
    void delete(Device device);
}