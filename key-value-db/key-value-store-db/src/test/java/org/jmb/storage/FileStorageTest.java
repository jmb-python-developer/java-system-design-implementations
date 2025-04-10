package org.jmb.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit Test class for {@link FileStorage}
 */
public class FileStorageTest {

    //Testing object with cache enabled
    private Storage<String, Map<String, String>> fileStorage;

    //Testing object without cache enabled
    private Storage<String, Map<String, String>> fileStorageNoCache;

    @BeforeEach
    void setUp() {
        fileStorage = new FileStorage<>(
                "aTable", "key-value-db", false
        );

        fileStorageNoCache = new FileStorage<>(
                "aTableCached", "key-value-db-cached", true
        );
    }

    @Test
    void testPutFirstEntry() {
        fileStorage.put("key1", Map.of("Name", "Juan Bruno"));
        fileStorage.put("key2", Map.of("Name", "Carlos Bruno"));
        fileStorageNoCache.put("key1", Map.of("Name", "Juan Bruno"));
        var savedValue = fileStorage.get("key1");
        assertEquals(savedValue.get(), Map.of("Name", "Juan Bruno"));
    }
}
