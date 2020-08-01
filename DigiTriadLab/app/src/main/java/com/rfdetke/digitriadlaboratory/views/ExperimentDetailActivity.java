package com.rfdetke.digitriadlaboratory.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rfdetke.digitriadlaboratory.R;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.ConfigurationRepository;
import com.rfdetke.digitriadlaboratory.repositories.ExperimentRepository;
import com.rfdetke.digitriadlaboratory.repositories.SourceTypeRepository;
import com.rfdetke.digitriadlaboratory.views.listadapters.ExperimentListAdapter;
import com.rfdetke.digitriadlaboratory.views.listadapters.RunListAdapter;
import com.rfdetke.digitriadlaboratory.views.modelviews.RunViewModel;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ExperimentDetailActivity extends AppCompatActivity {

    private RunListAdapter adapter;
    private RunViewModel runViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_detail);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);

        long experimentId = getIntent().getLongExtra(ExperimentListAdapter.EXTRA_ID, 0);
        AppDatabase database = DatabaseSingleton.getInstance(getApplicationContext());

        ExperimentRepository experimentRepository = new ExperimentRepository(database);
        Experiment currentExperiment = experimentRepository.getById(experimentId);
        ConfigurationRepository configurationRepository = new ConfigurationRepository(database);
        List<WindowConfiguration> configurations = configurationRepository.getConfigurationsForExperiment(currentExperiment.id);
        SourceTypeRepository sourceTypeRepository = new SourceTypeRepository(database);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getResources().getString(R.string.experiment_detail, currentExperiment.codename));
        findViewById(R.id.toolbar_button).setVisibility(View.INVISIBLE);

        String configurationString = "";
        for(WindowConfiguration configuration : configurations) {
            configurationString = configurationString.concat(String.format(Locale.ENGLISH,
                    "%s:\n  Active: %d s Inactive: %d s Windows: %d\n",
                    sourceTypeRepository.getById(configuration.sourceType).type,
                    configuration.activeTime, configuration.inactiveTime, configuration.windows));
        }

        TextView descriptionTextView = findViewById(R.id.experiment_description);
        if (currentExperiment.description == null || currentExperiment.description.isEmpty()) {
            descriptionTextView.setText(getResources().getString(R.string.description_text,
                    getResources().getString(R.string.no_description), configurationString));
        } else {
            descriptionTextView.setText(getResources().getString(R.string.description_text,
                    currentExperiment.description, configurationString));
        }

        RecyclerView recyclerView = findViewById(R.id.run_recyclerview);
        adapter = new RunListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        runViewModel = new ViewModelProvider(this).get(RunViewModel.class);
        runViewModel.setRunsForExperiment(experimentId);
        runViewModel.getRunsForExperiment().observe(this, runs -> {
            adapter.setRuns(runs);
        });
    }
}