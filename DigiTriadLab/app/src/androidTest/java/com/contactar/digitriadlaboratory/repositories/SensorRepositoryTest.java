package com.contactar.digitriadlaboratory.repositories;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.contactar.digitriadlaboratory.DatabaseTest;
import com.contactar.digitriadlaboratory.ObservableDataTestUtil;
import com.contactar.digitriadlaboratory.TestUtils;
import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.entities.Device;
import com.contactar.digitriadlaboratory.database.entities.Experiment;
import com.contactar.digitriadlaboratory.database.entities.Run;
import com.contactar.digitriadlaboratory.database.entities.SensorRecord;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SensorRepositoryTest extends DatabaseTest {

    private SensorRepository repository;
    private long windowId;
    private Run run;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        ExperimentRepository experimentRepository = new ExperimentRepository(db);
        RunRepository runRepository = new RunRepository(db);
        WindowRepository windowRepository = new WindowRepository(db);
        repository = new SensorRepository(db);
        Device device = TestUtils.getDevice();
        device.id = deviceRepository.insert(device);
        Experiment experiment = TestUtils.getExperiment(device.id);
        experiment.id = experimentRepository.insert(experiment);
        run = TestUtils.getRun(0, experiment.id);
        run.id = runRepository.insert(run);
        windowId = windowRepository.insert(run.id, 0, SourceTypeEnum.SENSORS.name());
    }

    @Test
    public void getLiveCount() {
        try {
            assertNull(ObservableDataTestUtil.getValue(repository.getLiveCount(run.id)));
            List<SensorRecord> records = TestUtils.getSensorRecordList(windowId);
            repository.insert(records);
            assertEquals(records.size(), ObservableDataTestUtil.getValue(repository.getLiveCount(run.id)).longValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllSamplesFor() {
        List<SensorRecord> records = TestUtils.getSensorRecordList(windowId);
        repository.insert(records);
        long[] runs = {run.id};
        assertEquals(records.size(), repository.getAllSamples(runs).size());
    }

    @Test
    public void insert() {
        List<SensorRecord> records = TestUtils.getSensorRecordList(windowId);
        long[] ids = repository.insert(records);
        assertEquals(records.size(), ids.length);
    }
}