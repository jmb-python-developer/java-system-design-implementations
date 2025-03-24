package org.jmb.storage;

import java.util.List;
import java.util.Optional;

/**
 * Represents the contract for the different type of Storage implementations.
 */
public interface Storage<K, V> {
    Optional<V> get(K key);
    void put(K key, V value);
    boolean delete(K key);
    List<K> keys();
    boolean exists(K key);
    void clear();
}
