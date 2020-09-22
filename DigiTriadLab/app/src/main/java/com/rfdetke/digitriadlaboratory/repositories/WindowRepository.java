package com.rfdetke.digitriadlaboratory.repositories;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.WindowDao;
import com.rfdetke.digitriadlaboratory.database.daos.SourceTypeDao;
import com.rfdetke.digitriadlaboratory.database.entities.Window;

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
