package com.contactar.contactarlaboratory.scanners.wifi;

import com.contactar.contactarlaboratory.DatabaseTest;
import com.contactar.contactarlaboratory.ObservableDataTestUtil;
import com.contactar.contactarlaboratory.TestUtils;
import com.contactar.contactarlaboratory.constants.SourceTypeEnum;
import com.contactar.contactarlaboratory.database.entities.Device;
import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.database.entities.Run;
import com.contactar.contactarlaboratory.database.entities.WindowConfiguration;
import com.contactar.contactarlaboratory.repositories.ConfigurationRepository;
import com.contactar.contactarlaboratory.repositories.DeviceRepository;
import com.contactar.contactarlaboratory.repositories.ExperimentRepository;
import com.contactar.contactarlaboratory.repositories.RunRepository;
import com.contactar.contactarlaboratory.repositories.SourceTypeRepository;
import com.contactar.contactarlaboratory.repositories.WifiRepository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WifiScanSchedulerTest extends DatabaseTest {

    private Run run;
    private WindowConfiguration configuration;
    private WifiRepository repository;
    private long duration;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        ExperimentRepository experimentRepository = new ExperimentRepository(db);
        RunRepository runRepository = new RunRepository(db);
        ConfigurationRepository configurationRepository = new ConfigurationRepository(db);
        SourceTypeRepository sourceTypeRepository = new SourceTypeRepository(db);
        repository = new WifiRepository(db);
        Device device = TestUtils.getDevice();
        device.id = deviceRepository.insert(device);
        Experiment experiment = TestUtils.getExperiment(device.id);
        experiment.id = experimentRepository.insert(experiment);
        run = TestUtils.getRun(0, experiment.id);
        run.id = runRepository.insert(run);
        configuration = TestUtils.getWindowConfiguration(
                sourceTypeRepository.getByType(SourceTypeEnum.WIFI.name()).id,experiment.id);
        configuration.id = configurationRepository.insert(configuration);
        duration = (configuration.activeTime+configuration.inactiveTime)*configuration.windows*1000;
    }

    @Test
    public void scheduledDataCollection() {
        long[] runs = {run.id};
        try {
            assertEquals(SourceTypeEnum.WIFI.name(), ObservableDataTestUtil.getValue(
                    new WifiScanScheduler(run.id, configuration, context, db), duration));
            assertNotNull(repository.getAllSamples(runs));
            assertTrue(repository.getAllSamples(runs).size()>0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}