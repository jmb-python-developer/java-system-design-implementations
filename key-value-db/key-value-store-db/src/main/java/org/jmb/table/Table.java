package org.jmb.table;

import org.jmb.storage.Storage;

import java.util.List;
import java.util.Optional;

/**
 * Represent a generic Table class.
 * @param <K> Type for the Table Keys
 * @param <V> Type for the Table Values
 */
public abstract class Table<K, V> {
    protected final String name;
    protected final Storage<K, V> storage;

    public Table(final String name, final Storage<K, V> storage) {
        this.name = name;
        this.storage = storage;
    }

    public String getName() {
        return name;
    }

    public Optional<V> get(K key) {
        return storage.get(key);
    }

    public void put(K key, V value) {
        storage.put(key, value);
    }

    public boolean delete(K key) {
        return storage.delete(key);
    }

    public List<K> keys() {
        return storage.keys();
    }

    public boolean exists(K key) {
        return storage.exists(key);
    }
}
