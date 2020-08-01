package com.rfdetke.digitriadlaboratory.views.modelviews;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;

import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.repositories.RunRepository;

import java.util.List;

public class RunViewModel extends AndroidViewModel {

    private RunRepository runRepository;
    private LiveData<List<Run>> runsForExperiment;

    public RunViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = DatabaseSingleton.getInstance(application.getApplicationContext());
        runRepository = new RunRepository(appDatabase);
    }

    public void setRunsForExperiment(long experimentId) {
        this.runsForExperiment = runRepository.getRunsForExperiment(experimentId);
    }

    public LiveData<List<Run>> getRunsForExperiment() {
        return runsForExperiment;
    }

    public long insert(Run run) { return runRepository.insert(run); }

    public void delete(Run run) { runRepository.delete(run); }


}
