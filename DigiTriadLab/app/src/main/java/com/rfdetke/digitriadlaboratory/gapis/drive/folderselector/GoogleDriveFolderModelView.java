package com.rfdetke.digitriadlaboratory.gapis.drive.folderselector;

import android.app.Application;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.TeamDrive;
import com.rfdetke.digitriadlaboratory.R;
import com.rfdetke.digitriadlaboratory.gapis.drive.DriveServiceHelper;

import java.util.ArrayList;
import java.util.List;

public class GoogleDriveFolderModelView extends AndroidViewModel {

    private final MutableLiveData<List<Drive>> drivesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<File>> foldersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> path = new MutableLiveData<>();
    private final MutableLiveData<String> currentFolderId = new MutableLiveData<>();
    private GoogleDrivePath drivePath;
    private DriveServiceHelper driveServiceHelper;

    public GoogleDriveFolderModelView(@NonNull Application application) {
        super(application);
    }

    public void setDriveService(DriveServiceHelper driveServiceHelper){
        List<Drive> drives = new ArrayList<>();
        this.driveServiceHelper = driveServiceHelper;
        driveServiceHelper.getAllTeamDrives().addOnSuccessListener(teamDriveList -> {
            drives.add(new Drive(getApplication().getResources().getString(R.string.my_drive), "root"));
            for (TeamDrive drive: teamDriveList.getTeamDrives()) {
                drives.add(new Drive(drive));
            }
            drivesLiveData.postValue(drives);
        }).addOnFailureListener(e -> Toast.makeText(getApplication().getApplicationContext(), R.string.failed_getting_drives, Toast.LENGTH_SHORT).show());
    }

    public LiveData<List<Drive>> getDrives() {
        return drivesLiveData;
    }

    public void setDrive(Drive drive, ProgressBar drivesProgress, ProgressBar folderProgress, RecyclerView folderList) {
        drivesProgress.setVisibility(View.VISIBLE);
        folderProgress.setVisibility(View.VISIBLE);
        folderList.setVisibility(View.INVISIBLE);
        drivePath = new GoogleDrivePath(drive);
        path.setValue(drivePath.getFullPath());
        currentFolderId.setValue(drivePath.getCurrentFolderId());
        updateFolders(folderProgress, folderList);
    }

    public void updateFolders(ProgressBar progress, RecyclerView folderList) {
        driveServiceHelper.getFoldersIn(drivePath.getCurrentFolderId())
                .addOnSuccessListener(fileList -> {
                    foldersLiveData.setValue(fileList.getFiles());
                    progress.setVisibility(View.INVISIBLE);
                    folderList.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplication().getApplicationContext(),
                            R.string.failed_getting_folders, Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                    folderList.setVisibility(View.VISIBLE);
                });
    }

    public void appendFolder(File file, ProgressBar folderProgress, RecyclerView folderList) {
        if (drivePath != null) {
            folderProgress.setVisibility(View.VISIBLE);
            folderList.setVisibility(View.INVISIBLE);
            drivePath.appendFolder(file);
            path.setValue(drivePath.getFullPath());
            currentFolderId.setValue(drivePath.getCurrentFolderId());
            updateFolders(folderProgress, folderList);
        }
    }

    public void removeLastFolder(ProgressBar folderProgress, RecyclerView folderList) {
        if (drivePath != null) {
            folderProgress.setVisibility(View.VISIBLE);
            folderList.setVisibility(View.INVISIBLE);
            drivePath.removeLast();
            path.setValue(drivePath.getFullPath());
            currentFolderId.setValue(drivePath.getCurrentFolderId());
            updateFolders(folderProgress, folderList);
        }
    }

    public LiveData<String> getPath() {
        return path;
    }

    public LiveData<String> getCurrentFolderId() {
        return currentFolderId;
    }

    public LiveData<List<File>> getFolders() {
        return foldersLiveData;
    }
}
