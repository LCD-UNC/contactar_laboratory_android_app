package com.rfdetke.digitriadlaboratory.gapis.drive.folderselector;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.services.drive.model.File;
import com.rfdetke.digitriadlaboratory.R;
import com.rfdetke.digitriadlaboratory.gapis.GoogleSessionAppCompatActivity;
import com.rfdetke.digitriadlaboratory.gapis.drive.folderselector.FolderListAdapter.FolderViewHolder;

import java.util.List;

public class FolderPickerActivity extends GoogleSessionAppCompatActivity {
    public static final int REQUEST_PICK_FOLDER = 7;
    public static final String EXTRA_FOLDER_PATH = "com.rfdetke.digitriadlaboratory.FOLDER_PATH";
    public static final String EXTRA_FOLDER_ID = "com.rfdetke.digitriadlaboratory.FOLDER_ID";

    private Toolbar topToolbar;
    private FolderListAdapter adapter;
    private String selectedFolderId;
    private String selectedFolderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_folder_picker);
        super.onCreate(savedInstanceState);

        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);

        GoogleDriveFolderModelView folderModelView = new ViewModelProvider(this).get(GoogleDriveFolderModelView.class);
        folderModelView.setDriveService(googleServicesHelper.getDriveService(getApplicationContext()));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new FolderListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RadioGroup drivesRadioGroup = findViewById(R.id.drives_radio_group);
        TextView selectedPath = findViewById(R.id.selected_path);

        ProgressBar drivesProgress = findViewById(R.id.drives_progress_bar);
        ProgressBar foldersProgress = findViewById(R.id.folders_progress_bar);

        folderModelView.getDrives().observe(this, drives -> {
            for (int i = 0; i < drives.size(); i++) {
                Drive drive = drives.get(i);
                RadioButton rb = new RadioButton(getApplicationContext());
                rb.setText(drive.getName());
                rb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(isChecked) {
                        folderModelView.setDrive(drive, drivesProgress, foldersProgress, recyclerView);
                    }
                });
                rb.setButtonTintList(getColorStateList(R.color.colorAccent));
                drivesRadioGroup.addView(rb);
            }
            drivesProgress.setVisibility(View.INVISIBLE);
        });

        folderModelView.getFolders().observe(this, files -> {
            adapter.setFolders(files);
            foldersProgress.setVisibility(View.INVISIBLE);
        });

        folderModelView.getPath().observe(this, s -> {
            selectedPath.setText(s);
            selectedFolderPath = s;
            drivesProgress.setVisibility(View.INVISIBLE);
        });

        folderModelView.getCurrentFolderId().observe(this, s -> selectedFolderId = s);

        recyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(() -> {
                    for (int i = 0; i < recyclerView.getChildCount(); i++) {
                        FolderViewHolder holder = (FolderViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                        holder.parentLayout.setOnClickListener(v -> folderModelView.appendFolder(holder.folder, foldersProgress, recyclerView));
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.folder_picker, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClickListener(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                return true;

            case R.id.new_experiment:
                createNewFolder();
                return true;

            default:
                return false;
        }
    }

    public void confirmPath(MenuItem menuItem) {
        if(selectedFolderId!=null && selectedFolderPath!= null) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_FOLDER_PATH, selectedFolderPath);
            intent.putExtra(EXTRA_FOLDER_ID, selectedFolderId);
            setResult(REQUEST_PICK_FOLDER, intent);
            finish();
        } else {
            Toast.makeText(this, R.string.pick_folder_message, Toast.LENGTH_SHORT).show();
        }
    }

    private void createNewFolder() {
        //TODO: Implementar para crear carpeta en el Path indicado. Cambiar mensaje del Toast!
        Toast.makeText(this, R.string.not_implemented_yet, Toast.LENGTH_SHORT).show();
    }
}