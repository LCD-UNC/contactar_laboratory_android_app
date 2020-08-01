package com.rfdetke.digitriadlaboratory.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.rfdetke.digitriadlaboratory.R;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.ConfigurationRepository;
import com.rfdetke.digitriadlaboratory.repositories.DeviceRepository;
import com.rfdetke.digitriadlaboratory.repositories.ExperimentRepository;
import com.rfdetke.digitriadlaboratory.repositories.SourceTypeRepository;

import java.util.Objects;

public class NewExperimentActivity extends AppCompatActivity {

    private ExperimentRepository experimentRepository;
    private DeviceRepository deviceRepository;
    private SourceTypeRepository sourceTypeRepository;
    private ConfigurationRepository configurationRepository;

    private EditText codename;
    private EditText description;
    private EditText wifiActive;
    private EditText wifiInactive;
    private EditText wifiWindows;
    private EditText bluetoothActive;
    private EditText bluetoothInactive;
    private EditText bluetoothWindows;
    private EditText bluetoothLeActive;
    private EditText bluetoothLeInactive;
    private EditText bluetoothLeWindows;
    private Switch wifiSwitch;
    private Switch bluetoothSwitch;
    private Switch bluetoothLeSwitch;
    private TextView wifiTitle;
    private TextView bluetoothTitle;
    private TextView bluetoothLeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_experiment);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.new_experiment));

        findViewById(R.id.toolbar_button).setVisibility(View.INVISIBLE);

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
        bluetoothActive = findViewById(R.id.bluetooth_active);
        bluetoothInactive = findViewById(R.id.bluetooth_inactive);
        bluetoothWindows = findViewById(R.id.bluetooth_windows);
        bluetoothTitle = findViewById(R.id.bluetooth_title);
        bluetoothLeActive = findViewById(R.id.bluetooth_le_active);
        bluetoothLeInactive = findViewById(R.id.bluetooth_le_inactive);
        bluetoothLeWindows = findViewById(R.id.bluetooth_le_windows);
        bluetoothLeTitle = findViewById(R.id.bluetooth_le_title);
        wifiSwitch = findViewById(R.id.wifi_switch);
        bluetoothSwitch = findViewById(R.id.bluetooth_switch);
        bluetoothLeSwitch = findViewById(R.id.bluetooth_le_switch);
        Button saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(v -> {
            Intent replyIntent = new Intent();
            long experimentId = 0;
            boolean success = true;
            if(validateExperiment()) {
                experimentId = saveExperiment();
                if(!wifiSwitch.isChecked() && !bluetoothSwitch.isChecked() && !bluetoothLeSwitch.isChecked())
                    success = false;
                else {
                    if (wifiSwitch.isChecked()) {
                        if (validateWifiConfig()) {
                            saveWifiConfig(experimentId);
                        } else {
                            success = false;
                        }
                    }
                    if (bluetoothSwitch.isChecked()) {
                        if (validateBluetoothConfig()) {
                            saveBluetoothConfig(experimentId);
                        } else {
                            success = false;
                        }
                    }
                    if (bluetoothLeSwitch.isChecked()) {
                        if (validateBluetoothLeConfig()) {
                            saveBluetoothLeConfig(experimentId);
                        } else {
                            success = false;
                        }
                    }
                }
            } else {
                success = false;
            }

            if(success) {
                setResult(RESULT_OK, replyIntent);
                finish();
            } else {
                Toast.makeText(this, getResources().getString(R.string.error, "Required field empty"), Toast.LENGTH_SHORT).show();
                if(experimentId!=0) {
                    Experiment experiment = experimentRepository.getById(experimentId);
                    experimentRepository.delete(experiment);
                }
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

    private long saveExperiment() {
        return experimentRepository.insert( new Experiment(codename.getText().toString(),
                description.getText().toString(),
                deviceRepository.getDevice().id));
    }


    private void saveBluetoothLeConfig(long experimentId) {
        long active = Integer.parseInt(bluetoothLeActive.getText().toString());
        long inactive = Integer.parseInt(bluetoothLeInactive.getText().toString());
        long windows = Integer.parseInt(bluetoothLeWindows.getText().toString());
        long sourceId = sourceTypeRepository.getByType(SourceTypeEnum.BLUETOOTH_LE.name()).id;
        WindowConfiguration configuration = new WindowConfiguration(active,
                inactive, windows, sourceId, experimentId);
        configurationRepository.insert(configuration);
    }

    private void saveBluetoothConfig(long experimentId) {
        long active = Integer.parseInt(bluetoothActive.getText().toString());
        long inactive = Integer.parseInt(bluetoothInactive.getText().toString());
        long windows = Integer.parseInt(bluetoothWindows.getText().toString());
        long sourceId = sourceTypeRepository.getByType(SourceTypeEnum.BLUETOOTH.name()).id;
        WindowConfiguration configuration = new WindowConfiguration(active,
                inactive, windows, sourceId, experimentId);
        configurationRepository.insert(configuration);
    }

    private void saveWifiConfig(long experimentId) {
        long active = Integer.parseInt(wifiActive.getText().toString());
        long inactive = Integer.parseInt(wifiInactive.getText().toString());
        long windows = Integer.parseInt(wifiWindows.getText().toString());
        long sourceId = sourceTypeRepository.getByType(SourceTypeEnum.WIFI.name()).id;
        WindowConfiguration configuration = new WindowConfiguration(active,
                inactive, windows, sourceId, experimentId);
        configurationRepository.insert(configuration);
    }
}