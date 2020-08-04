package com.rfdetke.digitriadlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.database.entities.AdvertiseConfiguration;

import java.util.List;

@Dao
public interface BluetoothLeAdvertiseConfigurationDao {

    @Insert
    long insert(AdvertiseConfiguration advertiseConfiguration);

    @Query("SELECT * FROM AdvertiseConfiguration WHERE experiment_id=(:id)")
    AdvertiseConfiguration getBluetoothLeAdvertiseConfigurationForByExperiment(long id);

    @Query("SELECT * FROM AdvertiseConfiguration")
    List<AdvertiseConfiguration> getAllBluetoothLeAdvertiseConfiguration();
}
