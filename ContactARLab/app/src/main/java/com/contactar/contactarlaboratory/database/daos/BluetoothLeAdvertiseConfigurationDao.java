package com.contactar.contactarlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.contactar.contactarlaboratory.database.entities.AdvertiseConfiguration;

@Dao
public interface BluetoothLeAdvertiseConfigurationDao {

    @Insert
    long insert(AdvertiseConfiguration advertiseConfiguration);

    @Query("SELECT * FROM advertise_configuration WHERE experiment_id=(:id)")
    AdvertiseConfiguration getBluetoothLeAdvertiseConfigurationForByExperiment(long id);
}
