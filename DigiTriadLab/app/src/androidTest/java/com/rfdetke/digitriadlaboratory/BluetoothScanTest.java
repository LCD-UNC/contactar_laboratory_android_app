package com.rfdetke.digitriadlaboratory;

import android.util.Log;

import com.rfdetke.digitriadlaboratory.constants.SourceTypeEnum;
import com.rfdetke.digitriadlaboratory.database.daos.BluetoothRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.SampleDao;
import com.rfdetke.digitriadlaboratory.database.daos.SensorRecordDao;
import com.rfdetke.digitriadlaboratory.database.daos.SourceTypeDao;
import com.rfdetke.digitriadlaboratory.database.entities.Device;
import com.rfdetke.digitriadlaboratory.database.entities.Experiment;
import com.rfdetke.digitriadlaboratory.database.entities.Run;
import com.rfdetke.digitriadlaboratory.database.entities.ScanConfiguration;
import com.rfdetke.digitriadlaboratory.scanners.bluetooth.BluetoothScanDataBucket;
import com.rfdetke.digitriadlaboratory.scanners.bluetooth.BluetoothScanScheduler;
import com.rfdetke.digitriadlaboratory.scanners.wifi.WifiScanScheduler;

import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BluetoothScanTest extends DatabaseTest{

    SampleDao sampleDao;
    SourceTypeDao sourceTypeDao;
    BluetoothRecordDao bluetoothRecordDao;
    SensorRecordDao sensorRecordDao;

    long deviceId;
    long experimentId;
    long runId;

    @Override
    public void setUp() {
        super.setUp();
        sampleDao = db.getSampleDao();
        sourceTypeDao = db.getSourceTypeDao();
        bluetoothRecordDao = db.getBluetoothRecordDao();
        sensorRecordDao = db.getSensorRecordDao();

        deviceId = db.getDeviceDao().insert(new Device("T-1", "SAMSUNG", "A50"));
        experimentId = db.getExperimentDao().insert(new Experiment("EXP-001", "aaaa", deviceId));
        runId = db.getRunDao().insert(new Run(new Date(), 1, experimentId));

        ScanConfiguration scanConfiguration = new ScanConfiguration();
        scanConfiguration.activeTime = 10000;
        scanConfiguration.inactiveTime = 2000;
        scanConfiguration.windows = 1;

        BluetoothScanScheduler scanScheduler = new BluetoothScanScheduler(runId, scanConfiguration,
                context, sampleDao, sourceTypeDao, bluetoothRecordDao, sensorRecordDao);

        try {
            Thread.sleep(20000);
            Log.d("h","k");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBluetoothRecordsInsertion() {
        long[] runs = {runId};

        List<SampleDao.BluetoothSampleRecord> samples = db.getSampleDao().getBluetoothSamplesRecords(runs);

        assertFalse(samples.isEmpty());
    }

    @Test
    public void testSensorRecordsInsertion() {
        long[] runs = {runId};

        List<SampleDao.SensorSampleRecord> sensorSampleRecords = db.getSampleDao().getSensorSamplesRecords(runs, SourceTypeEnum.BLUETOOTH.name());

        assertFalse(sensorSampleRecords.isEmpty());
    }

    @Test
    public void testSameSamplesLength() {
        long[] runs = {runId};
        long[] bluetoothSampleIds = db.getSampleDao().getDistinctBluetoothSampleIds(runs);
        long[] sensorSampleIds = db.getSampleDao().getDistinctSensorSampleIds(runs, SourceTypeEnum.BLUETOOTH.name());

        assertEquals(sensorSampleIds.length, bluetoothSampleIds.length);
    }

    @Test
    public void testSameSampleIds() {
        long[] runs = {runId};
        long[] bluetoothSampleIds = db.getSampleDao().getDistinctBluetoothSampleIds(runs);
        long[] sensorSampleIds = db.getSampleDao().getDistinctSensorSampleIds(runs, SourceTypeEnum.BLUETOOTH.name());

        assertEquals(sensorSampleIds[0], bluetoothSampleIds[0]);
    }

}
