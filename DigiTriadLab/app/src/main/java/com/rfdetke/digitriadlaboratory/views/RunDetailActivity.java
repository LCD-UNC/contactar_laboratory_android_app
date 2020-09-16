package com.rfdetke.digitriadlaboratory.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
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
import com.rfdetke.digitriadlaboratory.export.csv.CellCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.csv.SensorCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.csv.WifiCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.json.JsonExperimentFileWriter;
import com.rfdetke.digitriadlaboratory.gapis.GoogleServicesHelper;
import com.rfdetke.digitriadlaboratory.views.listadapters.RunListAdapter;
import com.rfdetke.digitriadlaboratory.views.modelviews.RunDetailViewModel;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.rfdetke.digitriadlaboratory.gapis.GoogleServicesHelper.REQUEST_CODE_SIGN_IN;

public class RunDetailActivity extends AppCompatActivity {

    private RunDetailViewModel runDetailViewModel;
    private Toolbar topToolbar;
    private Run currentRun;
    private GoogleServicesHelper googleServicesHelper;
    private AppDatabase database;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_detail);

        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);

        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setOnMenuItemClickListener((Toolbar.OnMenuItemClickListener) item -> {
            switch (item.getItemId()) {
                case R.id.sign_out:
                    signOut();
                    return true;

                case R.id.export:
                    exportData();
                    return true;

                case R.id.upload:
                    uploadToDrive();
                    return true;

                default:
                    return false;
            }
        });

        long runId = getIntent().getLongExtra(RunListAdapter.EXTRA_ID, 0);

        TextView sensorCount = findViewById(R.id.sensors_count);
        TextView wifiCount = findViewById(R.id.wifi_count);
        TextView bluetoothCount = findViewById(R.id.bluetooth_count);
        TextView bluetoothLeCount = findViewById(R.id.bluetooth_le_count);
        TextView cellCount = findViewById(R.id.cell_count);

        ProgressBar progressBar = findViewById(R.id.progress_bar);

        runDetailViewModel = new ViewModelProvider(this).get(RunDetailViewModel.class);
        runDetailViewModel.setRun(runId);

        currentRun = runDetailViewModel.getCurrentRun();
        context = getApplicationContext();
        database = DatabaseSingleton.getInstance(context);

        runDetailViewModel.getCurrentLiveRun().observe(this, run -> {
            topToolbar.setTitle(getString(R.string.run_label, currentRun.number));
            if (run.state.equals(RunStateEnum.RUNNING.name())) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        runDetailViewModel.getSensorCount().observe(this, count -> {
            if (count != null)
                sensorCount.setText(String.format(Locale.ENGLISH, "%d", count));
            else
                sensorCount.setText("0");
        });

        runDetailViewModel.getCellCount().observe(this, count -> {
            if (count != null)
                cellCount.setText(String.format(Locale.ENGLISH, "%d", count));
            else
                cellCount.setText("0");
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

        googleServicesHelper = GoogleServicesHelper.getInstance(getApplicationContext(), findViewById(R.id.signInFab));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.detail_run_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (data != null) {
                    googleServicesHelper.handleSignInResult(getApplicationContext(), data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void signIn(View view) {
        if(!googleServicesHelper.isSignedIn(getApplicationContext()))
            startActivityForResult(googleServicesHelper.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    public void signOut(){
        if(googleServicesHelper.isSignedIn(getApplicationContext())) {
            googleServicesHelper.signOut(getApplicationContext());
            Toast.makeText(this, "Signed out...", Toast.LENGTH_SHORT).show();
        }
    }

    public void exportData() {
        List<String> modules = runDetailViewModel.getModules();
        long[] runs = {currentRun.id};
        if (modules.contains(SourceTypeEnum.WIFI.name()))
            new WifiCsvFileWriter(runs, database, context).execute();

        if (modules.contains(SourceTypeEnum.BLUETOOTH.name()))
            new BluetoothCsvFileWriter(runs, database, context).execute();

        if (modules.contains(SourceTypeEnum.BLUETOOTH_LE.name()))
            new BluetoothLeCsvFileWriter(runs, database, context).execute();

        if (modules.contains(SourceTypeEnum.SENSORS.name()))
            new SensorCsvFileWriter(runs, database, context).execute();

        if (modules.contains(SourceTypeEnum.CELL.name()))
            new CellCsvFileWriter(runs, database, context).execute();

        new JsonExperimentFileWriter(currentRun.experimentId, database, context).execute();
        Toast.makeText(this, "Files exported...", Toast.LENGTH_SHORT).show();
    }

    public void uploadToDrive() {
        //TODO: Implementar para subir todas las corridas a Google Drive. Cambiar mensaje del Toast!
        Toast.makeText(this, "Not implemented yet!", Toast.LENGTH_SHORT).show();
    }

    public void cancelRun(MenuItem item) {
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
    }

    public void deleteRun(MenuItem menuItem) {
        if(!currentRun.state.equals(RunStateEnum.RUNNING.name())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialog_confirm_delete)
                    .setPositiveButton(R.string.yes, (dialog, id) -> {
                            runDetailViewModel.getCurrentLiveRun().removeObservers(this);
                            runDetailViewModel.getSensorCount().removeObservers(this);
                            runDetailViewModel.getWifiCount().removeObservers(this);
                            runDetailViewModel.getBluetoothCount().removeObservers(this);
                            runDetailViewModel.getBluetoothLeCount().removeObservers(this);
                            runDetailViewModel.getCellCount().removeObservers(this);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
                            alarmIntent.putExtra(NewRunActivity.EXTRA_RUN_ID, currentRun.id);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                    getApplicationContext(), (int)currentRun.id, alarmIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                            assert alarmManager != null;
                            alarmManager.cancel(pendingIntent);
                            stopService(new Intent(getApplicationContext(), ExperimentService.class));
                            runDetailViewModel.delete();
                            finish();
                    })
                    .setNeutralButton(R.string.no, ((dialog, id) -> {
                        dialog.dismiss();
                    }))
                    .create().show();
        } else {
            Toast.makeText(this, "Cancel Run first!", Toast.LENGTH_LONG).show();
        }
    }
}