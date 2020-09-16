package com.rfdetke.digitriadlaboratory.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.rfdetke.digitriadlaboratory.R;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.views.listadapters.ExperimentListAdapter;
import com.rfdetke.digitriadlaboratory.views.listadapters.RunListAdapter;
import com.rfdetke.digitriadlaboratory.views.modelviews.ExperimentDetailViewModel;

import java.util.List;
import java.util.Objects;

public class ExperimentDetailActivity extends AppCompatActivity {

    private static final String EXTRA_ID = "com.rfdetke.digitriadlaboratory.ID";

    private RunListAdapter adapter;
    private ExperimentDetailViewModel experimentDetailViewModel;
    private Toolbar topToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_detail);

        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);

        long experimentId = getIntent().getLongExtra(ExperimentListAdapter.EXTRA_ID, 0);
        experimentDetailViewModel = new ViewModelProvider(this).get(ExperimentDetailViewModel.class);
        experimentDetailViewModel.setExperiment(experimentId);

        ImageView qrContainer = findViewById(R.id.qr_container);
        qrContainer.setImageBitmap(experimentDetailViewModel.getExperimentQr());

        Experiment currentExperiment = experimentDetailViewModel.getCurrentExperiment();

        Button addRunButton = findViewById(R.id.add_run_button);
        addRunButton.setOnClickListener(v -> {
            Intent intent = new Intent(ExperimentDetailActivity.this, NewRunActivity.class);
            intent.putExtra(EXTRA_ID, experimentId);
            startActivity(intent);
        });

        List<String> tagsList = experimentDetailViewModel.getTagList();
        ChipGroup tags = findViewById(R.id.tags);
        for(String tagValue : tagsList) {
            Chip chip = new Chip(this);
            chip.setText(tagValue.toUpperCase());
            tags.addView(chip);
        }

        String configurationString = experimentDetailViewModel.getConfigurationString();

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

        experimentDetailViewModel.getRunsForExperiment().observe(this, runs -> {
            adapter.setRuns(runs);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.detail_experiment_menu, menu);
        return true;
    }

    public void deleteExperiment(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_confirm_delete)
                .setPositiveButton(R.string.yes, (dialog, id) -> {
                    experimentDetailViewModel.getRunsForExperiment().removeObservers(this);
                    experimentDetailViewModel.delete();
                    finish();
                })
                .setNeutralButton(R.string.no, ((dialog, id) -> {
                    dialog.dismiss();
                }))
                .create().show();
    }
}