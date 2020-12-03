package com.contactar.contactarlaboratory.repositories;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.daos.SourceTypeDao;
import com.contactar.contactarlaboratory.database.entities.SourceType;

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
