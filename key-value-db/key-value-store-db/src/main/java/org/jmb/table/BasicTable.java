package org.jmb.table;

import org.jmb.storage.Storage;

/**
 * Base Table class for a non-specialized Key-Value Storage. The storage can be of different types backing; however,
 * it is based on a Map for a plain Key-Document usage.
 * @param <K> The type for the storage Keys
 * @param <V> The type for the storage Values
 */
public class BasicTable<K, V> extends Table<K, V> {
    public BasicTable(String name, Storage<K, V> storage) {
        super(name, storage);
    }
}
