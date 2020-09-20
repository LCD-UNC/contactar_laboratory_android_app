package com.rfdetke.digitriadlaboratory;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rfdetke.digitriadlaboratory.constants.RunStateEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabasePopulator;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.database.daos.RunDao.StartDuration;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.gapis.GoogleSessionAppCompatActivity;
import com.rfdetke.digitriadlaboratory.repositories.DeviceRepository;
import com.rfdetke.digitriadlaboratory.repositories.RunRepository;
import com.rfdetke.digitriadlaboratory.views.ApplicationInfo;
import com.rfdetke.digitriadlaboratory.views.DeviceInfoActivity;
import com.rfdetke.digitriadlaboratory.views.NewExperimentActivity;
import com.rfdetke.digitriadlaboratory.views.listadapters.ExperimentListAdapter;
import com.rfdetke.digitriadlaboratory.views.modelviews.ExperimentViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainActivity extends GoogleSessionAppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 20;
    private static final int REQUEST_PERMISSIONS = 1;

    private List<String> permissionsList;
    private String[] permissionsArray;
    ExperimentListAdapter adapter;
    private AppDatabase database;
    private WifiManager wifiManager;


    private Toolbar topToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        preparePermissions();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            if (!wifiManager.isWifiEnabled()) {
                showEnableWifiDialog();
            }
        }

        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new ExperimentListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ExperimentViewModel experimentViewModel = new ViewModelProvider(this).get(ExperimentViewModel.class);
        experimentViewModel.getAllExperimentDone().observe(this, experiments -> adapter.setExperiments(experiments));

        database = DatabaseSingleton.getInstance(getApplicationContext());
        DatabasePopulator.prepopulate(database);

        DeviceRepository deviceRepository = new DeviceRepository(database);
        Device device = deviceRepository.getDevice();
        if (device == null) {
            deviceRepository.insert(new Device(null, Build.MANUFACTURER.toUpperCase(), Build.MODEL.toUpperCase(), new ParcelUuid(UUID.randomUUID())));
        }
        checkAllRunStates();
    }

    @Override
    public boolean onMenuItemClickListener(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                return true;

            case R.id.new_experiment:
                addExperiment();
                return true;

            default:
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void checkAllRunStates() {
        RunRepository runRepository = new RunRepository(database);
        List<StartDuration> times = runRepository.getCurrentScheduledOrRunningStartAndDuration();

        for(StartDuration startDuration : times) {
            Date now = new Date();
            if(now.getTime()>startDuration.start.getTime()+startDuration.duration)
                runRepository.updateState(startDuration.id, RunStateEnum.DONE.name());
        }
    }

    private void showEnableWifiDialog() {
        DialogFragment newFragment = new EnableWifiDialogFragment();
        newFragment.setCancelable(false);
        newFragment.show(getSupportFragmentManager(), "wifi");
    }

    private void preparePermissions() {
        permissionsList = new ArrayList<>();
        permissionsList.addAll(Arrays.asList(
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionsList.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }

        permissionsArray = new String[permissionsList.size()];
        for (String permission : permissionsList) {
            permissionsArray[permissionsList.indexOf(permission)] = permission;
        }
    }

    private boolean hasPermissions() {
        for (String permission : permissionsList) {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void showDeviceInfo(MenuItem menuItem) {
        Intent intent = new Intent(MainActivity.this, DeviceInfoActivity.class);
        startActivity(intent);
    }

    public void addExperiment(){
        if(!hasPermissions()) {
            requestPermissions(permissionsArray, REQUEST_PERMISSIONS);
        } else if(!wifiManager.isWifiEnabled()) {
            showEnableWifiDialog();
        } else {
            Intent intent = new Intent(MainActivity.this, NewExperimentActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode != Activity.RESULT_OK) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    public void showAppInfo(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, ApplicationInfo.class);
        startActivity(intent);
    }

    public static class EnableWifiDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_enable_wifi)
                    .setPositiveButton(R.string.enable, (dialog, id) -> {
                        Intent enableBtIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(enableBtIntent);
                    });
            return builder.create();
        }
    }
}
