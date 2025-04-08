package org.jmb.storage;

import java.util.List;
import java.util.Optional;

//TODO: To be implemented in the future.
/**
 * Represents a compressed storage implementation.
 * This class provides methods to store, retrieve, delete, and manage key-value pairs
 * with compression to save space.
 *
 * @param <K> Type for the storage keys
 * @param <V> Type for the storage values
 */
public class CompressedStorage<K, V> implements Storage<K, V> {
    @Override
    public Optional<V> get(K key) {
        return Optional.empty();
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public boolean delete(K key) {
        return false;
    }

    @Override
    public List<K> keys() {
        return null;
    }

    @Override
    public boolean exists(K key) {
        return false;
    }

    @Override
    public void clear() {

    }
}
