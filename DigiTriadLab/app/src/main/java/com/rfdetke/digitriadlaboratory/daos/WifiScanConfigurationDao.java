package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.WifiScanConfiguration;

@Dao
public interface WifiScanConfigurationDao {

    @Query("SELECT * FROM wifi_scan_configuration WHERE experiment_id==(:experimentId)")
    WifiScanConfiguration getScanConfigurationByExperimentId(int experimentId);

    @Insert
    void insert(WifiScanConfiguration scanConfiguration);

    @Delete
    void delete(WifiScanConfiguration scanConfiguration);
}
