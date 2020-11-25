package com.contactar.digitriadlaboratory.export.json;

import com.contactar.digitriadlaboratory.DatabaseTest;
import com.contactar.digitriadlaboratory.TestUtils;
import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.entities.Device;
import com.contactar.digitriadlaboratory.database.entities.Experiment;
import com.contactar.digitriadlaboratory.database.entities.Run;
import com.contactar.digitriadlaboratory.export.csv.BluetoothCsvFileWriter;
import com.contactar.digitriadlaboratory.repositories.DeviceRepository;
import com.contactar.digitriadlaboratory.repositories.ExperimentRepository;
import com.contactar.digitriadlaboratory.repositories.RunRepository;
import com.contactar.digitriadlaboratory.repositories.WindowRepository;

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