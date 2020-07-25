package com.rfdetke.digitriadlaboratory.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseSingleton {
    private static AppDatabase INSTANCE;
    private static AppDatabase MEMORY_INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "experiment-database").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static AppDatabase getMemoryInstance(Context context) {
        if(MEMORY_INSTANCE == null) {
            MEMORY_INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class).allowMainThreadQueries().build();
        }
        return MEMORY_INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
    public static void destroyMemoryInstance() { MEMORY_INSTANCE = null; }
}
