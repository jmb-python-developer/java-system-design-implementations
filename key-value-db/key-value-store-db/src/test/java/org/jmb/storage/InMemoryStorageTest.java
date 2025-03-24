package org.jmb.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryStorageTest {
    private Storage<String, String> storage;

    @BeforeEach
    void setUp() {
        storage = new InMemoryStorage<>();
    }

    @Test
    void testPutAndGet() {
        storage.put("key1", "value1");
        assertEquals(Optional.of("value1"), storage.get("key1"));
    }

    @Test
    void testGetNonExistentKey() {
        assertEquals(Optional.empty(), storage.get("nonexistent"));
    }

    @Test
    void testDelete() {
        storage.put("key1", "value1");
        assertTrue(storage.delete("key1"));
        assertFalse(storage.exists("key1"));
    }

    @Test
    void testDeleteNonExistentKey() {
        assertFalse(storage.delete("nonexistent"));
    }

    @Test
    void testKeys() {
        storage.put("key1", "value1");
        storage.put("key2", "value2");
        List<String> keys = storage.keys();
        assertEquals(2, keys.size());
        assertTrue(keys.contains("key1"));
        assertTrue(keys.contains("key2"));
    }

    @Test
    void testExists() {
        storage.put("key1", "value1");
        assertTrue(storage.exists("key1"));
        assertFalse(storage.exists("nonexistent"));
    }

    @Test
    void testClear() {
        storage.put("key1", "value1");
        storage.put("key2", "value2");
        storage.clear();
        assertTrue(storage.keys().isEmpty());
        assertFalse(storage.exists("key1"));
        assertFalse(storage.exists("key2"));
    }
}
