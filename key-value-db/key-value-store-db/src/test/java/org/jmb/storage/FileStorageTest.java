package org.jmb.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * Unit Test class for {@link FileStorage}
 */
public class FileStorageTest {

    //Testing object with cache enabled
    private Storage<String, Map<String, String>> fileStorage;

    //Testing object without cache enabled
    private Storage<String, Map<String, String>> fileStorageNoCache = new FileStorage<>(
            "aTable", ".", true
    );

    @BeforeEach
    void setUp() {
        fileStorage = new FileStorage<>(
                "aTable", ".", true
        );

        fileStorageNoCache = new FileStorage<>(
                "aTable", ".", true
        );
    }

    @Test
    void testPutFirstEntry() {
        fileStorage.put("key1", Map.of("Name", "Juan Bruno"));
        fileStorageNoCache.put("key1", Map.of("Name", "Juan Bruno"));

    }
}
