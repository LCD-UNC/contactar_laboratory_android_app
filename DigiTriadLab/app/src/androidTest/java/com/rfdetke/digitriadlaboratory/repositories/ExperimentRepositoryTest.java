package com.rfdetke.digitriadlaboratory.repositories;

import com.rfdetke.digitriadlaboratory.DatabaseTest;
import com.rfdetke.digitriadlaboratory.TestEntityGenerator;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExperimentRepositoryTest extends DatabaseTest {

    private ExperimentRepository repository;
    private Experiment experiment;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        repository = new ExperimentRepository(db);
        Device device = TestEntityGenerator.getDevice();
        device.id = deviceRepository.insert(device);
        experiment = TestEntityGenerator.getExperiment(device.id);
    }

    @Test
    public void insert() {
        long id = repository.insert(experiment);
        assertTrue(id != 0);
    }

    @Test
    public void delete() {
        experiment.id = repository.insert(experiment);
        repository.delete(experiment);
        assertNull(repository.getById(experiment.id));
    }

    @Test
    public void getLastEquals() {
        repository.insert(experiment);
        long id = repository.insert(experiment);
        Experiment experiment = repository.getLast();
        assertEquals(experiment.id, id);
    }

    @Test
    public void getLastNotNull() {
        repository.insert(experiment);
        assertNotNull(repository.getLast());
    }

    @Test
    public void getLastNull() {
        assertNull(repository.getLast());
    }

    @Test
    public void getByIdNotNull() {
        long id = repository.insert(experiment);
        assertNotNull(repository.getById(id));
    }

    @Test
    public void getByIdNull() {
        repository.insert(experiment);
        assertNull(repository.getById(20));
    }
}