package com.rfdetke.digitriadlaboratory.advertisers;

import com.rfdetke.digitriadlaboratory.DatabaseTest;
import com.rfdetke.digitriadlaboratory.ObservableDataTestUtil;
import com.rfdetke.digitriadlaboratory.TestUtils;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.entities.AdvertiseConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.repositories.BluetoothLeRepository;
import com.rfdetke.digitriadlaboratory.repositories.ConfigurationRepository;
import com.rfdetke.digitriadlaboratory.repositories.DeviceRepository;
import com.rfdetke.digitriadlaboratory.repositories.ExperimentRepository;
import com.rfdetke.digitriadlaboratory.repositories.RunRepository;
import com.rfdetke.digitriadlaboratory.repositories.SourceTypeRepository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BluetoothLeAdvertiseSchedulerTest extends DatabaseTest {

    private Run run;
    private WindowConfiguration windowConfiguration;
    private AdvertiseConfiguration advertiseConfiguration;
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
        windowConfiguration = TestUtils.getWindowConfiguration(
                sourceTypeRepository.getByType(SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name()).id, experiment.id);
        windowConfiguration.id = configurationRepository.insert(windowConfiguration);
        advertiseConfiguration = TestUtils.getAdvertiseConfiguration(experiment.id);
        advertiseConfiguration.id = configurationRepository.insertAdvertise(advertiseConfiguration);
        duration = (windowConfiguration.activeTime+ windowConfiguration.inactiveTime)* windowConfiguration.windows*1000;
    }

    @Test
    public void scheduledDataCollection() {
        try {
            assertEquals(SourceTypeEnum.BLUETOOTH_LE_ADVERTISE.name(), ObservableDataTestUtil.getValue(
                    new BluetoothLeAdvertiseScheduler(run.id, windowConfiguration, advertiseConfiguration, context, db), duration));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}