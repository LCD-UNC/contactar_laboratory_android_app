package com.rfdetke.digitriadlaboratory.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;

import java.util.List;

@Dao
public interface WindowConfigurationDao {

    @Query("SELECT * FROM window_configuration WHERE experiment_id==(:experimentId) AND source_type==(:type)")
    WindowConfiguration getWindowConfigurationByExperimentIdAndType(long experimentId, long type);

    @Query("SELECT * FROM window_configuration WHERE experiment_id==(:experimentId)")
    List<WindowConfiguration> getWindowConfigurationsByExperiment(long experimentId);

    @Insert
    long insert(WindowConfiguration windowConfiguration);

    @Delete
    void delete(WindowConfiguration windowConfiguration);
}
