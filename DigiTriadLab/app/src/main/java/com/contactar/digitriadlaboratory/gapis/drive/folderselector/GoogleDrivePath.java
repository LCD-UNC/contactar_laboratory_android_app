package com.contactar.digitriadlaboratory.gapis.drive.folderselector;

import com.google.api.services.drive.model.File;
import com.contactar.digitriadlaboratory.gapis.drive.DriveServiceHelper;

import java.util.ArrayList;
import java.util.Locale;

public class GoogleDrivePath {
    private final Drive drive;
    private ArrayList<File> folders;

    public GoogleDrivePath(Drive drive) {
        folders = new ArrayList<>();
        this.drive = drive;
    }

    public String getCurrentFolderId() {
        if(!folders.isEmpty()) {
            return folders.get(folders.size()-1).getId();
        } else {
            return drive.getId();
        }
    }

    public void appendFolder(File folder) throws RuntimeException {
        if(folder.getMimeType().equals(DriveServiceHelper.MIMETYPE_FOLDER)) {
            folders.add(folder);
        } else {
            throw new RuntimeException(String.format(Locale.ENGLISH, "Invalid MimeType: %s", folder.getMimeType()));
        }
    }

    public void removeLast() {
        if(!folders.isEmpty()) {
            folders.remove(folders.size()-1);
        }
    }

    public String getFullPath() {
        String path = java.io.File.separator + drive.getName() + java.io.File.separator;
        for(File folder : folders) {
            path = path.concat(folder.getName()+java.io.File.separator);
        }
        return path;
    }
}
