package org.jmb.table;

import org.jmb.storage.Storage;

import java.util.List;

/**
 * List backed table for sequential access of Values linked to a Key
 * @param <K>
 * @param <V>
 */
public class SequentialTable<K, V> extends Table<K, List<V>> {

    public SequentialTable(final String name, final Storage<K, List<V>> storage) {
        super(name, storage);
    }

    //TODO: Add Operations
}
