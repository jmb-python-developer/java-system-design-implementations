package org.jmb.table;

import org.jmb.storage.InMemoryStorage;
import org.jmb.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class BasicTableTest {
    private BasicTable<String, String> table;

    @BeforeEach
    void setUp() {
        Storage<String, String> storage = new InMemoryStorage<>();
        table = new BasicTable<>("test-table", storage);
    }

    @Test
    void testGetName() {
        assertEquals("test-table", table.getName());
    }

    @Test
    void testPutAndGet() {
        table.put("key1", "value1");
        assertEquals(Optional.of("value1"), table.get("key1"));
    }

    @Test
    void testGetNonExistentKey() {
        assertEquals(Optional.empty(), table.get("nonexistent"));
    }

    @Test
    void testDelete() {
        table.put("key1", "value1");
        assertTrue(table.delete("key1"));
        assertFalse(table.exists("key1"));
    }

    @Test
    void testDeleteNonExistentKey() {
        assertFalse(table.delete("nonexistent"));
    }

    @Test
    void testKeys() {
        table.put("key1", "value1");
        table.put("key2", "value2");
        List<String> keys = table.keys();
        assertEquals(2, keys.size());
        assertTrue(keys.contains("key1"));
        assertTrue(keys.contains("key2"));
    }

    @Test
    void testExists() {
        table.put("key1", "value1");
        assertTrue(table.exists("key1"));
        assertFalse(table.exists("nonexistent"));
    }
}
