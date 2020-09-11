package com.rfdetke.digitriadlaboratory.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.le.AdvertisingSetParameters;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.rfdetke.digitriadlaboratory.R;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.database.entities.AdvertiseConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.ConfigurationRepository;
import com.rfdetke.digitriadlaboratory.repositories.DeviceRepository;
import com.rfdetke.digitriadlaboratory.repositories.ExperimentRepository;
import com.rfdetke.digitriadlaboratory.repositories.SourceTypeRepository;
import com.rfdetke.digitriadlaboratory.export.representations.ExperimentRepresentation;
import com.rfdetke.digitriadlaboratory.utils.ShareTools;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NewExperimentActivity extends AppCompatActivity {

    public static final int SCAN_QR = 10;
    private ExperimentRepository experimentRepository;
    private DeviceRepository deviceRepository;
    private SourceTypeRepository sourceTypeRepository;
    private ConfigurationRepository configurationRepository;

    private EditText codename;
    private EditText description;

    private TextView wifiTitle;
    private EditText wifiActive;
    private EditText wifiInactive;
    private EditText wifiWindows;
    private Switch wifiSwitch;

    private TextView bluetoothTitle;
    private EditText bluetoothActive;
    private EditText bluetoothInactive;
    private EditText bluetoothWindows;
    private Switch bluetoothSwitch;

    private TextView bluetoothLeTitle;
    private EditText bluetoothLeActive;
    private EditText bluetoothLeInactive;
    private EditText bluetoothLeWindows;
    private Switch bluetoothLeSwitch;

    private TextView sensorsTitle;
    private EditText sensorsActive;
    private EditText sensorsInactive;
    private EditText sensorsWindows;
    private Switch sensorsSwitch;

    private TextView cellTitle;
    private EditText cellActive;
    private EditText cellInactive;
    private EditText cellWindows;
    private Switch cellSwitch;

    private TextView bluetoothLeAdvertiseTitle;
    private EditText bluetoothLeAdvertiseActive;
    private EditText bluetoothLeAdvertiseInactive;
    private EditText bluetoothLeAdvertiseWindows;
    private EditText bluetoothLeAdvertiseTxPower;
    private EditText bluetoothLeAdvertiseInterval;
    private Switch bluetoothLeAdvertiseSwitch;
    private ChipGroup tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_experiment);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.scan_toolbar);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.new_experiment));

        findViewById(R.id.scan_button).setVisibility(View.VISIBLE);
        findViewById(R.id.scan_button).setOnClickListener(v -> {
            Intent intent = new Intent(NewExperimentActivity.this, ScanQrExperiment.class);
            startActivityForResult(intent, SCAN_QR);
        });

        AppDatabase database = DatabaseSingleton.getInstance(getApplicationContext());
        experimentRepository = new ExperimentRepository(database);
        deviceRepository = new DeviceRepository(database);
        sourceTypeRepository = new SourceTypeRepository(database);
        configurationRepository = new ConfigurationRepository(database);

        codename = findViewById(R.id.experiment_codename);
        description = findViewById(R.id.experiment_description);

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
                        !sensorsSwitch.isChecked()) {
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
                !wifiWindows.getText().toString().isEmpty();
    }

    private boolean validateBluetoothConfig() {
        return  !bluetoothActive.getText().toString().isEmpty() &&
                !bluetoothInactive.getText().toString().isEmpty() &&
                !bluetoothWindows.getText().toString().isEmpty();
    }

    private boolean validateBluetoothLeConfig() {
        return  !bluetoothLeActive.getText().toString().isEmpty() &&
                !bluetoothLeInactive.getText().toString().isEmpty() &&
                !bluetoothLeWindows.getText().toString().isEmpty();
    }

    private boolean validateSensorsConfig() {
        return !sensorsActive.getText().toString().isEmpty() &&
                !sensorsInactive.getText().toString().isEmpty() &&
                !sensorsWindows.getText().toString().isEmpty();
    }

    private boolean validateCellConfig() {
        return !cellActive.getText().toString().isEmpty() &&
                !cellInactive.getText().toString().isEmpty() &&
                !cellWindows.getText().toString().isEmpty();
    }

    private long saveExperiment() {
        return experimentRepository.insert( new Experiment(codename.getText().toString(),
                description.getText().toString(),
                deviceRepository.getDevice().id));
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
                String experiment = data.getStringExtra(ScanQrExperiment.EXTRA_CONFIG_STRING);
                if (experiment != null) {
                    ExperimentRepresentation representation = ShareTools.getCodedExperiment(experiment);
                    codename.setText(representation.codename);
                    description.setText(representation.description);

                    wifiActive.setText(longOrNullToEmpty(representation.wifi.get(ExperimentRepresentation.ACTIVE)));
                    wifiInactive.setText(longOrNullToEmpty(representation.wifi.get(ExperimentRepresentation.INACTIVE)));
                    wifiWindows.setText(longOrNullToEmpty(representation.wifi.get(ExperimentRepresentation.WINDOWS)));

                    bluetoothActive.setText(longOrNullToEmpty(representation.bluetooth.get(ExperimentRepresentation.ACTIVE)));
                    bluetoothInactive.setText(longOrNullToEmpty(representation.bluetooth.get(ExperimentRepresentation.INACTIVE)));
                    bluetoothWindows.setText(longOrNullToEmpty(representation.bluetooth.get(ExperimentRepresentation.WINDOWS)));

                    bluetoothLeActive.setText(longOrNullToEmpty(representation.bluetoothLe.get(ExperimentRepresentation.ACTIVE)));
                    bluetoothLeInactive.setText(longOrNullToEmpty(representation.bluetoothLe.get(ExperimentRepresentation.INACTIVE)));
                    bluetoothLeWindows.setText(longOrNullToEmpty(representation.bluetoothLe.get(ExperimentRepresentation.WINDOWS)));

                    sensorsActive.setText(longOrNullToEmpty(representation.sensors.get(ExperimentRepresentation.ACTIVE)));
                    sensorsInactive.setText(longOrNullToEmpty(representation.sensors.get(ExperimentRepresentation.INACTIVE)));
                    sensorsWindows.setText(longOrNullToEmpty(representation.sensors.get(ExperimentRepresentation.WINDOWS)));

                    cellActive.setText(longOrNullToEmpty(representation.cell.get(ExperimentRepresentation.ACTIVE)));
                    cellInactive.setText(longOrNullToEmpty(representation.cell.get(ExperimentRepresentation.INACTIVE)));
                    cellWindows.setText(longOrNullToEmpty(representation.cell.get(ExperimentRepresentation.WINDOWS)));

                    bluetoothLeAdvertiseActive.setText(longOrNullToEmpty(representation.bluetoothLeAdvertise.get(ExperimentRepresentation.ACTIVE)));
                    bluetoothLeAdvertiseInactive.setText(longOrNullToEmpty(representation.bluetoothLeAdvertise.get(ExperimentRepresentation.INACTIVE)));
                    bluetoothLeAdvertiseWindows.setText(longOrNullToEmpty(representation.bluetoothLeAdvertise.get(ExperimentRepresentation.WINDOWS)));
                    bluetoothLeAdvertiseTxPower.setText(longOrNullToEmpty(representation.bluetoothLeAdvertise.get(ExperimentRepresentation.TX_POWER)));
                    bluetoothLeAdvertiseInterval.setText(longOrNullToEmpty(representation.bluetoothLeAdvertise.get(ExperimentRepresentation.INTERVAL)));

                    for(String tagValue : representation.tags) {
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
    }
}