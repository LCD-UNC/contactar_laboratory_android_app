package com.contactar.contactarlaboratory.export.json;

import com.contactar.contactarlaboratory.DatabaseTest;
import com.contactar.contactarlaboratory.TestUtils;
import com.contactar.contactarlaboratory.database.entities.Device;
import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.repositories.DeviceRepository;
import com.contactar.contactarlaboratory.repositories.ExperimentRepository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonFileWriterTest extends DatabaseTest {


    private JsonFileWriter fileWriter;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        ExperimentRepository experimentRepository = new ExperimentRepository(db);
        Device device = TestUtils.getDevice();
        device.id = deviceRepository.insert(device);
        Experiment experiment = TestUtils.getExperiment(device.id);
        experiment.id = experimentRepository.insert(experiment);
        fileWriter = new JsonExperimentFileWriter(experiment.id, db, context);
    }

    @Test
    public void getPath() {
        assertNotNull(fileWriter.getPath());
    }

    @Test
    public void getFileName() {
        assertNotNull(fileWriter.getFileName());
    }

    @Test
    public void getContent() {
        assertNotNull(fileWriter.getContent());
        assertFalse(fileWriter.getContent().isEmpty());
    }

}