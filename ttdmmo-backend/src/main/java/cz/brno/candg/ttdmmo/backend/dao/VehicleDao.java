package cz.brno.candg.ttdmmo.backend.dao;

import cz.brno.candg.ttdmmo.model.Vehicle;

/**
 * DAO interface for vehicles
 *
 * @author lastuvka
 */
public interface VehicleDao extends Dao<Vehicle> {

    /**
     * Change color for vehicles of user with ID
     *
     * @param id
     * @param color
     */
    void changeColor(String id, String color);
}
