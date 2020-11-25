package com.contactar.digitriadlaboratory.export.csv;

import com.contactar.digitriadlaboratory.DatabaseTest;
import com.contactar.digitriadlaboratory.TestUtils;
import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.entities.Device;
import com.contactar.digitriadlaboratory.database.entities.Experiment;
import com.contactar.digitriadlaboratory.database.entities.Run;
import com.contactar.digitriadlaboratory.repositories.BluetoothRepository;
import com.contactar.digitriadlaboratory.repositories.DeviceRepository;
import com.contactar.digitriadlaboratory.repositories.ExperimentRepository;
import com.contactar.digitriadlaboratory.repositories.RunRepository;
import com.contactar.digitriadlaboratory.repositories.WindowRepository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BluetoothLeCsvFileWriterTest extends SampleCsvFileWriterTest {

    private BluetoothLeCsvFileWriter fileWriter;
    private BluetoothLeCsvFileWriter fileWriterMultiple;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        windowRepository.insert(runs2[0], 0, SourceTypeEnum.BLUETOOTH_LE.name());
        windowRepository.insert(runs2[1], 0, SourceTypeEnum.BLUETOOTH_LE.name());
        fileWriter = new BluetoothLeCsvFileWriter(runs1, db, context);
        fileWriterMultiple = new BluetoothLeCsvFileWriter(runs2, db, context);
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