package cz.brno.candg.ttdmmo.backend.dao;

import com.firebase.client.ValueEventListener;

/**
 * DAO super-interface Extending interfaces will inherit basic CRUD operations
 * on their entities.
 *
 * @author Lastuvka
 *
 * @param <T> Generic type of entity
 */
public interface Dao<T> {

    /**
     * Create the entity
     *
     * @param entity
     * @return
     */
    String create(T entity);

    /**
     * Get the data to the valueEventListener
     *
     * @param id
     * @param valueEventListener
     */
    void get(String id, ValueEventListener valueEventListener);

    /**
     * Update the entity
     *
     * @param entity
     */
    void update(T entity);

    /**
     * Remove the entity
     *
     * @param id
     */
    void remove(String id);
}
