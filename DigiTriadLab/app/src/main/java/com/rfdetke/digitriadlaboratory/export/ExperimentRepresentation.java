package com.rfdetke.digitriadlaboratory.export;

import com.rfdetke.digitriadlaboratory.database.entities.AdvertiseConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExperimentRepresentation {
    public static final String ACTIVE = "active";
    public static final String INACTIVE = "inactive";
    public static final String WINDOWS = "windows";
    public static final String TX_POWER = "tx_power";
    public static final String INTERVAL = "interval";
    public String codename;
    public String description;

    public Map<String, Long> wifi;
    public Map<String, Long> bluetooth;
    public Map<String, Long> bluetoothLe;
    public Map<String, Long> sensors;
    public Map<String, Long> bluetoothLeAdvertise;

    public List<String> tags;

    public ExperimentRepresentation(Experiment experiment,
                                    WindowConfiguration wifiWindow,
                                    WindowConfiguration bluetoothWindow,
                                    WindowConfiguration bluetoothLeWindow,
                                    WindowConfiguration sensorWindow,
                                    WindowConfiguration advertiseWindow,
                                    AdvertiseConfiguration advertiseConfiguration,
                                    List<String> tags) {
        this.codename = experiment.codename;
        this.description = experiment.description;
        wifi = new HashMap<>();
        bluetooth = new HashMap<>();
        bluetoothLe = new HashMap<>();
        sensors = new HashMap<>();
        bluetoothLeAdvertise = new HashMap<>();
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
        if(advertiseWindow != null) {
            bluetoothLeAdvertise.put(ACTIVE, advertiseWindow.activeTime);
            bluetoothLeAdvertise.put(INACTIVE, advertiseWindow.inactiveTime);
            bluetoothLeAdvertise.put(WINDOWS, advertiseWindow.windows);
            bluetoothLeAdvertise.put(TX_POWER, (long) advertiseConfiguration.txPower);
            bluetoothLeAdvertise.put(INTERVAL, (long) advertiseConfiguration.interval);
        }
        this.tags = tags;
    }
}
