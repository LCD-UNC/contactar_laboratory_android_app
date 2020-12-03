package com.contactar.contactarlaboratory.repositories;

import com.contactar.contactarlaboratory.DatabaseTest;
import com.contactar.contactarlaboratory.ObservableDataTestUtil;
import com.contactar.contactarlaboratory.TestUtils;
import com.contactar.contactarlaboratory.database.entities.Device;
import com.contactar.contactarlaboratory.database.entities.Experiment;

import org.junit.Before;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.*;

public class ExperimentRepositoryTest extends DatabaseTest {

    private ExperimentRepository repository;
    private Experiment experiment;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        repository = new ExperimentRepository(db);
        Device device = TestUtils.getDevice();
        device.id = deviceRepository.insert(device);
        experiment = TestUtils.getExperiment(device.id);
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
    public void getAllExperimentDoneNotNull() {
        assertNotNull(repository.getAllExperimentDone());
    }

    @Test
    public void getAllExperimentDoneEmpty() {
        try {
            assertEquals(0, ObservableDataTestUtil.getValue(repository.getAllExperimentDone()).size());
            repository.insert(experiment);
            assertEquals(1, ObservableDataTestUtil.getValue(repository.getAllExperimentDone()).size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getByIdNull() {
        repository.insert(experiment);
        assertNull(repository.getById(20));
    }

    @Test
    public void insertTag() {
        long id = repository.insertTag(TestUtils.getRandomString(7));
        assertTrue(id != 0);
    }

    @Test
    public void getAllTagList() {
        List<String> tags = TestUtils.getRandomStringList(2, 12);
        for (String tag : tags) {
            repository.insertTag(tag);
        }
        assertEquals(tags.size(), repository.getTagList().size());
    }

    @Test
    public void getRelatedTagList() {
        experiment.id = repository.insert(experiment);
        List<String> tags = TestUtils.getRandomStringList(2, 12);
        for (String tag : tags) {
            long id = repository.insertTag(tag);
            repository.relateToTag(experiment.id, id);
        }
        assertEquals(tags.size(), repository.getTagList(experiment.id).size());
    }

    @Test
    public void getTagIdNull() {
        String tag = TestUtils.getRandomString(5);
        repository.insertTag(tag);
        assertEquals(0, repository.getTagId(TestUtils.getRandomString(5)));
    }

    @Test
    public void getTagIdNotNull() {
        String tag = TestUtils.getRandomString(5);
        long id = repository.insertTag(tag);
        assertEquals(id, repository.getTagId(tag));
    }
}