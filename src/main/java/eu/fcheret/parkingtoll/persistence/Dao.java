package eu.fcheret.parkingtoll.persistence;

import java.util.Collection;

public interface Dao<T> {

    /**
     * Get all currently stored parking lots
     * @return all currently stored parking lots
     */
    Collection<T> getAll();

    /**
     * Adds an object in database and assigns it a new ID
     * @param t the object to add
     * @return the added object
     */
    T save(T t);

    /**
     * Search an object by id
     * @param id the id to look for
     * @return the requested object object
     */
    T findById(Long id);

    /**
     * Update the specified object in database.
     * @param t the value of object to add
     */
    void update(Long id, T t);

    /**
     * Delete object by id
     * @param id of the object to delete
     */
    void delete(Long id);

    /**
     * Clear all data stored in the database
     */
    void clearDatabase();

}
