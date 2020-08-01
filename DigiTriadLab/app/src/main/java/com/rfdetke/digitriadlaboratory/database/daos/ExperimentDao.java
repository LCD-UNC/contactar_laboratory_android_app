package com.rfdetke.digitriadlaboratory.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.database.entities.Experiment;

import java.util.Date;
import java.util.List;

@Dao
public interface ExperimentDao {

    @Query("SELECT * FROM experiment")
    List<Experiment> getAllExperiments();

    @Query("SELECT * FROM experiment")
    LiveData<List<Experiment>> getLiveDataExperiments();


    @Query("SELECT e.*, d.done, t.total FROM experiment as e " +
            "LEFT JOIN (SELECT COUNT(id) as done, experiment_id " +
                        "FROM (SELECT r.id, r.experiment_id " +
                                "FROM run as r WHERE r.state=\"DONE\") " +
                                "GROUP BY experiment_id) as d " +
            "ON e.id=d.experiment_id " +
            "LEFT JOIN (SELECT COUNT(r.id) as total, r.experiment_id FROM run as r GROUP BY r.experiment_id) as t " +
            "ON e.id=t.experiment_id")

    LiveData<List<ExperimentDone>> getLiveDataExperimentDone();


    @Query("SELECT * FROM experiment WHERE codename == (:codename) LIMIT 1")
    Experiment getExperimentByCodename(String codename);

    @Query("SELECT * FROM experiment ORDER BY id DESC LIMIT 1")
    Experiment getLastExperiment();

    @Query("SELECT * FROM experiment WHERE id == (:id) LIMIT 1")
    Experiment getExperimentById(long id);

    @Insert
    long insert(Experiment experiment);

    @Delete
    void delete(Experiment experiment);

    @Query("DELETE FROM experiment")
    void deleteAll();

    static class ExperimentDone {
        public long id;
        public String codename;
        public String description;
        public long done;
        public long total;

        @ColumnInfo(name = "device_id")
        public long deviceId;
    }

}
