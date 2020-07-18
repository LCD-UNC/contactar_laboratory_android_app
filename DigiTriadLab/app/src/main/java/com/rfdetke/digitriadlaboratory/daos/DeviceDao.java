package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.Device;

@Dao
public interface DeviceDao {

    @Query("SELECT * FROM device WHERE id == (:deviceId) LIMIT 1")
    Device getDeviceById(int deviceId);

    @Insert
    void insert(Device device);

    @Delete
    void delete(Device device);
}