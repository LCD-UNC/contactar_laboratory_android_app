package com.rfdetke.digitriadlaboratory.qr;

import com.google.gson.Gson;
import com.rfdetke.digitriadlaboratory.DatabaseTest;
import com.rfdetke.digitriadlaboratory.TestUtils;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;
import com.rfdetke.digitriadlaboratory.export.representations.ExperimentRepresentation;
import com.rfdetke.digitriadlaboratory.repositories.ConfigurationRepository;
import com.rfdetke.digitriadlaboratory.repositories.DeviceRepository;
import com.rfdetke.digitriadlaboratory.repositories.ExperimentRepository;
import com.rfdetke.digitriadlaboratory.repositories.RunRepository;
import com.rfdetke.digitriadlaboratory.repositories.SourceTypeRepository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QRTest extends DatabaseTest {

    private Experiment experiment;
    private ConfigurationRepository configurationRepository;
    private ExperimentRepresentation representation;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        DeviceRepository deviceRepository = new DeviceRepository(db);
        ExperimentRepository experimentRepository = new ExperimentRepository(db);
        RunRepository runRepository = new RunRepository(db);
        configurationRepository = new ConfigurationRepository(db);
        SourceTypeRepository sourceTypeRepository = new SourceTypeRepository(db);
        Device device = TestUtils.getDevice();
        device.id = deviceRepository.insert(device);
        experiment = TestUtils.getExperiment(device.id);
        experiment.id = experimentRepository.insert(experiment);
        Run run = TestUtils.getRun(0, experiment.id);
        run.id = runRepository.insert(run);
        WindowConfiguration configuration = TestUtils.getWindowConfiguration(
                sourceTypeRepository.getByType(SourceTypeEnum.WIFI.name()).id, experiment.id);
        configuration.id = configurationRepository.insert(configuration);
        representation = new ExperimentRepresentation(experiment, null,
                null, null, null,
                null,null,null, null, null);
    }

    @Test
    public void getQrExperiment() {
        assertNotNull(QR.getQrExperiment(experiment, configurationRepository, null, null));
    }

    @Test
    public void getCodedExperiment() {
        Gson gson = new Gson();
        String string = gson.toJson(representation, ExperimentRepresentation.class);
        assertEquals(representation, ExperimentRepresentation.getCodedExperiment(string));
    }
}