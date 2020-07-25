package com.rfdetke.digitriadlaboratory;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.rfdetke.digitriadlaboratory.database.entities.Device;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DeviceEntityReadWriteTest extends DatabaseTest {

    @Test
    public void writeDeviceAndReadIt() {
        Device device = new Device("TD-1", "Samsung", "J2");
        db.getDeviceDao().insert(device);

        List<Device> devices = db.getDeviceDao().getAllDevices();

        assertEquals("Samsung", devices.get(0).brand);
    }
}
