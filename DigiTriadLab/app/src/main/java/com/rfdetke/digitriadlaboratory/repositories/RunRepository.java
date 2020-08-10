package com.rfdetke.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.RunDao;
import com.rfdetke.digitriadlaboratory.database.daos.RunDao.StartDuration;
import com.rfdetke.digitriadlaboratory.database.entities.Run;

import java.util.List;

public class RunRepository {

    private RunDao runDao;

    public RunRepository(AppDatabase database) {
        runDao = database.getRunDao();
    }

    public LiveData<List<Run>> getRunsForExperiment(long id) {
        return runDao.getLiveDataRunsByExperimentId(id);
    }

    public void updateState(long id, String state) {
        runDao.updateRunState(id, state);
    }

    public long getLastRunForExperiment(long id) {
        return runDao.getLastRunNumberByExperimentId(id);
    }

    public List<StartDuration> getCurrentScheduledOrRunningStartAndDuration() {
        return runDao.timePerConfiguration();
    }

    public long getMaxDurationForExperiment(long id) {
        return runDao.maxDurationByExperimentId(id);
    }

    public Run getById(long id) {
        return runDao.getRunById(id);
    }

    public LiveData<Run> getLiveById(long id) {
        return runDao.getLiveRunById(id);
    }

    public long insert(Run run) { return runDao.insert(run); }

    public void delete(Run run) { runDao.delete(run); }
}
