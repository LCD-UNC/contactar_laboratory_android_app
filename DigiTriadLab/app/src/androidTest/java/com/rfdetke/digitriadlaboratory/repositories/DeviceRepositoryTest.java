package com.rfdetke.digitriadlaboratory.repositories;

import android.os.ParcelUuid;

import com.rfdetke.digitriadlaboratory.DatabaseTest;
import com.rfdetke.digitriadlaboratory.TestEntityGenerator;
import com.rfdetke.digitriadlaboratory.database.entities.Device;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class DeviceRepositoryTest extends DatabaseTest {

    DeviceRepository repository;
    private Device device;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        repository = new DeviceRepository(db);
        device = TestEntityGenerator.getDevice();
    }

    @Test
    public void getDevice() {
        long id = repository.insert(device);
        Device device = repository.getDevice();
        assertEquals(device.id, id);
    }

    @Test
    public void insert() {
        long id = repository.insert(device);
        assertTrue(id != 0);
    }

    @Test
    public void update() {
        long id = repository.insert(device);
        device = repository.getDevice();
        Device device2 = TestEntityGenerator.getDevice();
        device2.id = id;
        repository.update(device2);
        device2 = repository.getDevice();
        assertNotEquals(device.codename, device2.codename);
    }
}