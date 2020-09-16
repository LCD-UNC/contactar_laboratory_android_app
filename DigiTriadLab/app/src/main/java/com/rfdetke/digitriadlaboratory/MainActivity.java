package com.rfdetke.digitriadlaboratory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.rfdetke.digitriadlaboratory.constants.RunStateEnum;
import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabasePopulator;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;
import com.rfdetke.digitriadlaboratory.database.daos.RunDao.StartDuration;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.export.gdrive.DriveServiceHelper;
import com.rfdetke.digitriadlaboratory.repositories.DeviceRepository;
import com.rfdetke.digitriadlaboratory.repositories.RunRepository;
import com.rfdetke.digitriadlaboratory.views.DeviceInfoActivity;
import com.rfdetke.digitriadlaboratory.views.NewExperimentActivity;
import com.rfdetke.digitriadlaboratory.views.listadapters.ExperimentListAdapter;
import com.rfdetke.digitriadlaboratory.views.modelviews.ExperimentViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 20;
    private static final int REQUEST_PERMISSIONS = 1;
    private static final int REQUEST_CODE_SIGN_IN = 5;

    private List<String> permissionsList;
    private String[] permissionsArray;
    ExperimentListAdapter adapter;
    private AppDatabase database;
    private WifiManager wifiManager;
    private DriveServiceHelper mDriveServiceHelper;
    private GoogleSignInClient client;

    private Toolbar topToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        setContentView(R.layout.activity_main);

        topToolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(topToolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new ExperimentListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ExperimentViewModel experimentViewModel = new ViewModelProvider(this).get(ExperimentViewModel.class);
        experimentViewModel.getAllExperimentDone().observe(this, experiments -> adapter.setExperiments(experiments));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if(!hasPermissions()) {
                requestPermissions(permissionsArray, REQUEST_PERMISSIONS);
            } else if(!wifiManager.isWifiEnabled()) {
                showEnableWifiDialog();
            } else {
                Intent intent = new Intent(MainActivity.this, NewExperimentActivity.class);
                startActivity(intent);
            }
        });

        database = DatabaseSingleton.getInstance(getApplicationContext());
        DatabasePopulator.prepopulate(database);

        DeviceRepository deviceRepository = new DeviceRepository(database);
        Device device = deviceRepository.getDevice();
        if (device == null) {
            deviceRepository.insert(new Device(null, Build.MANUFACTURER.toUpperCase(), Build.MODEL.toUpperCase(), new ParcelUuid(UUID.randomUUID())));
        }
        checkAllRunStates();
//        requestSignIn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void requestSignIn() {
        Log.d("Main", "Requesting sign-in");

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE))
                        .build();
        client = GoogleSignIn.getClient(getApplicationContext(), signInOptions);

        // The result of the sign-in Intent is handled in onActivityResult.
        startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    Log.d("Main", "Signed in as " + googleAccount.getEmail());

                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    this, Collections.singleton(DriveScopes.DRIVE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("DigiTriad Lab")
                                    .build();

                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(this, "Remember to sign in with an unc.edu.ar account", Toast.LENGTH_LONG).show();
                });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode != Activity.RESULT_OK) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                break;
            case REQUEST_CODE_SIGN_IN:
                if (data != null) {
                    handleSignInResult(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
