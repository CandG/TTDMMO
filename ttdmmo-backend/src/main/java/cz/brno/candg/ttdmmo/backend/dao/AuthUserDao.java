package cz.brno.candg.ttdmmo.backend.dao;

import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.model.AuthUser;

/**
 * DAO interface for user
 *
 * @author Zdenek Lastuvka
 */
public interface AuthUserDao extends Dao<AuthUser> {

    /**
     * Get money for user with ID not concurrent safe, but simple way
     *
     * @param id
     * @param valueEventListener
     */
    void getMoney(String id, ValueEventListener valueEventListener);

    /**
     * Update money for user with ID
     *
     * @param id
     * @param money
     */
    void updateMoney(String id, int money);

    /**
     * Add vehicle for user with ID
     *
     * @param id
     * @param vehicle_id
     */
    void addVehicle(String id, String vehicle_id);

    /**
     * Remove vehicle of user with ID
     *
     * @param id
     * @param vehicle_id
     */
    void removeVehicle(String id, String vehicle_id);

    /**
     * Change color for user with ID
     *
     * @param id
     * @param color
     */
    void changeColor(String id, String color);
}
