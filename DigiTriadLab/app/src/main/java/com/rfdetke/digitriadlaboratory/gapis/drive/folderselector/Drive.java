package com.rfdetke.digitriadlaboratory.gapis.drive.folderselector;

import com.google.api.services.drive.model.TeamDrive;

public class Drive {
    private final String name;
    private final String id;

    public Drive(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Drive(TeamDrive drive) {
        name = drive.getName();
        id = drive.getId();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

}
