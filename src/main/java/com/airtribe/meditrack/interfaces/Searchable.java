package main.java.com.airtribe.meditrack.interfaces;

import java.util.List;

/**
 * Interface for entities and services that support search operations.
 * Defines contract for searching entities by various criteria.
 *
 * @param <T> the type of entity being searched
 */
public interface Searchable<T> {

    T searchById(int id);

    List<T> searchByName(String name);

    List<T> getAll();

    int getTotalCount();
}

