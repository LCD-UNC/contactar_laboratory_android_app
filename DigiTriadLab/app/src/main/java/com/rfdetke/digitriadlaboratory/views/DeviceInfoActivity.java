package com.rfdetke.digitriadlaboratory.views;

import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rfdetke.digitriadlaboratory.R;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.repositories.DeviceRepository;

import java.util.UUID;

public class DeviceInfoActivity extends AppCompatActivity {

    private AppDatabase database;
    private Device device;
    private ParcelUuid tempUuid;
    private Toolbar topToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);
        topToolbar.setTitle(getString(R.string.device_info_title));

        EditText deviceCodename = findViewById(R.id.device_codename);
        TextView deviceUuid = findViewById(R.id.device_uuid);
        TextView deviceBrand = findViewById(R.id.device_brand);
        TextView deviceModel = findViewById(R.id.device_model);

        Button saveButton = findViewById(R.id.save_button);
        Button newUuidButton = findViewById(R.id.new_uuid_button);

        database = DatabaseSingleton.getInstance(getApplicationContext());
        DeviceRepository deviceRepository = new DeviceRepository(database);

        device = deviceRepository.getDevice();
        if (device == null) {
            deviceRepository.insert(new Device(null, Build.MANUFACTURER.toUpperCase(), Build.MODEL.toUpperCase(), new ParcelUuid(UUID.randomUUID())));
            device = database.getDeviceDao().getDevice();
        }

        if (device.codename != null) {
            deviceCodename.setText(device.codename);
        }

        if (device.uuid != null) {
            deviceUuid.setText(device.uuid.toString().toUpperCase());
        }

        if(device.brand != null) {
            deviceBrand.setText(device.brand.toUpperCase());
        }

        if(device.model != null) {
            deviceModel.setText(device.model.toUpperCase());
        }

        saveButton.setOnClickListener( v -> {
            if (tempUuid != null && !tempUuid.equals(device.uuid)) {
                device.uuid = tempUuid;
            }

            if (!deviceCodename.getText().toString().isEmpty()) {
                device.codename = deviceCodename.getText().toString();
            }
            deviceRepository.update(device);
            finish();
        });

        newUuidButton.setOnClickListener( v -> {
            tempUuid = new ParcelUuid(UUID.randomUUID());
            deviceUuid.setText(tempUuid.toString().toUpperCase());
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return true;
    }
}