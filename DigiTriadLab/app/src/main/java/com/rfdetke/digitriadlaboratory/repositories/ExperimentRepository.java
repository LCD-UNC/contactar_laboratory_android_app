package com.rfdetke.digitriadlaboratory.repositories;

import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.ExperimentDao;
import com.rfdetke.digitriadlaboratory.database.daos.ExperimentDao.ExperimentDone;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;

import java.util.List;

public class ExperimentRepository {
    private ExperimentDao experimentDao;
    private LiveData<List<ExperimentDone>> allExperimentDone;

    public ExperimentRepository(AppDatabase database) {
        experimentDao = database.getExperimentDao();
        allExperimentDone = experimentDao.getLiveDataExperimentDone();
    }

    public LiveData<List<ExperimentDone>> getAllExperimentDone() {
        return allExperimentDone;
    }

    public long insert(Experiment experiment) {
        return experimentDao.insert(experiment);
    }

    public void delete(Experiment experiment) {
        experimentDao.delete(experiment);
    }

    public Experiment getLast() {
        return experimentDao.getLastExperiment();
    }
}
