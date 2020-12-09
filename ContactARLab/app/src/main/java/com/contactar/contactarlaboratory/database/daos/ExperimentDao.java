package com.contactar.contactarlaboratory.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.database.entities.Run;

import java.util.List;

@Dao
public interface ExperimentDao {

    @Query("SELECT e.id, e.codename, e.description, e.device_id, d.done, t.total, r.running, c.canceled FROM experiment as e " +
            "LEFT JOIN (SELECT COUNT(id) as done, experiment_id " +
                        "FROM (SELECT r.id, r.experiment_id " +
                                "FROM run as r WHERE r.state=\"DONE\") " +
                                "GROUP BY experiment_id) as d " +
            "ON e.id=d.experiment_id " +
            "LEFT JOIN (SELECT COUNT(id) as running, experiment_id " +
                        "FROM (SELECT r.id, r.experiment_id " +
                                "FROM run as r WHERE r.state=\"RUNNING\") " +
                                "GROUP BY experiment_id) as r " +
            "ON e.id=r.experiment_id " +
            "LEFT JOIN (SELECT COUNT(id) as canceled, experiment_id " +
                        "FROM (SELECT r.id, r.experiment_id " +
                                "FROM run as r WHERE r.state=\"CANCELED\") " +
                                "GROUP BY experiment_id) as c " +
            "ON e.id=c.experiment_id " +
            "LEFT JOIN (SELECT COUNT(r.id) as total, r.experiment_id FROM run as r GROUP BY r.experiment_id) as t " +
            "ON e.id=t.experiment_id")

    LiveData<List<ExperimentDone>> getLiveDataExperimentDone();

    @Query("SELECT * FROM experiment ORDER BY id DESC LIMIT 1")
    Experiment getLastExperiment();

    @Query("SELECT * FROM experiment WHERE id == (:id) LIMIT 1")
    Experiment getExperimentById(long id);

    @Query("SELECT * FROM run WHERE state==\"RUNNING\" AND  experiment_id == (:id)")
    List<Run> getOnGoingRunsForExperiment(long id);
    @Insert
    long insert(Experiment experiment);

    @Delete
    void delete(Experiment experiment);

    static class ExperimentDone {
        public long id;
        public String codename;
        public String description;
        public long done;
        public long canceled;
        public long total;
        public long running;

        @ColumnInfo(name = "device_id")
        public long deviceId;
    }

}
