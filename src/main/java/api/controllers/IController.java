package api.controllers;

/**
 * Common interface for all controllers
 *
 * @param <T> Type with which implementation works with
 */
public interface IController<T> {

    /**
     * Updates information about instance in database with given id
     *
     * @param id      Id of instance
     * @param content Json with list of information, which will be put in databse
     * @return Entry of database containing updated information or raises exception if update was not successful
     */
    T update(Long id, String content);

    /**
     * Deletes entry with given id from database
     *
     * @param id Id of entry in database
     */
    void delete(Long id);
}