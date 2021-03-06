package com.contactar.contactarlaboratory.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.le.AdvertisingSetParameters;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.contactar.contactarlaboratory.R;
import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.DatabaseSingleton;
import com.contactar.contactarlaboratory.database.entities.AdvertiseConfiguration;
import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.repositories.ConfigurationRepository;
import com.contactar.contactarlaboratory.repositories.DeviceRepository;
import com.contactar.contactarlaboratory.repositories.ExperimentRepository;
import com.contactar.contactarlaboratory.repositories.SourceTypeRepository;
import com.contactar.contactarlaboratory.export.representations.ExperimentRepresentation;
import com.contactar.contactarlaboratory.views.modelviews.ExperimentDetailViewModel;

import java.util.List;
import java.util.Locale;

public class NewExperimentActivity extends AppCompatActivity {

    public static final int SCAN_QR = 10;
    public static final String EXTRA_EXPERIMENT_ID = "com.contactar.contactarlaboratory.EXPERIMENT_ID";
    public static final String EXTRA_EDIT = "com.contactar.contactarlaboratory.EDIT";

    private ExperimentRepository experimentRepository;
    private DeviceRepository deviceRepository;
    private SourceTypeRepository sourceTypeRepository;
    private ConfigurationRepository configurationRepository;

    private EditText codename;
    private EditText description;
    private EditText randomTime;

    private TextView wifiTitle;
    private EditText wifiActive;
    private EditText wifiInactive;
    private EditText wifiWindows;
    private SwitchCompat wifiSwitch;

    private TextView bluetoothTitle;
    private EditText bluetoothActive;
    private EditText bluetoothInactive;
    private EditText bluetoothWindows;
    private SwitchCompat bluetoothSwitch;

    private TextView bluetoothLeTitle;
    private EditText bluetoothLeActive;
    private EditText bluetoothLeInactive;
    private EditText bluetoothLeWindows;
    private SwitchCompat bluetoothLeSwitch;

    private TextView sensorsTitle;
    private EditText sensorsActive;
    private EditText sensorsInactive;
    private EditText sensorsWindows;
    private SwitchCompat sensorsSwitch;

    private TextView cellTitle;
    private EditText cellActive;
    private EditText cellInactive;
    private EditText cellWindows;
    private SwitchCompat cellSwitch;

    private TextView gpsTitle;
    private EditText gpsActive;
    private EditText gpsInactive;
    private EditText gpsWindows;
    private SwitchCompat gpsSwitch;

    private TextView batteryTitle;
    private EditText batteryActive;
    private EditText batteryInactive;
    private EditText batteryWindows;
    private SwitchCompat batterySwitch;

    private TextView activityTitle;
    private EditText activityActive;
    private EditText activityInactive;
    private EditText activityWindows;
    private SwitchCompat activitySwitch;

