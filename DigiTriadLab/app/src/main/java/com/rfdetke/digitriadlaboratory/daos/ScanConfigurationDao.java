package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.ScanConfiguration;

@Dao
public interface ScanConfigurationDao {

    @Query("SELECT * FROM scan_configuration WHERE id==(:id)")
    ScanConfiguration getScanConfigurationById(int id);

    @Query("SELECT * FROM scan_configuration WHERE experiment_id==(:experimentId) AND source_type==(:type)")
    ScanConfiguration getScanConfigurationByExperimentIdAndType(int experimentId, int type);

    @Insert
    long insert(ScanConfiguration scanConfiguration);

    @Delete
    void delete(ScanConfiguration scanConfiguration);
}
