package com.rfdetke.digitriadlaboratory.gapis.drive;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.TeamDriveList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */
public class DriveServiceHelper {
    public static final String MIMETYPE_FOLDER = "application/vnd.google-apps.folder";
    private static final String FOLDERS_LOOKUP = "'%s' in parents and mimeType = '"+MIMETYPE_FOLDER+"'";

    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;

    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }

    public Task<FileList> getFoldersIn(String id) {
        String query = String.format(FOLDERS_LOOKUP, id);
        return Tasks.call(mExecutor, () -> {
            return mDriveService.files().list()
                    .setQ(query)
                    .setIncludeTeamDriveItems(true)
                    .setSupportsTeamDrives(true)
                    .execute();
        });
    }

    public Task<TeamDriveList> getAllTeamDrives() {
        return Tasks.call(mExecutor, () -> mDriveService.teamdrives().list().execute());
    }
}
