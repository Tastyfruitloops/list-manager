package com.list.manager.api.controllers;

/**
 * Common interface for all controllers
 *
 * @param <T> Type with which implementation works with
 */
public interface IController<T> {

    /**
     * Gets information about instance from database with given id
     *
     * @param id      Id of instance
     * @return Entry of database containing requested information
     */
    T getById(Long id);

    T update(Long id, String attributes);

    /**
     * Deletes entry with given id from database
     *
     * @param id Id of entry in database
     */
    void delete(Long id);
}