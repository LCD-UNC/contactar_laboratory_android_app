package com.contactar.digitriadlaboratory.export.csv;

import com.contactar.digitriadlaboratory.DatabaseTest;
import com.contactar.digitriadlaboratory.TestUtils;
import com.contactar.digitriadlaboratory.constants.SourceTypeEnum;
import com.contactar.digitriadlaboratory.database.entities.Device;
import com.contactar.digitriadlaboratory.database.entities.Experiment;
import com.contactar.digitriadlaboratory.database.entities.Run;
import com.contactar.digitriadlaboratory.repositories.DeviceRepository;
import com.contactar.digitriadlaboratory.repositories.ExperimentRepository;
import com.contactar.digitriadlaboratory.repositories.RunRepository;
import com.contactar.digitriadlaboratory.repositories.WindowRepository;

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
