package com.contactar.contactarlaboratory.export.csv;

import com.contactar.contactarlaboratory.constants.SourceTypeEnum;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WifiCsvFileWriterTest extends SampleCsvFileWriterTest {


    private WifiCsvFileWriter fileWriter;
    private WifiCsvFileWriter fileWriterMultiple;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        windowRepository.insert(runs2[0], 0, SourceTypeEnum.WIFI.name());
        windowRepository.insert(runs2[1], 0, SourceTypeEnum.WIFI.name());
        fileWriter = new WifiCsvFileWriter(runs1, db, context);
        fileWriterMultiple = new WifiCsvFileWriter(runs2, db, context);
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