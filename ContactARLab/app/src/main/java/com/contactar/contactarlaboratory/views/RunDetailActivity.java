package com.contactar.contactarlaboratory.views;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.contactar.contactarlaboratory.AlarmReceiver;
import com.contactar.contactarlaboratory.ExperimentService;
import com.contactar.contactarlaboratory.R;
import com.contactar.contactarlaboratory.constants.RunStateEnum;
import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.DatabaseSingleton;
import com.contactar.contactarlaboratory.database.entities.Run;
import com.contactar.contactarlaboratory.export.FileWriter;
import com.contactar.contactarlaboratory.export.csv.ActivityCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.BatteryCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.BluetoothCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.BluetoothLeCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.CellCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.GpsCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.SensorCsvFileWriter;
import com.contactar.contactarlaboratory.export.csv.WifiCsvFileWriter;
import com.contactar.contactarlaboratory.export.json.JsonExperimentFileWriter;
import com.contactar.contactarlaboratory.gapis.GoogleSessionAppCompatActivity;
import com.contactar.contactarlaboratory.gapis.drive.DriveServiceHelper;
import com.contactar.contactarlaboratory.gapis.drive.folderselector.FolderPickerActivity;
import com.contactar.contactarlaboratory.views.listadapters.RunListAdapter;
import com.contactar.contactarlaboratory.views.modelviews.RunDetailViewModel;

import java.util.List;
import java.util.Locale;

public class RunDetailActivity extends GoogleSessionAppCompatActivity {

    private RunDetailViewModel runDetailViewModel;
    private Toolbar topToolbar;
    private Run currentRun;
    private AppDatabase database;
    private Context context;

