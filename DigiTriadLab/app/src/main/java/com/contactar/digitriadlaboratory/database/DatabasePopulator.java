package com.contactar.digitriadlaboratory.database;

import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.entities.SourceType;
import com.contactar.digitriadlaboratory.repositories.SourceTypeRepository;

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
