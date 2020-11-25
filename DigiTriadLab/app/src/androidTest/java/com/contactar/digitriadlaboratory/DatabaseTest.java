package com.contactar.digitriadlaboratory;

import android.Manifest;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;

import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.database.DatabasePopulator;
import com.contactar.digitriadlaboratory.database.DatabaseSingleton;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

public class DatabaseTest {
    @Rule
    public GrantPermissionRule permissionsRule = GrantPermissionRule.grant(
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.CAMERA
            );

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();


    protected AppDatabase db;
    protected Context context;

    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();
        db = DatabaseSingleton.getMemoryInstance(context);
        DatabasePopulator.prepopulate(db);
    }

    @After
    public void tearDown() {
        DatabaseSingleton.destroyMemoryInstance();
    }
}
