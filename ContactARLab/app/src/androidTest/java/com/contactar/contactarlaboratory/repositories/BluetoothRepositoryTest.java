package com.contactar.contactarlaboratory.repositories;

import com.contactar.contactarlaboratory.DatabaseTest;
import com.contactar.contactarlaboratory.ObservableDataTestUtil;
import com.contactar.contactarlaboratory.TestUtils;
import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.entities.BluetoothRecord;
import com.contactar.contactarlaboratory.database.entities.Device;
import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.database.entities.Run;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BluetoothRepositoryTest extends DatabaseTest {

    private BluetoothRepository repository;
    private long windowId;
    private Run run;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        ExperimentRepository experimentRepository = new ExperimentRepository(db);
        RunRepository runRepository = new RunRepository(db);
        WindowRepository windowRepository = new WindowRepository(db);
        repository = new BluetoothRepository(db);
        Device device = TestUtils.getDevice();
        device.id = deviceRepository.insert(device);
        Experiment experiment = TestUtils.getExperiment(device.id);
        experiment.id = experimentRepository.insert(experiment);
        run = TestUtils.getRun(0, experiment.id);
        run.id = runRepository.insert(run);
        windowId = windowRepository.insert(run.id, 0, SourceTypeEnum.BLUETOOTH.name());
    }

    @Test
    public void getLiveCount() {
        try {
            assertNull(ObservableDataTestUtil.getValue(repository.getLiveCount(run.id)));
            List<BluetoothRecord> records = TestUtils.getBluetoothRecordList(windowId);
            repository.insertBluetooth(records);
            assertEquals(records.size(), ObservableDataTestUtil.getValue(repository.getLiveCount(run.id)).longValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertBluetooth() {
        List<BluetoothRecord> records = TestUtils.getBluetoothRecordList(windowId);
        long[] ids = repository.insertBluetooth(records);
        assertEquals(ids.length, records.size());
    }

    @Test
    public void getAllSamples() {
        List<BluetoothRecord> records = TestUtils.getBluetoothRecordList(windowId);
        repository.insertBluetooth(records);
        long[] runs = {run.id};
        assertEquals(records.size(), repository.getAllSamples(runs).size());
    }
}