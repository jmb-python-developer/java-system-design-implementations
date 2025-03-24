package org.jmb.bootstrap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    @Test
    void testDatabaseBuilderDefaultValues() {
        Database database = Database.builder().build();
        assertTrue(database.isInMemory(), "Default storage should be in-memory");
    }

    @Test
    void testDatabaseBuilderCustomValues() {
        Database database = Database.builder()
                .inMemory(false)
                .build();
        assertFalse(database.isInMemory(), "Storage should not be in-memory");
    }

    @Test
    void testDatabaseBuilderFluentInterface() {
        Database.Builder builder = Database.builder();
        assertNotNull(builder, "Builder should not be null");

        Database database = builder.inMemory(true).build();
        assertNotNull(database, "Database instance should not be null");
    }
}