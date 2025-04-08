package org.jmb.storage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Represents a file-based storage implementation.
 * This class provides methods to store, retrieve, delete, and manage key-value pairs in a file system.

 * This class also aims at a more production-grade usage, when cache is enabled.
 *
 * @param <K> Type for the storage keys
 * @param <V> Type for the storage values
 */
public class FileStorage<K, V> implements Storage<K, V> {
    private final Map<K, V> cache = new ConcurrentHashMap<>();
    private final String tableName;
    private final String directoryPath;
    private final boolean cacheEnabled;

    public FileStorage(String tableName, String directoryPath, boolean cacheEnabled) {
        this.tableName = tableName;
        this.directoryPath = directoryPath;
        this.cacheEnabled = cacheEnabled;

        //Create directory to persist the file if it doesn't exist
        try {
            Files.createDirectories(Path.of(directoryPath));
        } catch (IOException ioe) {
            //TODO: Change to use a logging framework
            System.out.println("ERROR - Error creating file-storage for Table: " + tableName);
        }
        //TODO: This could be done lazily or on-demand of certain file blocks while querying
        loadData();
    }

    /**
     * Retrieves a Table object based on a Key, which is present on the Storage (in this case a File)
     * @param key The Key linked to the Value as the persisted domain object.
     * @return The domain Object representing the key Value.
     */
    @Override
    public Optional<V> get(K key) {
        //If present in cache, return the value from there
        if (cacheEnabled && cache.containsKey(key)) {
            return Optional.ofNullable(cache.get(key));
        }
        //If not loads from disk the values
        try {
            var filePath = getFilePath(key);
            if (!Files.exists(filePath)) {
                return Optional.empty();
            }
            V value = readValue(filePath);
            if (cacheEnabled) {
                cache.put(key, value);
            }
            return Optional.ofNullable(value);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read value from file", e);
        }
    }

    /**
     * Stores a Table object based on a Key and associated Value, (in this case, flushed to a file.)
     * If the cache is enabled, saves the entity to it, uses this value as the most recent one.
     *
     * @param key The Key linked to the Value as the persisted domain object.
     */
    @Override
    public void put(K key, V value) {
        try {
            writeValue(getFilePath(key), value);
            if (cacheEnabled) {
                cache.put(key, value);
            }
        } catch (IOException e) {
            System.out.println("ERROR - Error while saving value to the filesystem");
        }
    }

    /**
     * Deletes a value associated with a key in the storage, in this case in the filesystem.
     * @param key The key of the value to delete.
     * @return whether the value was deleted or not.
     */
    @Override
    public boolean delete(K key) {
        var path = getFilePath(key);
        try {
            boolean deleted = Files.deleteIfExists(path);
            //If cache enabled, delete from it as well
            if (deleted && cacheEnabled) {
                cache.remove(key);
            }
            return deleted;
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file");
        }
    }

    @Override
    public List<K> keys() {
        try {
            var directory = Paths.get(directoryPath, tableName);
            //TODO: If no file located, must be empty storage for the passed table, log statements of WARN
            if (!Files.exists(directory)) {
                System.out.println("WARN - No file found for table");
                return new ArrayList<>();
            }
            try (var paths = Files.list(directory)) {
                return paths.map(path -> path.getFileName().toString())
                        .map(this::decodeKey)
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to list files", e);
        }
    }

    /**
     *
     * @param key Key of the stored entity to check for existence.
     * @return whether the stored entity exists in the filesystem.
     */
    @Override
    public boolean exists(K key) {
        if (cacheEnabled) {
            return cache.containsKey(key);
        }
        return Files.exists(getFilePath(key));
    }

    /**
     * This operation is similar to a drop operation, and thus it deletes ALL the values associated with a stored
     * entity, in this case in the filesystem.
     */
    @Override
    public void clear() {
        try {
            Path directory = Paths.get(directoryPath, tableName);
            if (Files.exists(directory)) {
                try (var paths = Files.list(directory)) {
                    paths.forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete file", e);
                        }
                    });
                }
            }

            if (cacheEnabled) {
                cache.clear();
            }
        } catch (IOException e) {
            System.out.println("ERROR - Failed to clear storage for table: " + tableName);
            throw new RuntimeException("Failed to clear storage for table", e);
        }
    }

    // Decodes a key back to its original form
    @SuppressWarnings("unchecked")
    private K decodeKey(final String encodedKey) {
        //TODO: increase encoding algorithm for greater security
        byte[] bytes = Base64.getDecoder().decode(encodedKey);
        return (K) new String(bytes);
    }

    private String encodeKey(final K key) {
        // Simple encoding to handle special characters in filenames
        return Base64.getEncoder().encodeToString(key.toString().getBytes());
    }

    /**
     * Loads the data into the memory data structure supporting the storage as the volatile storage, until
     * the contents are flushed into disk in other operations, provided the cache flag is enabled.
     */
    private void loadData() {
        //If no volatile storage is enabled, omit the operation
        if (!cacheEnabled) {
            return;
        }
        var keys = keys();
        //For each
        for (K key : keys) {
            get(key);
        }
    }

    private Path getFilePath(K key) {
        String encodedKey = encodeKey(key);
        var directory = Paths.get(directoryPath, tableName);
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            //TODO: Refine this exception logging and handling
            throw new RuntimeException("Failed to create directory, it might already exist", e);
        }
        return directory.resolve(encodedKey);
    }

    //Deserializes the path of a persisted entity into a domain object for a Table.
    @SuppressWarnings("unchecked")
    private V readValue(Path path) throws IOException {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (V) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new IOException("Failed to deserialize object", e);
        }
    }

    //Serializes to the path given a domain object for a Table into a stored entity.
    private void writeValue(Path path, V value) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(value);
        }
    }
}
