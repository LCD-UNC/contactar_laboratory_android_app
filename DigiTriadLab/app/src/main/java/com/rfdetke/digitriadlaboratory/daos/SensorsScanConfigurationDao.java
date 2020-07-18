package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.SensorsScanConfiguration;

@Dao
public interface SensorsScanConfigurationDao {

    @Query("SELECT * FROM sensors_scan_configuration WHERE experiment_id==(:experimentId)")
    SensorsScanConfiguration getScanConfigurationByExperimentId(int experimentId);

    @Insert
    void insert(SensorsScanConfiguration scanConfiguration);

    @Delete
    void delete(SensorsScanConfiguration scanConfiguration);
}
