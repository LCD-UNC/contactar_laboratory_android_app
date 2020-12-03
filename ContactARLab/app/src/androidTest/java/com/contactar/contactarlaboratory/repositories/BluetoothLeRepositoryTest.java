package com.contactar.contactarlaboratory.repositories;

import com.contactar.contactarlaboratory.DatabaseTest;
import com.contactar.contactarlaboratory.ObservableDataTestUtil;
import com.contactar.contactarlaboratory.TestUtils;
import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.daos.WindowDao.BluetoothLeSampleRecord;
import com.contactar.contactarlaboratory.database.entities.BluetoothLeRecord;
import com.contactar.contactarlaboratory.database.entities.BluetoothLeUuid;
import com.contactar.contactarlaboratory.database.entities.Device;
import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.database.entities.Run;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BluetoothLeRepositoryTest extends DatabaseTest {

    private BluetoothLeRepository repository;
    private long windowId;
    private Run run;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        ExperimentRepository experimentRepository = new ExperimentRepository(db);
        RunRepository runRepository = new RunRepository(db);
        WindowRepository windowRepository = new WindowRepository(db);
        repository = new BluetoothLeRepository(db);
        Device device = TestUtils.getDevice();
        device.id = deviceRepository.insert(device);
        Experiment experiment = TestUtils.getExperiment(device.id);
        experiment.id = experimentRepository.insert(experiment);
        run = TestUtils.getRun(0, experiment.id);
        run.id = runRepository.insert(run);
        windowId = windowRepository.insert(run.id, 0, SourceTypeEnum.BLUETOOTH_LE.name());
    }

    @Test
    public void getLiveCount() {
        try {
            assertNull(ObservableDataTestUtil.getValue(repository.getLiveCount(run.id)));
            List<BluetoothLeRecord> records = TestUtils.getBluetoothLeRecordList(windowId);
            repository.insertBluetoothLe(records);
            assertEquals(records.size(), ObservableDataTestUtil.getValue(repository.getLiveCount(run.id)).longValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertBluetoothLe() {
        List<BluetoothLeRecord> records = TestUtils.getBluetoothLeRecordList(windowId);
        long[] ids = repository.insertBluetoothLe(records);
        assertEquals(ids.length, records.size());
    }

    @Test
    public void getAllSamples() {
        List<BluetoothLeRecord> records = TestUtils.getBluetoothLeRecordList(windowId);
        repository.insertBluetoothLe(records);
        long[] runs = {run.id};
        assertEquals(records.size(), repository.getAllSamples(runs).size());
    }

    @Test
    public void insertUuids() {
        List<BluetoothLeRecord> records = TestUtils.getBluetoothLeRecordList(windowId);
        long[] ids = repository.insertBluetoothLe(records);
        List<BluetoothLeUuid> uuids = TestUtils.getBluetoothLeUuidList(ids);
        repository.insertUuids(uuids);
        List<BluetoothLeSampleRecord> samples = repository.getAllSamples(ids);
        assertTrue(samples.size()>=records.size());
    }
}