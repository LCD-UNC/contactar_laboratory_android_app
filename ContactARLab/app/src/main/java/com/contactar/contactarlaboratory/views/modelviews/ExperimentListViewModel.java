package com.contactar.contactarlaboratory.views.modelviews;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.DatabaseSingleton;
import com.contactar.contactarlaboratory.database.daos.ExperimentDao.ExperimentDone;
import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.repositories.ExperimentRepository;

import java.util.List;

public class ExperimentListViewModel extends AndroidViewModel {

    private ExperimentRepository experimentRepository;
    private LiveData<List<ExperimentDone>> allExperimentDone;

    public ExperimentListViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = DatabaseSingleton.getInstance(application.getApplicationContext());
        experimentRepository = new ExperimentRepository(appDatabase);
        allExperimentDone = experimentRepository.getAllExperimentDone();
    }

    public LiveData<List<ExperimentDone>> getAllExperimentDone() {
        return allExperimentDone;
    }

    public long insert(Experiment experiment) { return experimentRepository.insert(experiment); }

    public void delete(Experiment experiment) { experimentRepository.delete(experiment); }

    public Experiment getLast() { return experimentRepository.getLast(); }

}
