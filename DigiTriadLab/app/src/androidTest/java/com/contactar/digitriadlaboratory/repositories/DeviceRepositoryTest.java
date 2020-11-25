package com.contactar.digitriadlaboratory.repositories;

import com.contactar.digitriadlaboratory.DatabaseTest;
import com.contactar.digitriadlaboratory.TestUtils;
import com.contactar.digitriadlaboratory.database.entities.Device;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeviceRepositoryTest extends DatabaseTest {

    DeviceRepository repository;
    private Device device;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        repository = new DeviceRepository(db);
        device = TestUtils.getDevice();
    }

    @Test
    public void getDevice() {
        long id = repository.insert(device);
        Device device = repository.getDevice();
        assertEquals(device.id, id);
    }

    @Test
    public void getDeviceNull() {
        assertNull(repository.getDevice());
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
        Device device2 = TestUtils.getDevice();
        device2.id = id;
        repository.update(device2);
        device2 = repository.getDevice();
        assertNotEquals(device.codename, device2.codename);
    }
}