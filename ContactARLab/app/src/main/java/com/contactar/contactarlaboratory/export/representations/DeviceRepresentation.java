package com.contactar.contactarlaboratory.export.representations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.contactar.contactarlaboratory.database.entities.Device;
import com.contactar.contactarlaboratory.export.json.JsonExportable;

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
