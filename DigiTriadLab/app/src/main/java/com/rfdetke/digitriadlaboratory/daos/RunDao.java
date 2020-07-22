package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.Run;

import java.util.List;

@Dao
public interface RunDao {

    @Query("SELECT * FROM run")
    List<Run> getAllRuns();

    @Query("SELECT * FROM run WHERE experiment_id==(:experimentId)")
    List<Run> getRunsByExperimentId(long experimentId);

    @Query("SELECT * FROM run WHERE id==(:runId) LIMIT 1")
    Run getRunById(int runId);

    @Insert
    long insert(Run run);

    @Delete
    void delete(Run run);
}
