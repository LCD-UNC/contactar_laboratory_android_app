package com.rfdetke.digitriadlaboratory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabasePopulator;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.views.DeviceInfoActivity;
import com.rfdetke.digitriadlaboratory.views.NewExperimentActivity;
import com.rfdetke.digitriadlaboratory.views.listadapters.ExperimentListAdapter;
import com.rfdetke.digitriadlaboratory.views.modelviews.ExperimentViewModel;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private ExperimentViewModel experimentViewModel;
    ExperimentListAdapter adapter;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.experiments));

        Button toolbarButton = findViewById(R.id.toolbar_button);
        toolbarButton.setOnClickListener(v -> {
            refreshList();
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new ExperimentListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        experimentViewModel = new ViewModelProvider(this).get(ExperimentViewModel.class);
        experimentViewModel.getAllExperimentDone().observe(this, experiments -> adapter.setExperiments(experiments));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewExperimentActivity.class);
            startActivity(intent);
        });

        database = DatabaseSingleton.getInstance(getApplicationContext());
        DatabasePopulator.prepopulate(database);
    }

    public void refreshList() {
        Intent intent = new Intent(MainActivity.this, DeviceInfoActivity.class);
        startActivity(intent);
    }
}
