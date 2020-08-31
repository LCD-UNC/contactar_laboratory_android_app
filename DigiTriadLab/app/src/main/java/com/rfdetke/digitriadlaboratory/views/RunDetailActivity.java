package com.rfdetke.digitriadlaboratory.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rfdetke.digitriadlaboratory.AlarmReceiver;
import com.rfdetke.digitriadlaboratory.ExperimentService;
import com.rfdetke.digitriadlaboratory.R;
import com.rfdetke.digitriadlaboratory.constants.RunStateEnum;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.export.csv.BluetoothCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.csv.BluetoothLeCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.csv.SensorCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.csv.WifiCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.json.JsonExperimentFileWriter;
import com.rfdetke.digitriadlaboratory.views.listadapters.RunListAdapter;
import com.rfdetke.digitriadlaboratory.views.modelviews.RunDetailViewModel;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RunDetailActivity extends AppCompatActivity {

    private RunDetailViewModel runDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_detail);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.delete_toolbar);

        long runId = getIntent().getLongExtra(RunListAdapter.EXTRA_ID, 0);

        TextView sensorCount = findViewById(R.id.sensors_count);
        TextView wifiCount = findViewById(R.id.wifi_count);
        TextView bluetoothCount = findViewById(R.id.bluetooth_count);
        TextView bluetoothLeCount = findViewById(R.id.bluetooth_le_count);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        Button exportButton = findViewById(R.id.export_button);
        Button deleteButton = findViewById(R.id.delete_button);

        runDetailViewModel = new ViewModelProvider(this).get(RunDetailViewModel.class);
        runDetailViewModel.setRun(runId);

        List<String> modules = runDetailViewModel.getModules();
        Run currentRun = runDetailViewModel.getCurrentRun();
        Context context = getApplicationContext();
        AppDatabase database = DatabaseSingleton.getInstance(context);

        exportButton.setOnClickListener(v -> {
            long[] runs = {currentRun.id};
            if (modules.contains(SourceTypeEnum.WIFI.name()))
                new WifiCsvFileWriter(runs, database, context).execute();

            if (modules.contains(SourceTypeEnum.BLUETOOTH.name()))
                new BluetoothCsvFileWriter(runs, database, context).execute();

            if (modules.contains(SourceTypeEnum.BLUETOOTH_LE.name()))
                new BluetoothLeCsvFileWriter(runs, database, context).execute();

            if (modules.contains(SourceTypeEnum.SENSORS.name()))
                new SensorCsvFileWriter(runs, database, context).execute();

            new JsonExperimentFileWriter(currentRun.experimentId, database, context).execute();
            Toast.makeText(this, "Files exported...", Toast.LENGTH_SHORT).show();
        });

        deleteButton.setOnClickListener(v -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_confirm_cancel)
                        .setPositiveButton(R.string.yes, (dialog, id) -> {
                            runDetailViewModel.getCurrentLiveRun().removeObservers(this);
                            runDetailViewModel.getSensorCount().removeObservers(this);
                            runDetailViewModel.getWifiCount().removeObservers(this);
                            runDetailViewModel.getBluetoothCount().removeObservers(this);
                            runDetailViewModel.getBluetoothLeCount().removeObservers(this);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                            alarmIntent.putExtra(NewRunActivity.EXTRA_RUN_ID, currentRun.id);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                    getApplicationContext(), (int)currentRun.id, alarmIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                            assert alarmManager != null;
                            alarmManager.cancel(pendingIntent);
                            if(currentRun.state.equals(RunStateEnum.RUNNING.name())) {
                                stopService(new Intent(getApplicationContext(), ExperimentService.class));
                            }
                            runDetailViewModel.updateState(RunStateEnum.CANCELED.name());
                            finish();
                        })
                        .setNeutralButton(R.string.no, ((dialog, id) -> {
                            dialog.dismiss();
                        }))
                        .create().show();

        });

        runDetailViewModel.getCurrentLiveRun().observe(this, run -> {
            toolbarTitle.setText(getString(R.string.run_label, run.number));
            if (run.state.equals(RunStateEnum.DONE.name())
                    || run.state.equals(RunStateEnum.CANCELED.name())
                    || run.state.equals(RunStateEnum.SCHEDULED.name())) {
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
            }
        });

        runDetailViewModel.getSensorCount().observe(this, count -> {
            if (count != null)
                sensorCount.setText(String.format(Locale.ENGLISH, "%d", count));
            else
                sensorCount.setText("0");
        });

        runDetailViewModel.getWifiCount().observe(this, count -> {
            if (count != null)
                wifiCount.setText(String.format(Locale.ENGLISH, "%d", count));
            else
                wifiCount.setText("0");
        });

        runDetailViewModel.getBluetoothCount().observe(this, count -> {
            if (count != null)
                bluetoothCount.setText(String.format(Locale.ENGLISH, "%d", count));
            else
                bluetoothCount.setText("0");
        });

        runDetailViewModel.getBluetoothLeCount().observe(this, count -> {
            if (count != null)
                bluetoothLeCount.setText(String.format(Locale.ENGLISH, "%d", count));
            else
                bluetoothLeCount.setText("0");
        });
    }
}