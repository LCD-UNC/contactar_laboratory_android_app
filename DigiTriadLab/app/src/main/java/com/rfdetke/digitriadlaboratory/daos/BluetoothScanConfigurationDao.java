package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.BluetoothScanConfiguration;

@Dao
public interface BluetoothScanConfigurationDao {

    @Query("SELECT * FROM bluetooth_scan_configuration WHERE experiment_id==(:experimentId)")
    BluetoothScanConfiguration getScanConfigurationByExperimentId(int experimentId);

    @Insert
    void insert(BluetoothScanConfiguration scanConfiguration);

    @Delete
    void delete(BluetoothScanConfiguration scanConfiguration);
}
