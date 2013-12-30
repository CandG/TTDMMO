package cz.brno.candg.ttdmmo.backend.dao;

import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;

/**
 * DAO super-interface Extending interfaces will inherit basic CRUD operations
 * on their entities.
 *
 * @author Martin Pasko (smartly23)
 *
 * @param <T> Generic type of entity
 */
public interface Dao<T> {

    /*  Create the entity
     * @throws IllegalArgumentException if parameter is null or invalid
     */
    Long create(T entity);

    /*  Get the data to the valueEventListener
     * @throws IllegalArgumentException if parameter is null or invalid
     */
    void get(int id, ValueEventListenerWithType valueEventListener);

    /* Update the entity
     * @throws IllegalArgumentException if parameter is null, invalid or non-existent in the DB
     */
    void update(T entity);

    /* Remove the entity
     * @throws IllegalArgumentException if parameter is null or invalid. Does not throw this exception if
     * parameter is valid but given entity is nonexistent. 
     */
    void remove(int id);
}
