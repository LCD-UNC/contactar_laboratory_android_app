package com.rfdetke.digitriadlaboratory.database.daos;

import android.os.ParcelUuid;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.database.entities.Device;

import java.util.List;

@Dao
public interface DeviceDao {

    @Query("SELECT * FROM device LIMIT 1")
    Device getDevice();

    @Insert
    long insert(Device device);

    @Delete
    void delete(Device device);

    @Query("UPDATE device SET codename=(:codename), uuid=(:uuid) WHERE id=(:id)")
    void update(long id, String codename, ParcelUuid uuid);
}