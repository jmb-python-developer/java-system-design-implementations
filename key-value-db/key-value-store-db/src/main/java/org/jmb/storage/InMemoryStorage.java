package org.jmb.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents and In Memory Storage, backed by a hash based data structure.
 */
public class InMemoryStorage<K, V> implements Storage<K, V> {

    //Concurrency is done through Java's native concurrent classes
    private final Map<K, V> store = new ConcurrentHashMap<>();

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(store.get(key));
    }

    @Override
    public void put(K key, V value) {
        store.put(key, value);
    }

    @Override
    public boolean delete(K key) {
        return store.remove(key) != null;
    }

    @Override
    public List<K> keys() {
        return new ArrayList<>(store.keySet());
    }

    @Override
    public boolean exists(K key) {
        return store.containsKey(key);
    }

    @Override
    public void clear() {
        store.clear();
    }
}
