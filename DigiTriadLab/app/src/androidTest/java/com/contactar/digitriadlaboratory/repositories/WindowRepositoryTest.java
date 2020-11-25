package com.contactar.digitriadlaboratory.repositories;

import com.contactar.digitriadlaboratory.DatabaseTest;
import com.contactar.digitriadlaboratory.TestUtils;
import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.entities.Device;
import com.contactar.digitriadlaboratory.database.entities.Experiment;
import com.contactar.digitriadlaboratory.database.entities.Run;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WindowRepositoryTest extends DatabaseTest {

    private WindowRepository repository;
    private Run run;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        ExperimentRepository experimentRepository = new ExperimentRepository(db);
        RunRepository runRepository = new RunRepository(db);
        repository = new WindowRepository(db);
        Device device = TestUtils.getDevice();
        device.id = deviceRepository.insert(device);
        Experiment experiment = TestUtils.getExperiment(device.id);
        experiment.id = experimentRepository.insert(experiment);
        run = TestUtils.getRun(0,experiment.id);
        run.id = runRepository.insert(run);
    }

    @Test
    public void insert() {
        long id = repository.insert(run.id, 0, SourceTypeEnum.WIFI.name());
        assertNotNull(repository.getById(id));
    }
}