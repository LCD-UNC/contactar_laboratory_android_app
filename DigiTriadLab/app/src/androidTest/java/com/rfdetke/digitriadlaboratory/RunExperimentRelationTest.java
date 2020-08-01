package com.rfdetke.digitriadlaboratory;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.rfdetke.digitriadlaboratory.constants.RunStateEnum;
import com.rfdetke.digitriadlaboratory.database.daos.DeviceDao;
import com.rfdetke.digitriadlaboratory.database.daos.ExperimentDao;
import com.rfdetke.digitriadlaboratory.database.daos.RunDao;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RunExperimentRelationTest extends DatabaseTest {

    RunDao runDao;
    ExperimentDao experimentDao;
    DeviceDao deviceDao;

    @Override
    public void setUp() {
        super.setUp();
        deviceDao = db.getDeviceDao();
        experimentDao = db.getExperimentDao();
        runDao = db.getRunDao();

        Device device = new Device("TD-2", "Samsung", "J2");
        deviceDao.insert(device);
        long deviceId = db.getDeviceDao().getAllDevices().get(0).id;

        Experiment experiment1 = new Experiment("EXP-001", "Test experiment 001", deviceId);
        Experiment experiment2 = new Experiment("EXP-002", "Test experiment 002", deviceId);

        experimentDao.insert(experiment1);
        experimentDao.insert(experiment2);

        List<Experiment> experimentList = experimentDao.getAllExperiments();

        for (int i=0; i<2; i++) {
            for (int j = 1; j <= 3; j++) {
                Date startDate = new Date();

                startDate.setTime(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * j));

                Run run = new Run(startDate, j, RunStateEnum.SCHEDULED.name(), experimentList.get(i).id);
                runDao.insert(run);
            }
        }
    }

    @Test
    public void readRunListFilteredByExperimentId() {
        List<Experiment> experimentList = experimentDao.getAllExperiments();

        assertEquals(3, runDao.getRunsByExperimentId(experimentList.get(0).id).size());
    }

    @Test
    public void readFullRunList() {
        assertEquals(6, runDao.getAllRuns().size());
    }

}
