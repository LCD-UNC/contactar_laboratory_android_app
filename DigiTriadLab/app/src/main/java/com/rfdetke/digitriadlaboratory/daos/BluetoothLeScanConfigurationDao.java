package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.BluetoothLeScanConfiguration;

@Dao
public interface BluetoothLeScanConfigurationDao extends DeviceDao{

    @Query("SELECT * FROM bluetooth_le_scan_configuration WHERE experiment_id==(:experimentId)")
    BluetoothLeScanConfiguration getScanConfigurationByExperimentId(int experimentId);

    @Insert
    void insert(BluetoothLeScanConfiguration scanConfiguration);

    @Delete
    void delete(BluetoothLeScanConfiguration scanConfiguration);
}
