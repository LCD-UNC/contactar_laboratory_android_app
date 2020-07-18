package com.rfdetke.digitriadlaboratory.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rfdetke.digitriadlaboratory.entities.Experiment;

import java.util.List;

@Dao
public interface ExperimentDao {

    @Query("SELECT * FROM experiment")
    List<Experiment> getAllExperiments();

    @Query("SELECT * FROM experiment WHERE codename == (:codename) LIMIT 1")
    Experiment getExperimentByCodename(String codename);

    @Insert
    void insert(Experiment experiment);

    @Delete
    void delete(Experiment experiment);

}
