package com.rfdetke.digitriadlaboratory.export.representations;

import com.rfdetke.digitriadlaboratory.DatabaseTest;
import com.rfdetke.digitriadlaboratory.TestUtils;
import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.entities.AdvertiseConfiguration;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.WindowConfiguration;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ExperimentRepresentationTest {

    private Experiment experiment;
    private WindowConfiguration wifiWindow;
    private WindowConfiguration bluetoothWindow;
    private WindowConfiguration bluetoothLeWindow;
    private WindowConfiguration sensorWindow;
    private WindowConfiguration advertiseWindow;
    private AdvertiseConfiguration advertiseConfiguration;
    private List<String> tags;

    @Before
    public void setUp() throws Exception {
        experiment = TestUtils.getExperiment(1);
        wifiWindow = TestUtils.getWindowConfiguration(1, 1);
        bluetoothWindow = TestUtils.getWindowConfiguration(2,1);
        bluetoothLeWindow = TestUtils.getWindowConfiguration(3, 1);
        sensorWindow = TestUtils.getWindowConfiguration(5, 1);
        advertiseWindow = TestUtils.getWindowConfiguration(4, 1);
        advertiseConfiguration = TestUtils.getAdvertiseConfiguration(1);
        tags =  TestUtils.getRandomStringList(5,10);
    }

    @Test
    public void testEquals() {
        ExperimentRepresentation representation1 = new ExperimentRepresentation(
                experiment, wifiWindow, bluetoothWindow, bluetoothLeWindow, sensorWindow,
                advertiseWindow, advertiseConfiguration, tags );
        ExperimentRepresentation representation2 = new ExperimentRepresentation(
                experiment, wifiWindow, bluetoothWindow, bluetoothLeWindow, sensorWindow,
                advertiseWindow, advertiseConfiguration, tags );
        assertEquals(representation1, representation2);
    }

    @Test
    public void toJson() {
        ExperimentRepresentation representation = new ExperimentRepresentation(
                experiment, wifiWindow, bluetoothWindow, bluetoothLeWindow, sensorWindow,
                advertiseWindow, advertiseConfiguration, tags );
        assertNotNull(representation.toJson());
    }

    @Test
    public void objectHashCode() {
        ExperimentRepresentation representation = new ExperimentRepresentation(
                experiment, wifiWindow, bluetoothWindow, bluetoothLeWindow, sensorWindow,
                advertiseWindow, advertiseConfiguration, tags );
        assertTrue(representation.hashCode()!=0);
    }
}