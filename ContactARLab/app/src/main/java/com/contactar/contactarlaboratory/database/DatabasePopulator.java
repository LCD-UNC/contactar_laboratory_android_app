package com.contactar.contactarlaboratory.database;

import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.entities.SourceType;
import com.contactar.contactarlaboratory.repositories.SourceTypeRepository;

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
