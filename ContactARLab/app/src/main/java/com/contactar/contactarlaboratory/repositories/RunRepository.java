package com.contactar.contactarlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.daos.RunDao;
import com.contactar.contactarlaboratory.database.daos.RunDao.StartDuration;
import com.contactar.contactarlaboratory.database.entities.Run;

import java.util.List;

public class RunRepository {

    private RunDao runDao;

    public RunRepository(AppDatabase database) {
        runDao = database.getRunDao();
    }

    public LiveData<List<Run>> getLiveRunsForExperiment(long id) {
        return runDao.getLiveDataRunsByExperimentId(id);
    }

    public long[] getRunIdsForExperiment(long id) {
        return runDao.getRunIdsByExperimentId(id);
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
