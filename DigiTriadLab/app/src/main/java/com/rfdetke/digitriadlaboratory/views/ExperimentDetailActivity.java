package com.rfdetke.digitriadlaboratory.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.rfdetke.digitriadlaboratory.MainActivity;
import com.rfdetke.digitriadlaboratory.R;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.export.FileWriter;
import com.rfdetke.digitriadlaboratory.export.csv.BluetoothCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.csv.BluetoothLeCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.csv.CellCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.csv.GpsCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.csv.SensorCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.csv.WifiCsvFileWriter;
import com.rfdetke.digitriadlaboratory.export.json.JsonExperimentFileWriter;
import com.rfdetke.digitriadlaboratory.gapis.GoogleSessionAppCompatActivity;
import com.rfdetke.digitriadlaboratory.gapis.drive.DriveServiceHelper;
import com.rfdetke.digitriadlaboratory.gapis.drive.folderselector.FolderPickerActivity;
import com.rfdetke.digitriadlaboratory.views.listadapters.ExperimentListAdapter;
import com.rfdetke.digitriadlaboratory.views.listadapters.RunListAdapter;
import com.rfdetke.digitriadlaboratory.views.modelviews.ExperimentDetailViewModel;
import com.rfdetke.digitriadlaboratory.repositories.DeviceRepository;

import java.util.List;

import static com.rfdetke.digitriadlaboratory.gapis.GoogleServicesHelper.REQUEST_CODE_SIGN_IN;

public class ExperimentDetailActivity extends GoogleSessionAppCompatActivity {

    private static final String EXTRA_ID = "com.rfdetke.digitriadlaboratory.ID";

    private RunListAdapter adapter;
    private ExperimentDetailViewModel experimentDetailViewModel;
    private Toolbar topToolbar;
    private Experiment currentExperiment;

    private AppDatabase database;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_experiment_detail);
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        database = DatabaseSingleton.getInstance(context);

        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);

        long experimentId = getIntent().getLongExtra(ExperimentListAdapter.EXTRA_ID, 0);
        experimentDetailViewModel = new ViewModelProvider(this).get(ExperimentDetailViewModel.class);
        experimentDetailViewModel.setExperiment(experimentId);

        ImageView qrContainer = findViewById(R.id.qr_container);
        qrContainer.setImageBitmap(experimentDetailViewModel.getExperimentQr());

        currentExperiment = experimentDetailViewModel.getCurrentExperiment();

        topToolbar.setTitle(getString(R.string.experiment_detail, currentExperiment.codename));

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
        long[] runs = experimentDetailViewModel.getRunIdsForExperiment(currentExperiment.id);

        if (experimentDetailViewModel.getModules().contains(SourceTypeEnum.WIFI.name()))
            new WifiCsvFileWriter(runs, database, context).execute();

        if (experimentDetailViewModel.getModules().contains(SourceTypeEnum.BLUETOOTH.name()))
            new BluetoothCsvFileWriter(runs, database, context).execute();

        if (experimentDetailViewModel.getModules().contains(SourceTypeEnum.BLUETOOTH_LE.name()))
            new BluetoothLeCsvFileWriter(runs, database, context).execute();

        if (experimentDetailViewModel.getModules().contains(SourceTypeEnum.SENSORS.name()))
            new SensorCsvFileWriter(runs, database, context).execute();

        if (experimentDetailViewModel.getModules().contains(SourceTypeEnum.CELL.name()))
            new CellCsvFileWriter(runs, database, context).execute();

        if (experimentDetailViewModel.getModules().contains(SourceTypeEnum.GPS.name()))
            new GpsCsvFileWriter(runs, database, context).execute();

        new JsonExperimentFileWriter(currentExperiment.id, database, context).execute();
        Toast.makeText(this, R.string.files_exported, Toast.LENGTH_SHORT).show();
    }

    public void selectGoogleDriveDestination() {
        if(googleServicesHelper.isSignedIn(getApplicationContext())) {
            Intent intent = new Intent(ExperimentDetailActivity.this, FolderPickerActivity.class);
            startActivityForResult(intent, FolderPickerActivity.REQUEST_PICK_FOLDER);
        } else {
            Toast.makeText(this, R.string.sign_in_first, Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadToDrive(String folderId) {
        List<String> modules = experimentDetailViewModel.getModules();
        long[] runs = experimentDetailViewModel.getRunIdsForExperiment(currentExperiment.id);
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

        writeDriveFile(folderId, new JsonExperimentFileWriter(currentExperiment.id, database, context));
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

    public void deleteExperiment(MenuItem item) {
        if (!experimentDetailViewModel.hasOngoingRuns(currentExperiment.id)) {
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
        }else{
            Toast.makeText(this, R.string.cancel_run_first, Toast.LENGTH_LONG).show();
        }
    }

    public void duplicateExperiment(MenuItem item) {
        Intent intent = new Intent(ExperimentDetailActivity.this, NewExperimentActivity.class);
        intent.putExtra(NewExperimentActivity.EXTRA_EXPERIMENT_ID, currentExperiment.id);
        startActivity(intent);
    }

//    public void editExperiment(MenuItem item) {
//        Intent intent = new Intent(ExperimentDetailActivity.this, NewExperimentActivity.class);
//        intent.putExtra(NewExperimentActivity.EXTRA_EXPERIMENT_ID, currentExperiment.id);
//        intent.putExtra(NewExperimentActivity.EXTRA_EDIT, true);
//        startActivity(intent);
//    }
}