    private boolean keepScreenOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_run_detail);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);

        long runId = getIntent().getLongExtra(RunListAdapter.EXTRA_ID, 0);

        TextView sensorCount = findViewById(R.id.sensors_count);
        TextView wifiCount = findViewById(R.id.wifi_count);
        TextView bluetoothCount = findViewById(R.id.bluetooth_count);
        TextView bluetoothLeCount = findViewById(R.id.bluetooth_le_count);
        TextView cellCount = findViewById(R.id.cell_count);
        TextView gpsCount = findViewById(R.id.gps_count);
        TextView batteryCount = findViewById(R.id.battery_count);
        TextView activityCount = findViewById(R.id.activity_count);

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

        runDetailViewModel.getGpsCount().observe(this, count -> {
            if (count != null)
                gpsCount.setText(String.format(Locale.ENGLISH, "%d", count));
            else
                gpsCount.setText("0");
        });

        runDetailViewModel.getBatteryCount().observe(this, count -> {
            if (count != null)
                batteryCount.setText(String.format(Locale.ENGLISH, "%d", count));
            else
                batteryCount.setText("0");
        });
        runDetailViewModel.getActivityCount().observe(this, count -> {
            if (count != null)
                activityCount.setText(String.format(Locale.ENGLISH, "%d", count));
            else
                activityCount.setText("0");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.detail_run_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FolderPickerActivity.REQUEST_PICK_FOLDER) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String folderId = data.getStringExtra(FolderPickerActivity.EXTRA_FOLDER_ID);
                String folderPath = data.getStringExtra(FolderPickerActivity.EXTRA_FOLDER_PATH);
                Toast.makeText(this, getResources().getString(R.string.uploading_to_folder, folderPath), Toast.LENGTH_SHORT).show();
                uploadToDrive(folderId);
            }
        }
    }

    @Override
    public boolean onMenuItemClickListener(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                return true;

            case R.id.export:
                exportData();
                return true;

            case R.id.upload:
                selectGoogleDriveDestination();
                return true;

            default:
                return false;
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

        if (modules.contains(SourceTypeEnum.GPS.name()))
            new GpsCsvFileWriter(runs, database, context).execute();

        if (modules.contains(SourceTypeEnum.BATTERY.name()))
            new BatteryCsvFileWriter(runs, database, context).execute();

        if (modules.contains(SourceTypeEnum.ACTIVITY.name()))
            new ActivityCsvFileWriter(runs, database, context).execute();

        new JsonExperimentFileWriter(currentRun.experimentId, database, context).execute();
        Toast.makeText(this, R.string.files_exported, Toast.LENGTH_SHORT).show();
    }

    public void selectGoogleDriveDestination() {
        if(googleServicesHelper.isSignedIn(getApplicationContext())) {
            Intent intent = new Intent(RunDetailActivity.this, FolderPickerActivity.class);
            startActivityForResult(intent, FolderPickerActivity.REQUEST_PICK_FOLDER);
        } else {
            Toast.makeText(this, R.string.sign_in_first, Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadToDrive(String folderId) {
        List<String> modules = runDetailViewModel.getModules();
        long[] runs = {currentRun.id};
        if (modules.contains(SourceTypeEnum.WIFI.name()))
            writeDriveFile(folderId, new WifiCsvFileWriter(runs, database, context));

        if (modules.contains(SourceTypeEnum.BLUETOOTH.name()))
            writeDriveFile(folderId, new BluetoothCsvFileWriter(runs, database, context));

        if (modules.contains(SourceTypeEnum.BLUETOOTH_LE.name()))
            writeDriveFile(folderId, new BluetoothLeCsvFileWriter(runs, database, context));

        if (modules.contains(SourceTypeEnum.SENSORS.name()))
            writeDriveFile(folderId, new SensorCsvFileWriter(runs, database, context));

        if (modules.contains(SourceTypeEnum.CELL.name()))
            writeDriveFile(folderId, new CellCsvFileWriter(runs, database, context));

        if (modules.contains(SourceTypeEnum.GPS.name()))
            writeDriveFile(folderId, new GpsCsvFileWriter(runs, database, context));

        if (modules.contains(SourceTypeEnum.BATTERY.name()))
            writeDriveFile(folderId, new BatteryCsvFileWriter(runs, database, context));

        if (modules.contains(SourceTypeEnum.ACTIVITY.name()))
            writeDriveFile(folderId, new ActivityCsvFileWriter(runs, database, context));

        writeDriveFile(folderId, new JsonExperimentFileWriter(currentRun.experimentId, database, context));
    }

    private void writeDriveFile(String folderId, FileWriter fileWriter) {
        DriveServiceHelper driveServiceHelper = googleServicesHelper.getDriveService(getApplicationContext());
        driveServiceHelper.createFile(fileWriter.getFileName(), folderId)
                .addOnSuccessListener(s ->
                        driveServiceHelper.updateFileContent(s, fileWriter.getContent())
                                .addOnFailureListener(e ->
                                        Toast.makeText(getApplicationContext(),
                                                getString(R.string.failed_uploading),
                                                Toast.LENGTH_SHORT).show()))
                .addOnFailureListener(e ->
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.failed_creating),
                                Toast.LENGTH_SHORT).show());
    }

    public void cancelRun(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_confirm_cancel)
                .setPositiveButton(R.string.yes, (dialog, id) -> {
                    runDetailViewModel.getCurrentLiveRun().removeObservers(this);
                    runDetailViewModel.getSensorCount().removeObservers(this);
                    runDetailViewModel.getWifiCount().removeObservers(this);
                    runDetailViewModel.getCellCount().removeObservers(this);
                    runDetailViewModel.getGpsCount().removeObservers(this);
                    runDetailViewModel.getBatteryCount().removeObservers(this);
                    runDetailViewModel.getActivityCount().removeObservers(this);
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
                            runDetailViewModel.getGpsCount().removeObservers(this);
                        runDetailViewModel.getBatteryCount().removeObservers(this);
                        runDetailViewModel.getActivityCount().removeObservers(this);
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
            Toast.makeText(this, R.string.cancel_run_message, Toast.LENGTH_LONG).show();
        }
    }

    public void changeScreenBehaviour(MenuItem item) {
        if(keepScreenOn) {
            item.setIcon(R.drawable.ic_baseline_mobile_off_24);
            keepScreenOn = false;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            item.setIcon(R.drawable.ic_baseline_screen_lock_portrait_24);
            keepScreenOn = true;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}