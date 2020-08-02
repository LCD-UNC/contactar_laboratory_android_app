package com.rfdetke.digitriadlaboratory.database;

import android.content.Context;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.daos.SourceTypeDao;
import com.rfdetke.digitriadlaboratory.database.entities.SourceType;
import com.rfdetke.digitriadlaboratory.repositories.SourceTypeRepository;

public class DatabasePopulator {

    public static void prepopulate(AppDatabase db) {
        populateSourceTypes(db);
    }

    private static void populateSourceTypes(AppDatabase db) {
        SourceTypeRepository repository = new SourceTypeRepository(db);
        for (SourceTypeEnum sourceType : SourceTypeEnum.values()) {
            repository.insert(new SourceType(sourceType.toString()));
        }
    }
}
