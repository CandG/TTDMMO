package cz.brno.candg.ttdmmo.backend.dao;

import cz.brno.candg.ttdmmo.backend.adapter.MapServiceAdapterImpl;
import cz.brno.candg.ttdmmo.model.City;

/**
 * DAO interface for City
 *
 * @author lastuvka
 */
public interface CityDao extends Dao<City> {

    /**
     * Add food, update rank and people if needed
     *
     * @param id
     * @param food
     * @param mapServiceAdapter
     */
    void changeFood(String id, int food, MapServiceAdapterImpl mapServiceAdapter);

    /**
     * Add Wood, update rank and people if needed
     *
     * @param id
     * @param wood
     * @param mapServiceAdapter
     */
    void changeWood(String id, int wood, MapServiceAdapterImpl mapServiceAdapter);
}
