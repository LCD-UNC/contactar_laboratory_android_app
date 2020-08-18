package com.rfdetke.digitriadlaboratory.repositories;

import com.rfdetke.digitriadlaboratory.DatabaseTest;
import com.rfdetke.digitriadlaboratory.TestUtils;
import com.rfdetke.digitriadlaboratory.database.entities.SourceType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SourceTypeRepositoryTest extends DatabaseTest {

    private SourceTypeRepository repository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        repository = new SourceTypeRepository(db);
    }
    @Test
    public void getById() {
        SourceType sourceType = TestUtils.getSourceType();
        sourceType.id = repository.insert(sourceType);
        assertEquals(sourceType.type, repository.getById(sourceType.id).type);
    }

    @Test
    public void getByType() {
        SourceType sourceType = TestUtils.getSourceType();
        sourceType.id = repository.insert(sourceType);
        assertEquals(sourceType.id, repository.getByType(sourceType.type).id);
    }

    @Test
    public void insert() {
        long id = repository.insert(TestUtils.getSourceType());
        assertTrue(id != 0);
    }
}