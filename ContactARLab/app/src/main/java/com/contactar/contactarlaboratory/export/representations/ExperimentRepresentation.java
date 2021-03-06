package com.contactar.contactarlaboratory.export.representations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.contactar.contactarlaboratory.database.entities.AdvertiseConfiguration;
import com.contactar.contactarlaboratory.database.entities.Device;
import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.export.json.JsonExportable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExperimentRepresentation implements JsonExportable {
    public static final String ACTIVE = "active";
    public static final String INACTIVE = "inactive";
    public static final String WINDOWS = "windows";
    public static final String TX_POWER = "tx_power";
    public static final String INTERVAL = "interval";
    public static final String CODENAME = "codename";
    public static final String BRAND = "brand";
    public static final String MODEL = "model";
    public static final String UUID = "uuid";
    public String codename;
    public String description;
    public long maxRandomTime;

    public Map<String, Long> wifi;
    public Map<String, Long> bluetooth;
    public Map<String, Long> bluetoothLe;
    public Map<String, Long> sensors;
    public Map<String, Long> cell;
    public Map<String, Long> gps;
    public Map<String, Long> battery;
    public Map<String, Long> activity;
    public Map<String, Long> bluetoothLeAdvertise;
    public Map<String, String> device;

    public List<String> tags;

    public ExperimentRepresentation(Experiment experiment,
                                    WindowConfiguration wifiWindow,
                                    WindowConfiguration bluetoothWindow,
                                    WindowConfiguration bluetoothLeWindow,
                                    WindowConfiguration sensorWindow,
                                    WindowConfiguration cellWindow,
                                    WindowConfiguration gpsWindow,
                                    WindowConfiguration batteryWindow,
                                    WindowConfiguration activityWindow,
                                    WindowConfiguration advertiseWindow,
                                    AdvertiseConfiguration advertiseConfiguration,
                                    List<String> tags,
                                    Device device) {
        this.codename = experiment.codename;
        this.description = experiment.description;
        this.maxRandomTime = experiment.maxRandomTime;

        wifi = new HashMap<>();
        bluetooth = new HashMap<>();
        bluetoothLe = new HashMap<>();
        sensors = new HashMap<>();
        cell = new HashMap<>();
        gps = new HashMap<>();
        battery = new HashMap<>();
        activity = new HashMap<>();
        bluetoothLeAdvertise = new HashMap<>();
        this.device = new HashMap<>();

        if(device!= null) {
            this.device.put(CODENAME, device.codename);
            this.device.put(BRAND, device.brand);
            this.device.put(MODEL, device.model);
            this.device.put(UUID, device.uuid.toString());
        }

        if(wifiWindow != null) {
            wifi.put(ACTIVE, wifiWindow.activeTime);
            wifi.put(INACTIVE, wifiWindow.inactiveTime);
            wifi.put(WINDOWS, wifiWindow.windows);
        }
        if(bluetoothWindow != null) {
            bluetooth.put(ACTIVE, bluetoothWindow.activeTime);
            bluetooth.put(INACTIVE, bluetoothWindow.inactiveTime);
            bluetooth.put(WINDOWS, bluetoothWindow.windows);
        }
        if(bluetoothLeWindow != null) {
            bluetoothLe.put(ACTIVE, bluetoothLeWindow.activeTime);
            bluetoothLe.put(INACTIVE, bluetoothLeWindow.inactiveTime);
            bluetoothLe.put(WINDOWS, bluetoothLeWindow.windows);
        }
        if(sensorWindow != null) {
            sensors.put(ACTIVE, sensorWindow.activeTime);
            sensors.put(INACTIVE, sensorWindow.inactiveTime);
            sensors.put(WINDOWS, sensorWindow.windows);
        }
        if(cellWindow != null) {
            cell.put(ACTIVE, cellWindow.activeTime);
            cell.put(INACTIVE, cellWindow.inactiveTime);
            cell.put(WINDOWS, cellWindow.windows);
        }
        if(gpsWindow != null) {
            gps.put(ACTIVE, gpsWindow.activeTime);
            gps.put(INACTIVE, gpsWindow.inactiveTime);
            gps.put(WINDOWS, gpsWindow.windows);
        }
        if(batteryWindow != null) {
            battery.put(ACTIVE, batteryWindow.activeTime);
            battery.put(INACTIVE, batteryWindow.inactiveTime);
            battery.put(WINDOWS, batteryWindow.windows);
        }
        if(activityWindow != null) {
            activity.put(ACTIVE, activityWindow.activeTime);
            activity.put(INACTIVE, activityWindow.inactiveTime);
            activity.put(WINDOWS, activityWindow.windows);
        }
        if(advertiseWindow != null) {
            bluetoothLeAdvertise.put(ACTIVE, advertiseWindow.activeTime);
            bluetoothLeAdvertise.put(INACTIVE, advertiseWindow.inactiveTime);
            bluetoothLeAdvertise.put(WINDOWS, advertiseWindow.windows);
            bluetoothLeAdvertise.put(TX_POWER, (long) advertiseConfiguration.txPower);
            bluetoothLeAdvertise.put(INTERVAL, (long) advertiseConfiguration.interval);
        }
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExperimentRepresentation that = (ExperimentRepresentation) o;
        return Objects.equals(codename, that.codename) &&
                Objects.equals(description, that.description) &&
                Objects.equals(wifi, that.wifi) &&
                Objects.equals(bluetooth, that.bluetooth) &&
                Objects.equals(bluetoothLe, that.bluetoothLe) &&
                Objects.equals(sensors, that.sensors) &&
                Objects.equals(cell, that.cell) &&
                Objects.equals(gps, that.gps) &&
                Objects.equals(battery, that.battery) &&
                Objects.equals(activity, that.activity) &&
                Objects.equals(bluetoothLeAdvertise, that.bluetoothLeAdvertise) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(device, that.device);
    }

    public static ExperimentRepresentation getCodedExperiment(String jsonExperiment) {
        Gson gson = new Gson();
        return gson.fromJson(jsonExperiment, ExperimentRepresentation.class);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codename, description, wifi, bluetooth, bluetoothLe, sensors, cell, gps, battery, activity, bluetoothLeAdvertise, tags, device);
    }

    @Override
    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this, ExperimentRepresentation.class);
    }
}
