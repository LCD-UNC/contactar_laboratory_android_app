package com.contactar.contactarlaboratory.gapis.drive;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.TeamDriveList;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */
public class DriveServiceHelper {
    public static final String MIMETYPE_FOLDER = "application/vnd.google-apps.folder";
    public static final String MIMETYPE_TEXT = "text/plain";
    private static final String FOLDERS_LOOKUP = "'%s' in parents and mimeType = '"+MIMETYPE_FOLDER+"'";
    private static final String FILE_LOOKUP = "name = '%s' and '%s' in parents and mimeType = '"+MIMETYPE_TEXT+"'";

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

    public Task<String> createFile(String fileName, String folderId) {
        String query = String.format(FILE_LOOKUP, fileName, folderId);
        return Tasks.call(mExecutor, () -> {
            FileList files = mDriveService.files().list()
                    .setQ(query)
                    .setIncludeTeamDriveItems(true)
                    .setSupportsTeamDrives(true)
                    .execute();
            for(File file : files.getFiles()) {
                mDriveService.files().delete(file.getId()).execute();
            }
            File metadata = new File()
                    .setParents(Collections.singletonList(folderId))
                    .setMimeType("text/plain")
                    .setName(fileName);

            File googleFile = mDriveService.files().create(metadata).setSupportsTeamDrives(true).execute();
            if (googleFile == null) {
                throw new IOException("Null result when requesting file creation.");
            }

            return googleFile.getId();
        });
    }

    public Task<Void> updateFileContent(String fileId, String content) {
        return Tasks.call(mExecutor, () -> {
            File metadata = new File();

            ByteArrayContent contentStream = ByteArrayContent.fromString(MIMETYPE_TEXT, content);

            mDriveService.files().update(fileId, metadata, contentStream).setSupportsTeamDrives(true).execute();
            return null;
        });
    }
}
