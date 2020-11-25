package com.contactar.digitriadlaboratory.repositories;

import com.contactar.digitriadlaboratory.DatabaseTest;
import com.contactar.digitriadlaboratory.TestUtils;
import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.entities.AdvertiseConfiguration;
import com.contactar.digitriadlaboratory.database.entities.Device;
import com.contactar.digitriadlaboratory.database.entities.Experiment;
import com.contactar.digitriadlaboratory.database.entities.SourceType;
import com.contactar.digitriadlaboratory.database.entities.WindowConfiguration;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigurationRepositoryTest extends DatabaseTest {

    private Experiment experiment;
    private ConfigurationRepository repository;
    private SourceTypeRepository sourceTypeRepository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        ExperimentRepository experimentRepository = new ExperimentRepository(db);
        sourceTypeRepository = new SourceTypeRepository(db);
        repository = new ConfigurationRepository(db);
        Device device = TestUtils.getDevice();
        device.id = deviceRepository.insert(device);
        experiment = TestUtils.getExperiment(device.id);
        experiment.id = experimentRepository.insert(experiment);
    }

    @Test
    public void getConfigurationsForExperiment() {
        SourceType sourceType = sourceTypeRepository.getByType(SourceTypeEnum.WIFI.name());
        WindowConfiguration configuration = TestUtils.getWindowConfiguration(sourceType.id,experiment.id);
        repository.insert(configuration);
        sourceType = sourceTypeRepository.getByType(SourceTypeEnum.BLUETOOTH.name());
        configuration = TestUtils.getWindowConfiguration(sourceType.id,experiment.id);
        repository.insert(configuration);
        assertEquals(2, repository.getConfigurationsForExperiment(experiment.id).size());
    }

    @Test
    public void emptyConfigs() {
        assertTrue(repository.getConfigurationsForExperiment(experiment.id).isEmpty());
    }

    @Test
    public void insert() {
        SourceType sourceType = sourceTypeRepository.getByType(SourceTypeEnum.WIFI.name());
        WindowConfiguration configuration = TestUtils.getWindowConfiguration(sourceType.id,experiment.id);
        repository.insert(configuration);
        assertFalse(repository.getConfigurationsForExperiment(experiment.id).isEmpty());
    }

    @Test
    public void delete() {
        SourceType sourceType = sourceTypeRepository.getByType(SourceTypeEnum.WIFI.name());
        WindowConfiguration configuration = TestUtils.getWindowConfiguration(sourceType.id,experiment.id);
        configuration.id = repository.insert(configuration);
        repository.delete(configuration);
        assertTrue(repository.getConfigurationsForExperiment(experiment.id).isEmpty());
    }

    @Test
    public void getConfigurationForExperimentByType() {
        SourceType sourceType = sourceTypeRepository.getByType(SourceTypeEnum.WIFI.name());
        WindowConfiguration configuration = TestUtils.getWindowConfiguration(sourceType.id,experiment.id);
        repository.insert(configuration);
        assertNotNull(repository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.WIFI.name()));
    }

    @Test
    public void getConfigurationForExperimentByTypeThatNotExist() {
        SourceType sourceType = sourceTypeRepository.getByType(SourceTypeEnum.WIFI.name());
        WindowConfiguration configuration = TestUtils.getWindowConfiguration(sourceType.id,experiment.id);
        repository.insert(configuration);
        assertNull(repository.getConfigurationForExperimentByType(experiment.id, SourceTypeEnum.BLUETOOTH.name()));
    }

    @Test
    public void insertAdvertise() {
        AdvertiseConfiguration configuration = TestUtils.getAdvertiseConfiguration(experiment.id);
        repository.insertAdvertise(configuration);
        assertNotNull(repository.getBluetoothLeAdvertiseConfigurationFor(experiment.id));
    }

    @Test
    public void nonExistentAdvertise() {
        assertNull(repository.getBluetoothLeAdvertiseConfigurationFor(experiment.id));
    }
}