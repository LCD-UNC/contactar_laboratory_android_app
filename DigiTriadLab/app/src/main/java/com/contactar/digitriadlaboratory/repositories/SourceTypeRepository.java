package com.contactar.digitriadlaboratory.repositories;

import com.contactar.digitriadlaboratory.database.AppDatabase;
import com.contactar.digitriadlaboratory.database.daos.SourceTypeDao;
import com.contactar.digitriadlaboratory.database.entities.SourceType;

public class SourceTypeRepository {

    private SourceTypeDao sourceTypeDao;

    public SourceTypeRepository(AppDatabase database) {
        sourceTypeDao = database.getSourceTypeDao();
    }

    public SourceType getById(long id) {
        return sourceTypeDao.getSourceTypeById(id);
    }

    public SourceType getByType(String type) {
        return sourceTypeDao.getSourceTypeByType(type);
    }

    public long insert(SourceType sourceType) {
        return sourceTypeDao.insert(sourceType);
    }

}
