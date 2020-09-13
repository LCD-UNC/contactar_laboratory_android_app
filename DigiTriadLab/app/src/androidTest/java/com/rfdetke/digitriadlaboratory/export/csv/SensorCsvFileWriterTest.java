package com.rfdetke.digitriadlaboratory.export.csv;

import com.rfdetke.digitriadlaboratory.DatabaseTest;
import com.rfdetke.digitriadlaboratory.TestUtils;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.repositories.DeviceRepository;
import com.rfdetke.digitriadlaboratory.repositories.ExperimentRepository;
import com.rfdetke.digitriadlaboratory.repositories.RunRepository;
import com.rfdetke.digitriadlaboratory.repositories.WindowRepository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SensorCsvFileWriterTest extends SampleCsvFileWriterTest {

    private SensorCsvFileWriter fileWriter;
    private SensorCsvFileWriter fileWriterMultiple;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        windowRepository.insert(runs2[0], SourceTypeEnum.SENSORS.name());
        windowRepository.insert(runs2[1], SourceTypeEnum.SENSORS.name());
        fileWriter = new SensorCsvFileWriter(runs1, db, context);
        fileWriterMultiple = new SensorCsvFileWriter(runs2, db, context);
    }

    @Test
    public void getPath() {
        assertNotNull(fileWriter.getPath());
    }

    @Test
    public void getFileName() {
        assertNotNull(fileWriter.getFileName());
        assertNotNull(fileWriterMultiple.getFileName());
    }

    @Test
    public void getContent() {
        assertNotNull(fileWriter.getContent());
    }

    @Test
    public void getExportableData() {
        assertNotNull(fileWriter.getExportableData());
    }

    @Test
    public void getKey() {
        assertNotNull(fileWriter.getKey());
    }
}