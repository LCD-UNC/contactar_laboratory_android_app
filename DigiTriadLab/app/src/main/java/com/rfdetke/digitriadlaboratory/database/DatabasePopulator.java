package com.rfdetke.digitriadlaboratory.database;

import android.content.Context;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.daos.SourceTypeDao;
import com.rfdetke.digitriadlaboratory.database.entities.SourceType;
import com.rfdetke.digitriadlaboratory.repositories.SourceTypeRepository;

public class DatabasePopulator {

    public static void prepopulate(Context context, boolean inMemory) {
        AppDatabase db;
        if(inMemory) {
            db = DatabaseSingleton.getMemoryInstance(context);
        } else {

            db = DatabaseSingleton.getInstance(context);
        }
        populateSourceTypes(db);
    }

    private static void populateSourceTypes(AppDatabase db) {
        SourceTypeRepository repository = new SourceTypeRepository(db);
        for (SourceTypeEnum sourceType : SourceTypeEnum.values()) {
            repository.insert(new SourceType(sourceType.toString()));
        }
    }
}
