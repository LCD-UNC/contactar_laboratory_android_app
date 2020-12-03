package com.contactar.contactarlaboratory.export.csv;

import com.contactar.contactarlaboratory.DatabaseTest;
import com.contactar.contactarlaboratory.TestUtils;
import com.contactar.contactarlaboratory.database.entities.Device;
import com.contactar.contactarlaboratory.database.entities.Experiment;
import com.contactar.contactarlaboratory.database.entities.Run;
import com.contactar.contactarlaboratory.repositories.DeviceRepository;
import com.contactar.contactarlaboratory.repositories.ExperimentRepository;
import com.contactar.contactarlaboratory.repositories.RunRepository;
import com.contactar.contactarlaboratory.repositories.WindowRepository;

import org.junit.Before;

public class SampleCsvFileWriterTest extends DatabaseTest {

    protected long[] runs2;
    protected long[] runs1;
    protected WindowRepository windowRepository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        ExperimentRepository experimentRepository = new ExperimentRepository(db);
        RunRepository runRepository = new RunRepository(db);
        windowRepository = new WindowRepository(db);
        Device device = TestUtils.getDevice();
        device.id = deviceRepository.insert(device);
        Experiment experiment = TestUtils.getExperiment(device.id);
        experiment.id = experimentRepository.insert(experiment);
        Run run1 = TestUtils.getRun(0, experiment.id);

        long id1 = runRepository.insert(run1);
        runs1 = new long[]{id1};
        Run run2 = TestUtils.getRun(0, experiment.id);
        long id2 = runRepository.insert(run2);
        runs2 = new long[]{id1, id2};
    }

}