    private TextView bluetoothLeAdvertiseTitle;
    private EditText bluetoothLeAdvertiseActive;
    private EditText bluetoothLeAdvertiseInactive;
    private EditText bluetoothLeAdvertiseWindows;
    private EditText bluetoothLeAdvertiseTxPower;
    private EditText bluetoothLeAdvertiseInterval;
    private SwitchCompat bluetoothLeAdvertiseSwitch;
    private ChipGroup tags;
    private Toolbar topToolbar;
    private ExperimentDetailViewModel experimentDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_experiment);

        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);
        topToolbar.setTitle(getString(R.string.new_experiment));

        AppDatabase database = DatabaseSingleton.getInstance(getApplicationContext());
        experimentRepository = new ExperimentRepository(database);
        deviceRepository = new DeviceRepository(database);
        sourceTypeRepository = new SourceTypeRepository(database);
        configurationRepository = new ConfigurationRepository(database);

        codename = findViewById(R.id.experiment_codename);
        description = findViewById(R.id.experiment_description);
        randomTime = findViewById(R.id.random_initial_time);

        wifiActive = findViewById(R.id.wifi_active);
        wifiInactive = findViewById(R.id.wifi_inactive);
        wifiWindows = findViewById(R.id.wifi_windows);
        wifiTitle = findViewById(R.id.wifi_title);
        wifiSwitch = findViewById(R.id.wifi_switch);

        bluetoothActive = findViewById(R.id.bluetooth_active);
        bluetoothInactive = findViewById(R.id.bluetooth_inactive);
        bluetoothWindows = findViewById(R.id.bluetooth_windows);
        bluetoothTitle = findViewById(R.id.bluetooth_title);
        bluetoothSwitch = findViewById(R.id.bluetooth_switch);

        bluetoothLeActive = findViewById(R.id.bluetooth_le_active);
        bluetoothLeInactive = findViewById(R.id.bluetooth_le_inactive);
        bluetoothLeWindows = findViewById(R.id.bluetooth_le_windows);
        bluetoothLeTitle = findViewById(R.id.bluetooth_le_title);
        bluetoothLeSwitch = findViewById(R.id.bluetooth_le_switch);

        sensorsActive = findViewById(R.id.sensors_active);
        sensorsInactive = findViewById(R.id.sensors_inactive);
        sensorsWindows = findViewById(R.id.sensors_windows);
        sensorsTitle = findViewById(R.id.sensors_title);
        sensorsSwitch = findViewById(R.id.sensors_switch);

        cellActive = findViewById(R.id.cell_active);
        cellInactive = findViewById(R.id.cell_inactive);
        cellWindows = findViewById(R.id.cell_windows);
        cellTitle = findViewById(R.id.cell_title);
        cellSwitch = findViewById(R.id.cell_switch);

        gpsActive = findViewById(R.id.gps_active);
        gpsInactive = findViewById(R.id.gps_inactive);
        gpsWindows = findViewById(R.id.gps_windows);
        gpsTitle = findViewById(R.id.gps_title);
        gpsSwitch = findViewById(R.id.gps_switch);

        batteryActive = findViewById(R.id.battery_active);
        batteryInactive = findViewById(R.id.battery_inactive);
        batteryWindows = findViewById(R.id.battery_windows);
        batteryTitle = findViewById(R.id.battery_title);
        batterySwitch = findViewById(R.id.battery_switch);

        activityTitle = findViewById(R.id.activity_title);
        activityActive = findViewById(R.id.activity_active);
        activityInactive = findViewById(R.id.activity_inactive);
        activityWindows = findViewById(R.id.activity_windows);
        activityTitle = findViewById(R.id.activity_title);
        activitySwitch = findViewById(R.id.activity_switch);


        bluetoothLeAdvertiseActive = findViewById(R.id.bluetooth_le_advertise_active);
        bluetoothLeAdvertiseInactive = findViewById(R.id.bluetooth_le_advertise_inactive);
        bluetoothLeAdvertiseWindows = findViewById(R.id.bluetooth_le_advertise_windows);
        bluetoothLeAdvertiseTitle = findViewById(R.id.bluetooth_le_advertise_title);
        bluetoothLeAdvertiseTxPower = findViewById(R.id.bluetooth_le_advertise_tx_power);
        bluetoothLeAdvertiseInterval = findViewById(R.id.bluetooth_le_advertise_interval);
        bluetoothLeAdvertiseSwitch = findViewById(R.id.bluetooth_le_advertise_switch);

        List<String> tagsList = experimentRepository.getTagList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, tagsList);

        AutoCompleteTextView textView = findViewById(R.id.add_tag);
        tags = findViewById(R.id.tags);
        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> {
            String value = textView.getText().toString();
            if(!value.isEmpty()) {
                Chip chip = new Chip(this);
                chip.setText(value.toUpperCase());
                chip.setCloseIconVisible(true);
                chip.setClickable(true);
                tags.addView(chip);
                chip.setOnCloseIconClickListener(tags::removeView);
            }
        });

        textView.setOnItemClickListener((parent, view, position, id) -> {
            Chip chip = new Chip(this);
            chip.setText(((TextView)view).getText().toString().toUpperCase());
            chip.setCloseIconVisible(true);
            chip.setClickable(true);
            tags.addView(chip);
            chip.setOnCloseIconClickListener(tags::removeView);
        });
        textView.setThreshold(1);
        textView.setAdapter(adapter);
        
        Button saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(v -> {
            long experimentId = 0;
            boolean success = true;
            String errorMessage = "";
            if(validateExperiment()) {
                experimentId = saveExperiment();
                if(!wifiSwitch.isChecked() && !bluetoothSwitch.isChecked() &&
                        !bluetoothLeSwitch.isChecked() && !bluetoothLeAdvertiseSwitch.isChecked() &&
                        !sensorsSwitch.isChecked()&&
                        !cellSwitch.isChecked() &&
                        !gpsSwitch.isChecked() &&
                        !batterySwitch.isChecked() &&
                        !activitySwitch.isChecked()){
                    errorMessage = errorMessage.concat(getResources().getString(R.string.at_least_one_configuration));
                    success = false;
                } else {
                    if (wifiSwitch.isChecked()) {
                        if (validateWifiConfig()) {
                            saveWifiConfig(experimentId);
                        } else {
                            errorMessage = errorMessage.concat(getResources().getString(R.string.wifi_error));
                            success = false;
                        }
                    }
                    if (bluetoothSwitch.isChecked()) {
                        if (validateBluetoothConfig()) {
                            saveBluetoothConfig(experimentId);
                        } else {
                            errorMessage = errorMessage.concat(getResources().getString(R.string.bluetooth_error));
                            success = false;
                        }
                    }
                    if (bluetoothLeSwitch.isChecked()) {
                        if (validateBluetoothLeConfig()) {
                            saveBluetoothLeConfig(experimentId);
                        } else {
                            errorMessage = errorMessage.concat(getResources().getString(R.string.bluetooth_le_error));
                            success = false;
                        }
                    }
                    if(sensorsSwitch.isChecked()) {
                        if (validateSensorsConfig()) {
                            saveSensorsConfig(experimentId);
                        } else {
                            errorMessage = errorMessage.concat(getResources().getString(R.string.sensors_error));
                            success = false;
                        }
                    }
                    if(cellSwitch.isChecked()) {
                        if (validateCellConfig()) {
                            saveCellConfig(experimentId);
                        } else {
                            errorMessage = errorMessage.concat(getResources().getString(R.string.cell_error));
                            success = false;
                        }
                    }
                    if(gpsSwitch.isChecked()) {
                        if (validateGpsConfig()) {
                            saveGpsConfig(experimentId);
                        } else {
                            errorMessage = errorMessage.concat(getResources().getString(R.string.gps_error));
                            success = false;
                        }
                    }
                    if(batterySwitch.isChecked()) {
                        if (validateBatteryConfig()) {
                            saveBatteryConfig(experimentId);
                        } else {
                            errorMessage = errorMessage.concat(getResources().getString(R.string.battery_error));
                            success = false;
                        }
                    }
                    if(activitySwitch.isChecked()) {
                        if (validateActivityConfig()) {
                            saveActivityConfig(experimentId);
                        } else {
                            errorMessage = errorMessage.concat(getResources().getString(R.string.activity_error));
                            success = false;
                        }
                    }
                    if(bluetoothLeAdvertiseSwitch.isChecked()) {
                        if (validateBluetoothLeAdvertiseConfig()) {
                            saveBluetoothLeAdvertiseConfig(experimentId);
                        } else {
                            errorMessage = errorMessage.concat(getResources().getString(R.string.bluetooth_le_advertise_error));
                            success = false;
                        }
                    }
                    saveTags(experimentId);
                }
            } else {
                errorMessage = errorMessage.concat(getResources().getString(R.string.experiment_codename_error));
                success = false;
            }

            if(success) {
                finish();
            } else {
                if(experimentId!=0) {
                    Experiment experiment = experimentRepository.getById(experimentId);
                    experimentRepository.delete(experiment);
                }
                DialogFragment newFragment = new FieldErrorDialogFragment(errorMessage);
                newFragment.setCancelable(false);
                newFragment.show(getSupportFragmentManager(), "field_error");
            }

        });

        wifiSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            wifiActive.setEnabled(isChecked);
            wifiInactive.setEnabled(isChecked);
            wifiWindows.setEnabled(isChecked);
            int color;
            color = isChecked ? R.color.black : R.color.grey;
            wifiTitle.setTextColor(getColor(color));
        });

        bluetoothSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bluetoothActive.setEnabled(isChecked);
            bluetoothInactive.setEnabled(isChecked);
            bluetoothWindows.setEnabled(isChecked);
            int color;
            color = isChecked ? R.color.black : R.color.grey;
            bluetoothTitle.setTextColor(getColor(color));
        });

        bluetoothLeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bluetoothLeActive.setEnabled(isChecked);
            bluetoothLeInactive.setEnabled(isChecked);
            bluetoothLeWindows.setEnabled(isChecked);
            int color;
            color = isChecked ? R.color.black : R.color.grey;
            bluetoothLeTitle.setTextColor(getColor(color));
        });

        sensorsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sensorsActive.setEnabled(isChecked);
            sensorsInactive.setEnabled(isChecked);
            sensorsWindows.setEnabled(isChecked);
            int color;
            color = isChecked ? R.color.black : R.color.grey;
            sensorsTitle.setTextColor(getColor(color));
        });

        cellSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cellActive.setEnabled(isChecked);
            cellInactive.setEnabled(isChecked);
            cellWindows.setEnabled(isChecked);
            int color;
            color = isChecked ? R.color.black : R.color.grey;
            cellTitle.setTextColor(getColor(color));
        });

        gpsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            gpsActive.setEnabled(isChecked);
            gpsInactive.setEnabled(isChecked);
            gpsWindows.setEnabled(isChecked);
            int color;
            color = isChecked ? R.color.black : R.color.grey;
            gpsTitle.setTextColor(getColor(color));
        });

        batterySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            batteryActive.setEnabled(isChecked);
            batteryInactive.setEnabled(isChecked);
            batteryWindows.setEnabled(isChecked);
            int color;
            color = isChecked ? R.color.black : R.color.grey;
            batteryTitle.setTextColor(getColor(color));
        });

        activitySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activityActive.setEnabled(isChecked);
            activityInactive.setEnabled(isChecked);
            activityWindows.setEnabled(isChecked);
            int color;
            color = isChecked ? R.color.black : R.color.grey;
            activityTitle.setTextColor(getColor(color));
        });

        bluetoothLeAdvertiseSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bluetoothLeAdvertiseActive.setEnabled(isChecked);
            bluetoothLeAdvertiseInactive.setEnabled(isChecked);
            bluetoothLeAdvertiseWindows.setEnabled(isChecked);
            bluetoothLeAdvertiseTxPower.setEnabled(isChecked);
            bluetoothLeAdvertiseInterval.setEnabled(isChecked);
            int color;
            color = isChecked ? R.color.black : R.color.grey;
            bluetoothLeAdvertiseTitle.setTextColor(getColor(color));
        });

        wifiSwitch.setChecked(false);
        bluetoothSwitch.setChecked(false);
        bluetoothLeSwitch.setChecked(false);
        sensorsSwitch.setChecked(false);
        cellSwitch.setChecked(false);
        gpsSwitch.setChecked(false);
        batterySwitch.setChecked(false);
        activitySwitch.setChecked(false);
        bluetoothLeAdvertiseSwitch.setChecked(false);

        long experimentId = getIntent().getLongExtra(EXTRA_EXPERIMENT_ID, 0);
        if(experimentId != 0) {
            experimentDetailViewModel = new ViewModelProvider(this).get(ExperimentDetailViewModel.class);
            experimentDetailViewModel.setExperiment(experimentId);
            ExperimentRepresentation experiment = experimentDetailViewModel.getExperimentRepresentation();
            fillFieldsWith(experiment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.new_experiment_menu, menu);
        return true;
    }

    private void saveTags(long experimentId) {
        for(int i=0, count=tags.getChildCount(); i<count; i++) {
            String tagValue = ((Chip)tags.getChildAt(i)).getText().toString();
            long tagId = experimentRepository.insertTag(tagValue);
            if (tagId == -1) {
                tagId = experimentRepository.getTagId(tagValue);
            }
            experimentRepository.relateToTag(experimentId, tagId);
        }
    }

    private boolean validateBluetoothLeAdvertiseConfig() {
        return !bluetoothLeAdvertiseActive.getText().toString().isEmpty() &&
                !bluetoothLeAdvertiseInactive.getText().toString().isEmpty() &&
                !bluetoothLeAdvertiseTxPower.getText().toString().isEmpty() &&
                (Integer.parseInt(bluetoothLeAdvertiseActive.getText().toString()) > 0) &&
                (Integer.parseInt(bluetoothLeAdvertiseInactive.getText().toString()) > 0) &&
                (Integer.parseInt(bluetoothLeAdvertiseWindows.getText().toString()) > 0) &&
                (Integer.parseInt(bluetoothLeAdvertiseTxPower.getText().toString()) >= AdvertisingSetParameters.TX_POWER_MIN) &&
                (Integer.parseInt(bluetoothLeAdvertiseTxPower.getText().toString()) <= AdvertisingSetParameters.TX_POWER_MAX) &&
                !bluetoothLeAdvertiseInterval.getText().toString().isEmpty() &&
                (Long.parseLong(bluetoothLeAdvertiseInterval.getText().toString()) >= 100) &&
                (Long.parseLong(bluetoothLeAdvertiseInterval.getText().toString()) <= 10485759) &&
                !bluetoothLeAdvertiseWindows.getText().toString().isEmpty();
    }

    private boolean validateExperiment(){
        return !codename.getText().toString().isEmpty();
    }

    private boolean validateWifiConfig() {
        return  !wifiActive.getText().toString().isEmpty() &&
                !wifiInactive.getText().toString().isEmpty() &&
                !wifiWindows.getText().toString().isEmpty() &&
                (Integer.parseInt(wifiActive.getText().toString()) > 0) &&
                (Integer.parseInt(wifiInactive.getText().toString()) > 0) &&
                (Integer.parseInt(wifiWindows.getText().toString()) > 0);
    }

    private boolean validateBluetoothConfig() {
        return  !bluetoothActive.getText().toString().isEmpty() &&
                !bluetoothInactive.getText().toString().isEmpty() &&
                !bluetoothWindows.getText().toString().isEmpty() &&
                (Integer.parseInt(bluetoothActive.getText().toString()) > 0) &&
                (Integer.parseInt(bluetoothInactive.getText().toString()) > 0) &&
                (Integer.parseInt(bluetoothWindows.getText().toString()) > 0);
    }

    private boolean validateBluetoothLeConfig() {
        return  !bluetoothLeActive.getText().toString().isEmpty() &&
                !bluetoothLeInactive.getText().toString().isEmpty() &&
                !bluetoothLeWindows.getText().toString().isEmpty() &&
                (Integer.parseInt(bluetoothLeActive.getText().toString()) > 0) &&
                (Integer.parseInt(bluetoothLeInactive.getText().toString()) > 0) &&
                (Integer.parseInt(bluetoothLeWindows.getText().toString()) > 0);
    }

    private boolean validateSensorsConfig() {
        return !sensorsActive.getText().toString().isEmpty() &&
                !sensorsInactive.getText().toString().isEmpty() &&
                !sensorsWindows.getText().toString().isEmpty() &&
                (Integer.parseInt(sensorsActive.getText().toString()) > 0) &&
                (Integer.parseInt(sensorsInactive.getText().toString()) > 0) &&
                (Integer.parseInt(sensorsWindows.getText().toString()) > 0);
    }

    private boolean validateCellConfig() {
        return !cellActive.getText().toString().isEmpty() &&
                !cellInactive.getText().toString().isEmpty() &&
                !cellWindows.getText().toString().isEmpty() &&
                (Integer.parseInt(cellActive.getText().toString()) > 0) &&
                (Integer.parseInt(cellInactive.getText().toString()) > 0) &&
                (Integer.parseInt(cellWindows.getText().toString()) > 0);
    }

    private boolean validateGpsConfig() {
        return !gpsActive.getText().toString().isEmpty() &&
                !gpsInactive.getText().toString().isEmpty() &&
                !gpsWindows.getText().toString().isEmpty() &&
                (Integer.parseInt(gpsActive.getText().toString()) > 0) &&
                (Integer.parseInt(gpsInactive.getText().toString()) > 0) &&
                (Integer.parseInt(gpsWindows.getText().toString()) > 0);
    }

    private boolean validateBatteryConfig() {
        return !batteryActive.getText().toString().isEmpty() &&
                !batteryInactive.getText().toString().isEmpty() &&
                !batteryWindows.getText().toString().isEmpty() &&
                (Integer.parseInt(batteryActive.getText().toString()) > 0) &&
                (Integer.parseInt(batteryInactive.getText().toString()) > 0) &&
                (Integer.parseInt(batteryWindows.getText().toString()) > 0);
    }
    private boolean validateActivityConfig() {
        return !activityActive.getText().toString().isEmpty() &&
                !activityInactive.getText().toString().isEmpty() &&
                !activityWindows.getText().toString().isEmpty() &&
                (Integer.parseInt(activityActive.getText().toString()) > 0) &&
                (Integer.parseInt(activityInactive.getText().toString()) > 0) &&
                (Integer.parseInt(activityWindows.getText().toString()) > 0);
    }
    private long saveExperiment() {
        int randTime;
        if (randomTime.getText().toString().isEmpty())
            randTime = 0;
        else
            randTime = Integer.parseInt(randomTime.getText().toString());
        return experimentRepository.insert( new Experiment(codename.getText().toString(),
                                                description.getText().toString(),
                                                deviceRepository.getDevice().id,
                                                randTime));
    }

    private void saveBluetoothLeConfig(long experimentId) {
        long active = Long.parseLong(bluetoothLeActive.getText().toString());
        long inactive = Long.parseLong(bluetoothLeInactive.getText().toString());
        long windows = Long.parseLong(bluetoothLeWindows.getText().toString());
        long sourceId = sourceTypeRepository.getByType(SourceTypeEnum.BLUETOOTH_LE.name()).id;
        WindowConfiguration configuration = new WindowConfiguration(active,
                inactive, windows, sourceId, experimentId);
        configurationRepository.insert(configuration);
    }

    private void saveBluetoothConfig(long experimentId) {
        long active = Long.parseLong(bluetoothActive.getText().toString());
        long inactive = Long.parseLong(bluetoothInactive.getText().toString());
        long windows = Long.parseLong(bluetoothWindows.getText().toString());
        long sourceId = sourceTypeRepository.getByType(SourceTypeEnum.BLUETOOTH.name()).id;
        WindowConfiguration configuration = new WindowConfiguration(active,
                inactive, windows, sourceId, experimentId);
        configurationRepository.insert(configuration);
    }

    private void saveWifiConfig(long experimentId) {
        long active = Long.parseLong(wifiActive.getText().toString());
        long inactive = Long.parseLong(wifiInactive.getText().toString());
        long windows = Long.parseLong(wifiWindows.getText().toString());
        long sourceId = sourceTypeRepository.getByType(SourceTypeEnum.WIFI.name()).id;
        WindowConfiguration configuration = new WindowConfiguration(active,
                inactive, windows, sourceId, experimentId);
        configurationRepository.insert(configuration);
    }

    private void saveSensorsConfig(long experimentId) {
        long active = Long.parseLong(sensorsActive.getText().toString());
        long inactive = Long.parseLong(sensorsInactive.getText().toString());
        long windows = Long.parseLong(sensorsWindows.getText().toString());
        long sourceId = sourceTypeRepository.getByType(SourceTypeEnum.SENSORS.name()).id;
        WindowConfiguration configuration = new WindowConfiguration(active,
                inactive, windows, sourceId, experimentId);
        configurationRepository.insert(configuration);
    }

    private void saveCellConfig(long experimentId) {
        long active = Long.parseLong(cellActive.getText().toString());
        long inactive = Long.parseLong(cellInactive.getText().toString());
        long windows = Long.parseLong(cellWindows.getText().toString());
        long sourceId = sourceTypeRepository.getByType(SourceTypeEnum.CELL.name()).id;
        WindowConfiguration configuration = new WindowConfiguration(active,
                inactive, windows, sourceId, experimentId);
        configurationRepository.insert(configuration);
    }

    private void saveGpsConfig(long experimentId) {
        long active = Long.parseLong(gpsActive.getText().toString());
        long inactive = Long.parseLong(gpsInactive.getText().toString());
        long windows = Long.parseLong(gpsWindows.getText().toString());
        long sourceId = sourceTypeRepository.getByType(SourceTypeEnum.GPS.name()).id;
        WindowConfiguration configuration = new WindowConfiguration(active,
                inactive, windows, sourceId, experimentId);
        configurationRepository.insert(configuration);
    }

    private void saveBatteryConfig(long experimentId) {
        long active = Long.parseLong(batteryActive.getText().toString());
        long inactive = Long.parseLong(batteryInactive.getText().toString());
        long windows = Long.parseLong(batteryWindows.getText().toString());
        long sourceId = sourceTypeRepository.getByType(SourceTypeEnum.BATTERY.name()).id;
        WindowConfiguration configuration = new WindowConfiguration(active,
                inactive, windows, sourceId, experimentId);
        configurationRepository.insert(configuration);
    }

    private void saveActivityConfig(long experimentId) {
        long active = Long.parseLong(activityActive.getText().toString());
        long inactive = Long.parseLong(activityInactive.getText().toString());
        long windows = Long.parseLong(activityWindows.getText().toString());
        long sourceId = sourceTypeRepository.getByType(SourceTypeEnum.ACTIVITY.name()).id;
        WindowConfiguration configuration = new WindowConfiguration(active,
                inactive, windows, sourceId, experimentId);
        configurationRepository.insert(configuration);
    }

    private void saveBluetoothLeAdvertiseConfig(long experimentId) {
        long active = Long.parseLong(bluetoothLeAdvertiseActive.getText().toString());
        long inactive = Long.parseLong(bluetoothLeAdvertiseInactive.getText().toString());
        long windows = Long.parseLong(bluetoothLeAdvertiseWindows.getText().toString());
        int txPower = Integer.parseInt(bluetoothLeAdvertiseTxPower.getText().toString());
        int interval = Integer.parseInt(bluetoothLeAdvertiseInterval.getText().toString());
        long sourceId = sourceTypeRepository.getByType(SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name()).id;
        WindowConfiguration configuration = new WindowConfiguration(active,
                inactive, windows, sourceId, experimentId);
        AdvertiseConfiguration advertiseConfiguration = new AdvertiseConfiguration(txPower, interval, experimentId);
        configurationRepository.insert(configuration);
        configurationRepository.insertAdvertise(advertiseConfiguration);
    }

    public void scanQrCode(MenuItem item) {
        Intent intent = new Intent(NewExperimentActivity.this, ScanQrExperiment.class);
        startActivityForResult(intent, SCAN_QR);
    }

    public static class FieldErrorDialogFragment extends DialogFragment {

        String message;

        public FieldErrorDialogFragment(String message) {
            super();
            this.message = message;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(this.message)
                    .setNeutralButton(R.string.ok, (dialog, id) -> {});
            return builder.create();
        }
    }
    private String longOrNullToEmpty(Long value) {
        if (value == null) {
            return "";
        } else {
            return String.format(Locale.ENGLISH, "%d", value);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_QR && resultCode == Activity.RESULT_OK) {
            if(data != null) {
                String experimentStr = data.getStringExtra(ScanQrExperiment.EXTRA_CONFIG_STRING);
                ExperimentRepresentation experiment = ExperimentRepresentation.getCodedExperiment(experimentStr);
                fillFieldsWith(experiment);
            }
        }
    }

    private void fillFieldsWith(ExperimentRepresentation experiment) {
        if (experiment != null) {
            codename.setText(experiment.codename);
            description.setText(experiment.description);
            randomTime.setText(String.format(Locale.ENGLISH, "%d", experiment.maxRandomTime));

            if(!experiment.wifi.isEmpty()) {
                wifiSwitch.setChecked(true);
                wifiActive.setText(longOrNullToEmpty(experiment.wifi.get(ExperimentRepresentation.ACTIVE)));
                wifiInactive.setText(longOrNullToEmpty(experiment.wifi.get(ExperimentRepresentation.INACTIVE)));
                wifiWindows.setText(longOrNullToEmpty(experiment.wifi.get(ExperimentRepresentation.WINDOWS)));
            }

            if(!experiment.bluetooth.isEmpty()) {
                bluetoothSwitch.setChecked(true);
                bluetoothActive.setText(longOrNullToEmpty(experiment.bluetooth.get(ExperimentRepresentation.ACTIVE)));
                bluetoothInactive.setText(longOrNullToEmpty(experiment.bluetooth.get(ExperimentRepresentation.INACTIVE)));
                bluetoothWindows.setText(longOrNullToEmpty(experiment.bluetooth.get(ExperimentRepresentation.WINDOWS)));
            }

            if(!experiment.bluetoothLe.isEmpty()) {
                bluetoothLeSwitch.setChecked(true);
                bluetoothLeActive.setText(longOrNullToEmpty(experiment.bluetoothLe.get(ExperimentRepresentation.ACTIVE)));
                bluetoothLeInactive.setText(longOrNullToEmpty(experiment.bluetoothLe.get(ExperimentRepresentation.INACTIVE)));
                bluetoothLeWindows.setText(longOrNullToEmpty(experiment.bluetoothLe.get(ExperimentRepresentation.WINDOWS)));
            }

            if(!experiment.sensors.isEmpty()) {
                sensorsSwitch.setChecked(true);
                sensorsActive.setText(longOrNullToEmpty(experiment.sensors.get(ExperimentRepresentation.ACTIVE)));
                sensorsInactive.setText(longOrNullToEmpty(experiment.sensors.get(ExperimentRepresentation.INACTIVE)));
                sensorsWindows.setText(longOrNullToEmpty(experiment.sensors.get(ExperimentRepresentation.WINDOWS)));
            }

            if(!experiment.cell.isEmpty()) {
                cellSwitch.setChecked(true);
                cellActive.setText(longOrNullToEmpty(experiment.cell.get(ExperimentRepresentation.ACTIVE)));
                cellInactive.setText(longOrNullToEmpty(experiment.cell.get(ExperimentRepresentation.INACTIVE)));
                cellWindows.setText(longOrNullToEmpty(experiment.cell.get(ExperimentRepresentation.WINDOWS)));
            }

            if(!experiment.gps.isEmpty()) {
                gpsSwitch.setChecked(true);
                gpsActive.setText(longOrNullToEmpty(experiment.gps.get(ExperimentRepresentation.ACTIVE)));
                gpsInactive.setText(longOrNullToEmpty(experiment.gps.get(ExperimentRepresentation.INACTIVE)));
                gpsWindows.setText(longOrNullToEmpty(experiment.gps.get(ExperimentRepresentation.WINDOWS)));
            }

            if(!experiment.battery.isEmpty()) {
                batterySwitch.setChecked(true);
                batteryActive.setText(longOrNullToEmpty(experiment.battery.get(ExperimentRepresentation.ACTIVE)));
                batteryInactive.setText(longOrNullToEmpty(experiment.battery.get(ExperimentRepresentation.INACTIVE)));
                batteryWindows.setText(longOrNullToEmpty(experiment.battery.get(ExperimentRepresentation.WINDOWS)));
            }

            if(!experiment.activity.isEmpty()) {
                activitySwitch.setChecked(true);
                activityActive.setText(longOrNullToEmpty(experiment.activity.get(ExperimentRepresentation.ACTIVE)));
                activityInactive.setText(longOrNullToEmpty(experiment.activity.get(ExperimentRepresentation.INACTIVE)));
                activityWindows.setText(longOrNullToEmpty(experiment.activity.get(ExperimentRepresentation.WINDOWS)));
            }

            if(!experiment.bluetoothLeAdvertise.isEmpty()) {
                bluetoothLeAdvertiseSwitch.setChecked(true);
                bluetoothLeAdvertiseActive.setText(longOrNullToEmpty(experiment.bluetoothLeAdvertise.get(ExperimentRepresentation.ACTIVE)));
                bluetoothLeAdvertiseInactive.setText(longOrNullToEmpty(experiment.bluetoothLeAdvertise.get(ExperimentRepresentation.INACTIVE)));
                bluetoothLeAdvertiseWindows.setText(longOrNullToEmpty(experiment.bluetoothLeAdvertise.get(ExperimentRepresentation.WINDOWS)));
                bluetoothLeAdvertiseTxPower.setText(longOrNullToEmpty(experiment.bluetoothLeAdvertise.get(ExperimentRepresentation.TX_POWER)));
                bluetoothLeAdvertiseInterval.setText(longOrNullToEmpty(experiment.bluetoothLeAdvertise.get(ExperimentRepresentation.INTERVAL)));
            }

            for(String tagValue : experiment.tags) {
                Chip chip = new Chip(this);
                chip.setText(tagValue.toUpperCase());
                chip.setCloseIconVisible(true);
                chip.setClickable(true);
                tags.addView(chip);
                chip.setOnCloseIconClickListener(tags::removeView);
            }
        }
    }
}