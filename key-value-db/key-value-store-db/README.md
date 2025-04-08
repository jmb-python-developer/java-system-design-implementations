# Key-Value Store DB

A simple Key-Value database implementation in Java.

This project allows exploring different data structure representations (`Table` types) backed by various storage mechanisms (`Storage` types).

## Features

* **Multiple Table Types:** Choose the data structure that best fits how you want to manage the value(s) associated with a key:
    * `BasicTable<K, V>`: Represents a standard key-to-single-value mapping. Ideal for simple lookups like configuration, user profiles, etc.
    * `SequentialTable<K, List<V>>`: Maps a key to an ordered `List` of values. Suitable for storing sequences, logs, or event histories where order matters.
    * *(Extensible):* The design allows for future implementations like `SortedSetTable` or others.

* **Multiple Storage Backends:** Select the persistence strategy for each table instance:
    * `InMemoryStorage`: Stores data directly in Java heap memory. Offers high speed but data is lost when the application stops.
    * `FileStorage`: Persists data to files on disk. This backend aims to implement optimized storage strategies (potentially using techniques like memory-mapped files or indexing) tailored to the specific `Table` type to ensure efficiency, especially for collection-based tables like `SequentialTable`. (Note: Optimization is ongoing).
    * `CompressedStorage`: Stores data in a compressed format, potentially saving disk space.
    * Identified via `StorageType` enum (`IN_MEMORY`, `FILE_BASED`, `COMPRESSED`).

* **Mix and Match:** Combine different `Table` implementations with different `Storage` backends based on the specific requirements of the data being stored. For example, use `InMemoryStorage` for frequently accessed cache data in a `BasicTable`, and `FileStorage` for persistent event logs in a `SequentialTable`.

**WIP**: This project is a work in progress. The current focus is on implementing the core data structures and storage mechanisms. 
The API and features are subject to change, i.e: a Querying API, Indexing, and other optimizations are planned for future iterations.

