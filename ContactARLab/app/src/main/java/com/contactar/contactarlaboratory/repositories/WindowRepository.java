package com.contactar.contactarlaboratory.repositories;

import com.contactar.contactarlaboratory.database.AppDatabase;
import com.contactar.contactarlaboratory.database.daos.WindowDao;
import com.contactar.contactarlaboratory.database.daos.SourceTypeDao;
import com.contactar.contactarlaboratory.database.entities.Window;

import java.util.Date;

public class WindowRepository {
    private WindowDao windowDao;
    private SourceTypeDao sourceTypeDao;

    public WindowRepository(AppDatabase database) {
        windowDao = database.getWindowDao();
        sourceTypeDao = database.getSourceTypeDao();
    }

    public long insert(long runId, long number, String type) {
        return windowDao.insert(new Window(new Date(), number, runId, sourceTypeDao.getSourceTypeByType(type).id));
    }

    public Window getById(long id) {
        return windowDao.getById(id);
    }
}
