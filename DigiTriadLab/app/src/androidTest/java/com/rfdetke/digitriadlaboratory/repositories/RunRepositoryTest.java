package com.rfdetke.digitriadlaboratory.repositories;

import com.rfdetke.digitriadlaboratory.DatabaseTest;
import com.rfdetke.digitriadlaboratory.ObservableDataTestUtil;
import com.rfdetke.digitriadlaboratory.TestUtils;
import com.rfdetke.digitriadlaboratory.constants.RunStateEnum;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RunRepositoryTest extends DatabaseTest {

    private Experiment experiment;
    private RunRepository repository;
    private WindowConfiguration configA;
    private WindowConfiguration configB;
    private WindowConfiguration configC;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        ExperimentRepository experimentRepository = new ExperimentRepository(db);
        ConfigurationRepository configurationRepository = new ConfigurationRepository(db);
        SourceTypeRepository sourceTypeRepository = new SourceTypeRepository(db);
        repository = new RunRepository(db);
        Device device = TestUtils.getDevice();
        device.id = deviceRepository.insert(device);
        experiment = TestUtils.getExperiment(device.id);
        experiment.id = experimentRepository.insert(experiment);
        configA = TestUtils.getWindowConfiguration(sourceTypeRepository.getByType(SourceTypeEnum.WIFI.name()).id,experiment.id);
        configA.id = configurationRepository.insert(configA);
        configB = TestUtils.getWindowConfiguration(sourceTypeRepository.getByType(SourceTypeEnum.BLUETOOTH.name()).id,experiment.id);
        configB.id = configurationRepository.insert(configB);
        configC = TestUtils.getWindowConfiguration(sourceTypeRepository.getByType(SourceTypeEnum.BLUETOOTH_LE.name()).id,experiment.id);
        configC.id = configurationRepository.insert(configC);
    }

    @Test
    public void updateState() {
        Run run = TestUtils.getRun(0, experiment.id);
        run.id = repository.insert(run);
        repository.updateState(run.id, RunStateEnum.RUNNING.name());
        assertNotEquals(run.state, repository.getById(run.id).state);
    }

    @Test
    public void getRunForExperiment() {
        try {
            assertEquals(0, ObservableDataTestUtil.getValue(repository.getLiveRunsForExperiment(experiment.id)).size());
            List<Run> runs = TestUtils.getRunList(experiment.id);
            for(Run run : runs) {
                repository.insert(run);
            }
            assertEquals(runs.size(), ObservableDataTestUtil.getValue(repository.getLiveRunsForExperiment(experiment.id)).size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void scheduledOrRunningEmpty() {
        assertEquals(0, repository.getCurrentScheduledOrRunningStartAndDuration().size());
    }

    @Test
    public void scheduledOrRunningNotEmpty() {
        repository.insert(TestUtils.getRun(0, experiment.id));
        assertEquals(1, repository.getCurrentScheduledOrRunningStartAndDuration().size());
    }

    @Test
    public void getMaxDuration() {
        long max = repository.getMaxDurationForExperiment(experiment.id);
        long durationA = (configA.activeTime+configA.inactiveTime)*configA.windows;
        long durationB = (configB.activeTime+configB.inactiveTime)*configB.windows;
        long durationC = (configC.activeTime+configC.inactiveTime)*configC.windows;
        assertTrue(max>=durationA && max>=durationB && max>=durationC);
    }

    @Test
    public void getLiveRun() {
        try {
            long id = repository.insert(TestUtils.getRun(0, experiment.id));
            assertEquals(RunStateEnum.SCHEDULED.name(), ObservableDataTestUtil.getValue(repository.getLiveById(id)).state);
            repository.updateState(id, RunStateEnum.RUNNING.name());
            assertEquals(RunStateEnum.RUNNING.name(), ObservableDataTestUtil.getValue(repository.getLiveById(id)).state);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getLastRunForExperimentNonZero() {
        repository.insert(TestUtils.getRun(0, experiment.id));
        long n = repository.getLastRunForExperiment(experiment.id);
        assertNotEquals(n, 0);
    }

    @Test
    public void getLastRunForExperimentZero() {
        long n = repository.getLastRunForExperiment(experiment.id);
        assertEquals(n, 0);
    }

    @Test
    public void getByIdNotNull() {
        Run run = TestUtils.getRun(0, experiment.id);
        run.id = repository.insert(run);
        assertEquals(run.id, repository.getById(run.id).id);
    }

    @Test
    public void getByIdNull() {
        assertNull(repository.getById(TestUtils.getRandomInt()));
    }

    @Test
    public void insert() {
        long id = repository.insert(TestUtils.getRun(0, experiment.id));
        assertTrue(id != 0);
    }

    @Test
    public void delete() {
        Run run = TestUtils.getRun(0, experiment.id);
        run.id = repository.insert(run);
        repository.delete(run);
        assertNull(repository.getById(run.id));
    }
}