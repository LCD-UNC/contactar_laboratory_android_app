package com.contactar.digitriadlaboratory.scanners.bluetooth;

import com.contactar.digitriadlaboratory.DatabaseTest;
import com.contactar.digitriadlaboratory.ObservableDataTestUtil;
import com.contactar.digitriadlaboratory.TestUtils;
import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.entities.Device;
import com.contactar.digitriadlaboratory.database.entities.Experiment;
import com.contactar.digitriadlaboratory.database.entities.Run;
import com.contactar.digitriadlaboratory.database.entities.WindowConfiguration;
import com.contactar.digitriadlaboratory.repositories.BluetoothLeRepository;
import com.contactar.digitriadlaboratory.repositories.BluetoothRepository;
import com.contactar.digitriadlaboratory.repositories.ConfigurationRepository;
import com.contactar.digitriadlaboratory.repositories.DeviceRepository;
import com.contactar.digitriadlaboratory.repositories.ExperimentRepository;
import com.contactar.digitriadlaboratory.repositories.RunRepository;
import com.contactar.digitriadlaboratory.repositories.SourceTypeRepository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BluetoothLeScanSchedulerTest extends DatabaseTest {

    private Run run;
    private WindowConfiguration configuration;
    private BluetoothLeRepository repository;
    private long duration;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        ExperimentRepository experimentRepository = new ExperimentRepository(db);
        RunRepository runRepository = new RunRepository(db);
        ConfigurationRepository configurationRepository = new ConfigurationRepository(db);
        SourceTypeRepository sourceTypeRepository = new SourceTypeRepository(db);
        repository = new BluetoothLeRepository(db);
        Device device = TestUtils.getDevice();
        device.id = deviceRepository.insert(device);
        Experiment experiment = TestUtils.getExperiment(device.id);
        experiment.id = experimentRepository.insert(experiment);
        run = TestUtils.getRun(0, experiment.id);
        run.id = runRepository.insert(run);
        configuration = TestUtils.getWindowConfiguration(
                sourceTypeRepository.getByType(SourceTypeEnum.BLUETOOTH_LE.name()).id, experiment.id);
        configuration.id = configurationRepository.insert(configuration);
        duration = (configuration.activeTime+configuration.inactiveTime)*configuration.windows*1000;
    }

    @Test
    public void scheduledDataCollection() {
        long[] runs = {run.id};
        try {
            assertEquals(SourceTypeEnum.BLUETOOTH_LE.name(), ObservableDataTestUtil.getValue(
                    new BluetoothLeScanScheduler(run.id, configuration, context, db), duration));
            assertNotNull(repository.getAllSamples(runs));
            assertTrue(repository.getAllSamples(runs).size()>0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}