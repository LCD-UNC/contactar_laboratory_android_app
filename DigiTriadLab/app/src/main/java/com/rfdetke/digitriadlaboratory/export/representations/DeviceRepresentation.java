package com.rfdetke.digitriadlaboratory.export.representations;

import android.os.ParcelUuid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.export.json.JsonExportable;

public class DeviceRepresentation implements JsonExportable {

    public String codename;
    public String brand;
    public String model;
    public String uuid;

    public DeviceRepresentation(Device device) {
        this.codename = device.codename;
        this.brand = device.brand;
        this.model = device.model;
        this.uuid = device.uuid.toString();
    }

    @Override
    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this, DeviceRepresentation.class);
    }

    public static DeviceRepresentation fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, DeviceRepresentation.class);
    }
}
