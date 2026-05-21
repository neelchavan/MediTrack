package main.java.com.airtribe.meditrack.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic in-memory data store for storing and managing entities.
 * Provides CRUD operations for any entity type with ID field.
 *
 * @param <T> the type of entity stored in this data store
 */
public class DataStore<T> {
    private final Map<Integer, T> storage;

    /**
     * Initializes an empty DataStore.
     */
    public DataStore() {
        this.storage = new HashMap<>();
    }

    /**
     * Adds a new entity to the data store.
     * The entity is stored using its ID as key (assumes T has getId() method).
     *
     * @param id the unique identifier for the entity
     * @param entity the entity to add
     * @throws IllegalArgumentException if ID is invalid or entity already exists
     */
    public void add(int id, T entity) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive integer");
        }
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if (storage.containsKey(id)) {
            throw new IllegalArgumentException("Entity with ID " + id + " already exists");
        }
        storage.put(id, entity);
    }

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the unique identifier of the entity
     * @return the entity if found, null otherwise
     */
    public T get(int id) {
        return storage.get(id);
    }

    /**
     * Updates an existing entity in the data store.
     *
     * @param id the unique identifier of the entity to update
     * @param entity the updated entity
     * @throws IllegalArgumentException if entity doesn't exist or entity is null
     */
    public void update(int id, T entity) {
        if (!storage.containsKey(id)) {
            throw new IllegalArgumentException("Entity with ID " + id + " does not exist");
        }
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        storage.put(id, entity);
    }

    /**
     * Deletes an entity from the data store.
     *
     * @param id the unique identifier of the entity to delete
     * @return the deleted entity if found, null otherwise
     */
    public T delete(int id) {
        return storage.remove(id);
    }

    /**
     * Checks if an entity with the given ID exists.
     *
     * @param id the unique identifier to check
     * @return true if entity exists, false otherwise
     */
    public boolean exists(int id) {
        return storage.containsKey(id);
    }

    /**
     * Gets all entities stored in this data store.
     *
     * @return a list containing all entities
     */
    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    /**
     * Gets all entity IDs stored in this data store.
     *
     * @return a list of all IDs
     */
    public List<Integer> getAllIds() {
        return new ArrayList<>(storage.keySet());
    }

    /**
     * Returns the number of entities in the data store.
     *
     * @return the count of stored entities
     */
    public int size() {
        return storage.size();
    }

    /**
     * Checks if the data store is empty.
     *
     * @return true if no entities are stored, false otherwise
     */
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    /**
     * Clears all entities from the data store.
     */
    public void clear() {
        storage.clear();
    }

    /**
     * Gets a copy of the internal storage map.
     * Useful for batch operations or analysis.
     *
     * @return a new map containing all stored entities
     */
    public Map<Integer, T> getStorageMap() {
        return new HashMap<>(storage);
    }

    @Override
    public String toString() {
        return "DataStore{" +
                "size=" + storage.size() +
                ", isEmpty=" + storage.isEmpty() +
                '}';
    }
}

