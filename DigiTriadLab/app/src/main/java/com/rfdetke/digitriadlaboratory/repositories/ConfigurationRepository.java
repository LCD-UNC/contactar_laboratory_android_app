package com.rfdetke.digitriadlaboratory.repositories;

import com.rfdetke.digitriadlaboratory.database.AppDatabase;
import com.rfdetke.digitriadlaboratory.database.daos.SourceTypeDao;
import com.rfdetke.digitriadlaboratory.database.daos.WindowConfigurationDao;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;

import java.util.List;

public class ConfigurationRepository {

    private WindowConfigurationDao windowConfigurationDao;
    private SourceTypeDao sourceTypeDao;


    public ConfigurationRepository(AppDatabase database) {
        windowConfigurationDao = database.getWindowConfigurationDao();
        sourceTypeDao = database.getSourceTypeDao();
    }

    public List<WindowConfiguration> getConfigurationsForExperiment(long experimentId) {
        return windowConfigurationDao.getWindowConfigurationsByExperiment(experimentId);
    }

    public long insert(WindowConfiguration windowConfiguration) { return windowConfigurationDao.insert(windowConfiguration); }

    public void delete(WindowConfiguration windowConfiguration) { windowConfigurationDao.delete(windowConfiguration); }

    public WindowConfiguration getConfigurationForExperimentByType(long experimentId, String type) {
        long sourceId = sourceTypeDao.getSourceTypeByType(type).id;
        return windowConfigurationDao.getWindowConfigurationByExperimentIdAndType(experimentId, sourceId);
    }
}
