package com.rfdetke.digitriadlaboratory.scanners.sensors;

import com.rfdetke.digitriadlaboratory.DatabaseTest;
import com.rfdetke.digitriadlaboratory.ObservableDataTestUtil;
import com.rfdetke.digitriadlaboratory.TestUtils;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.ConfigurationRepository;
import com.rfdetke.digitriadlaboratory.repositories.DeviceRepository;
import com.rfdetke.digitriadlaboratory.repositories.ExperimentRepository;
import com.rfdetke.digitriadlaboratory.repositories.RunRepository;
import com.rfdetke.digitriadlaboratory.repositories.SensorRepository;
import com.rfdetke.digitriadlaboratory.repositories.SourceTypeRepository;
import com.rfdetke.digitriadlaboratory.repositories.WifiRepository;
import com.rfdetke.digitriadlaboratory.scanners.wifi.WifiScanScheduler;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SensorsScanSchedulerTest extends DatabaseTest {

    private Run run;
    private WindowConfiguration configuration;
    private SensorRepository repository;
    private long duration;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        ExperimentRepository experimentRepository = new ExperimentRepository(db);
        RunRepository runRepository = new RunRepository(db);
        ConfigurationRepository configurationRepository = new ConfigurationRepository(db);
        SourceTypeRepository sourceTypeRepository = new SourceTypeRepository(db);
        repository = new SensorRepository(db);
        Device device = TestUtils.getDevice();
        device.id = deviceRepository.insert(device);
        Experiment experiment = TestUtils.getExperiment(device.id);
        experiment.id = experimentRepository.insert(experiment);
        run = TestUtils.getRun(0, experiment.id);
        run.id = runRepository.insert(run);
        configuration = TestUtils.getWindowConfiguration(
                sourceTypeRepository.getByType(SourceTypeEnum.SENSORS.name()).id,experiment.id);
        configuration.id = configurationRepository.insert(configuration);
        duration = (configuration.activeTime+configuration.inactiveTime)*configuration.windows*1000;
    }

    @Test
    public void scheduledDataCollection() {
        long[] runs = {run.id};
        try {
            assertEquals(SourceTypeEnum.SENSORS.name(), ObservableDataTestUtil.getValue(
                    new SensorsScanScheduler(run.id, configuration, context, db), duration));
            assertNotNull(repository.getAllSamples(runs));
            assertTrue(repository.getAllSamples(runs).size()>0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}