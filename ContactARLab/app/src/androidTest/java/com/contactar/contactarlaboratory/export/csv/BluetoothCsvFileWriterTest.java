package com.contactar.contactarlaboratory.export.csv;

import com.contactar.contactarlaboratory.constants.SourceTypeEnum;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BluetoothCsvFileWriterTest extends SampleCsvFileWriterTest {

    private BluetoothCsvFileWriter fileWriter;
    private BluetoothCsvFileWriter fileWriterMultiple;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        windowRepository.insert(runs2[0], 0, SourceTypeEnum.BLUETOOTH.name());
        windowRepository.insert(runs2[1],0, SourceTypeEnum.BLUETOOTH.name());
        fileWriter = new BluetoothCsvFileWriter(runs1, db, context);
        fileWriterMultiple = new BluetoothCsvFileWriter(runs2, db, context);
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