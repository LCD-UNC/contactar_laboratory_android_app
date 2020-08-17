package com.rfdetke.digitriadlaboratory.repositories;

import android.os.ParcelUuid;

import com.rfdetke.digitriadlaboratory.DatabaseTest;
import com.rfdetke.digitriadlaboratory.TestEntityGenerator;
import com.rfdetke.digitriadlaboratory.constants.RunStateEnum;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

public class RunRepositoryTest extends DatabaseTest {

    private Experiment experiment;
    private RunRepository repository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        ExperimentRepository experimentRepository = new ExperimentRepository(db);
        repository = new RunRepository(db);
        Device device = TestEntityGenerator.getDevice();
        device.id = deviceRepository.insert(device);
        experiment = TestEntityGenerator.getExperiment(device.id);
        experiment.id = experimentRepository.insert(experiment);
    }

    @Test
    public void updateState() {
        Run run = TestEntityGenerator.getRun(0, experiment.id);
        run.id = repository.insert(run);
        repository.updateState(run.id, RunStateEnum.RUNNING.name());
        assertNotEquals(run.state, repository.getById(run.id).state);
    }

    @Test
    public void getLastRunForExperimentNonZero() {
        repository.insert(TestEntityGenerator.getRun(0, experiment.id));
        long n = repository.getLastRunForExperiment(experiment.id);
        assertNotEquals(n, 0);
    }

    @Test
    public void getLastRunForExperimentZero() {
        long n = repository.getLastRunForExperiment(experiment.id);
        assertEquals(n, 0);
    }

    @Test
    public void getById() {
        Run run = TestEntityGenerator.getRun(0, experiment.id);
        run.id = repository.insert(run);
        assertEquals(run.id, repository.getById(run.id).id);
    }

    @Test
    public void insert() {
        long id = repository.insert(TestEntityGenerator.getRun(0, experiment.id));
        assertNotNull(repository.getById(id));
    }

    @Test
    public void delete() {
        Run run = TestEntityGenerator.getRun(0, experiment.id);
        run.id = repository.insert(run);
        repository.delete(run);
        assertNull(repository.getById(run.id));
    }
}