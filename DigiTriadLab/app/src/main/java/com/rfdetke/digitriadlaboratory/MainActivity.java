package com.rfdetke.digitriadlaboratory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rfdetke.digitriadlaboratory.constants.RunStateEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabasePopulator;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.views.NewExperimentActivity;
import com.rfdetke.digitriadlaboratory.views.listadapters.ExperimentListAdapter;
import com.rfdetke.digitriadlaboratory.views.modelviews.ExperimentViewModel;

import java.util.Date;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    public static final int NEW_EXPERIMENT_ACTIVITY_REQUEST_CODE = 1;

    private ExperimentViewModel experimentViewModel;
    ExperimentListAdapter adapter;
    private Device device;
    private long runId;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.experiments));

        Button toolbarButton = (Button) findViewById(R.id.toolbar_button);
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
            startActivityForResult(intent, NEW_EXPERIMENT_ACTIVITY_REQUEST_CODE);
        });

        DatabasePopulator.prepopulate(getApplicationContext(),false);
        database = DatabaseSingleton.getInstance(getApplicationContext());
        database.getRunDao().deleteAll();
        database.getExperimentDao().deleteAll();

        device = database.getDeviceDao().getDevice();
        if(device == null) {
            database.getDeviceDao().insert(new Device("T-1", "SAMSUNG", "A50"));
            device = database.getDeviceDao().getDevice();
        }

        long exp1 = database.getExperimentDao().insert(new Experiment("EXP-001", "aaaa", device.id));
        long exp2 = database.getExperimentDao().insert(new Experiment("EXP-002", "bbbb", device.id));
        long exp3 = database.getExperimentDao().insert(new Experiment("EXP-003", "cccc", device.id));
        long exp4 = database.getExperimentDao().insert(new Experiment("EXP-004", null, device.id));

        Date date = new Date(System.currentTimeMillis()+1000*10);
        runId = database.getRunDao().insert(new Run(date, 1, RunStateEnum.DONE.name(), exp1));

        date.setTime(date.getTime()+1000*10);
        runId = database.getRunDao().insert(new Run(date, 2, RunStateEnum.DONE.name(), exp1));

        date.setTime(date.getTime()+1000*60*2);
        runId = database.getRunDao().insert(new Run(date, 1, RunStateEnum.SCHEDULED.name(), exp2));

        date.setTime(date.getTime()+1000*60*2);
        runId = database.getRunDao().insert(new Run(date, 2, RunStateEnum.SCHEDULED.name(), exp2));

        date.setTime(date.getTime()+1000*60*2);
        runId = database.getRunDao().insert(new Run(date, 3, RunStateEnum.SCHEDULED.name(), exp2));

        runId = database.getRunDao().insert(new Run(date, 1, RunStateEnum.DONE.name(), exp4));
        runId = database.getRunDao().insert(new Run(date, 2, RunStateEnum.SCHEDULED.name(), exp4));
//        new AsyncTask<Integer, Integer, Integer>() {
//            @Override
//            protected Integer doInBackground(Integer... integers) {
//                try {
//                    Thread.sleep(integers[0]*1000);
//                    database.getRunDao().updateRunState(runId, RunStateEnum.RUNNING.name());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return integers[0];
//            }
//        }.execute(10);
//
//        new AsyncTask<Integer, Integer, Integer>() {
//            @Override
//            protected Integer doInBackground(Integer... integers) {
//                try {
//                    Thread.sleep(integers[0]*1000);
//                    database.getRunDao().updateRunState(runId, RunStateEnum.DONE.name());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return integers[0];
//            }
//        }.execute(20);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_EXPERIMENT_ACTIVITY_REQUEST_CODE && resultCode != RESULT_OK) {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    public void refreshList() {
        Experiment exp = experimentViewModel.getLast();
        exp.id = 0;
        long id = experimentViewModel.insert(exp);
        exp.id = id;
        experimentViewModel.delete(exp);
    }
}
