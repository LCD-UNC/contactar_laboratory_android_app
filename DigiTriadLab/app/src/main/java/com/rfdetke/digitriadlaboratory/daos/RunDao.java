package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.Run;

import java.util.List;

@Dao
public interface RunDao {

    @Query("SELECT * FROM run WHERE experiment_id==(:experimentId)")
    List<Run> getAllRunsByExperimentId(int experimentId);

    @Query("SELECT * FROM run WHERE id==(:runId) LIMIT 1")
    List<Run> getRunById(int runId);

    @Insert
    void insert(Run run);

    @Delete
    void delete(Run run);
}
