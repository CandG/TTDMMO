package cz.brno.candg.ttdmmo.backend.dao;

import cz.brno.candg.ttdmmo.dto.HousePlacesDTO;

/**
 * DAO interface for HousePlaces
 *
 * @author lastuvka
 */
public interface HousePlacesDao extends Dao<HousePlacesDTO> {

    /**
     * Add place for given city
     *
     * @param city_id
     * @param place_id
     */
    void addPlace(String city_id, String place_id);

    /**
     * Remove place for given city
     *
     * @param city_id
     * @param place_id
     */
    void removePlace(String city_id, String place_id);
}
