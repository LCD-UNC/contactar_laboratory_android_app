package com.rfdetke.digitriadlaboratory.repositories;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.SourceTypeDao;
import com.rfdetke.digitriadlaboratory.database.entities.SourceType;

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
