package com.rfdetke.digitriadlaboratory;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.DatabasePopulator;
import com.rfdetke.digitriadlaboratory.database.DatabaseSingleton;

import org.junit.After;
import org.junit.Before;

public class DatabaseTest {

    AppDatabase db;
    Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        db = DatabaseSingleton.getMemoryInstance(context);
        DatabasePopulator.prepopulate(context, true);
    }

    @After
    public void tearDown() {
        DatabaseSingleton.destroyMemoryInstance();
    }
}
