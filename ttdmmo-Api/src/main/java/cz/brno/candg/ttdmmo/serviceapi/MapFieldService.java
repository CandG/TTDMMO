package cz.brno.candg.ttdmmo.serviceapi;

import cz.brno.candg.ttdmmo.dto.HousePlacesDTO;
import cz.brno.candg.ttdmmo.dto.MapFieldDTO;
import cz.brno.candg.ttdmmo.model.MapField;
import java.util.List;
import java.util.Map;

/**
 * Map service interface.
 *
 * @author lastuvka
 */
public interface MapFieldService {

    /**
     * Find position on map for given num.
     *
     * @param num
     * @return x,y coords
     */
    Map<String, Integer> findPosition(int num);

    /**
     * Generate city (map around and city), create city to Rank City name is
     * combination position and city name set by user
     *
     * @param name
     * @param x
     * @param y
     */
    void generateCity(String name, int x, int y);

    /**
     * Generate middle of map (x,y)
     *
     * @param x
     * @param y
     */
    void generateMiddle(int x, int y);

    /**
     * Generate places for city on x,y - places for buildings in future
     *
     * @param x
     * @param y
     */
    void generateHousePlaces(int x, int y);

    /**
     * Spread city if it is needed
     *
     * @param places
     * @param req
     */
    void spreadCity(List<MapField> places, HousePlacesDTO req);

    /**
     * Build or demolitation a field
     *
     * @param money
     * @param mapField
     * @param firebaseReq
     */
    void build(int money, MapField mapField, MapFieldDTO firebaseReq);

}
