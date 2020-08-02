package com.rfdetke.digitriadlaboratory.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.database.entities.Run;

import java.util.Date;
import java.util.List;

@Dao
public interface RunDao {

    @Query("SELECT * FROM run")
    List<Run> getAllRuns();

    @Query("SELECT * FROM run WHERE experiment_id==(:experimentId)")
    List<Run> getRunsByExperimentId(long experimentId);

    @Query("SELECT * FROM run WHERE experiment_id==(:experimentId)")
    LiveData<List<Run>> getLiveDataRunsByExperimentId(long experimentId);

    @Query("SELECT start, max as duration FROM run as r " +
            "LEFT JOIN (SELECT max(total_time) as max, experiment_id " +
                        "FROM (SELECT ((active_time+inactive_time)*windows) as total_time, experiment_id " +
                                    "FROM window_configuration) " +
                        "GROUP BY experiment_id) as ts " +
            "ON r.experiment_id=ts.experiment_id " +
            "WHERE r.state=\"SCHEDULED\" OR r.state=\"RUNNING\"")
    List<StartDuration> timePerConfiguration();

    @Query("SELECT max FROM (SELECT max(total_time) as max, experiment_id " +
                            "FROM (SELECT ((active_time+inactive_time)*windows) as total_time, experiment_id " +
                                    "FROM window_configuration) " +
                            "GROUP BY experiment_id)" +
            "WHERE experiment_id=(:experimentId)")
    long maxDurationByExperimentId(long experimentId);

    @Query("SELECT max(number) FROM run WHERE experiment_id=(:experimentId) GROUP BY experiment_id")
    long getLastRunNumberByExperimentId(long experimentId);

    @Query("SELECT * FROM run WHERE id==(:runId) LIMIT 1")
    Run getRunById(int runId);

    @Query("UPDATE run SET state=(:state) WHERE id=(:id)")
    void updateRunState(long id, String state);

    @Insert
    long insert(Run run);

    @Delete
    void delete(Run run);

    @Query("DELETE FROM run")
    void deleteAll();

    static class StartDuration {
        public Date start;
        public long duration;
    }
}
