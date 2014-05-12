package cz.brno.candg.ttdmmo.adapterapi;

import cz.brno.candg.ttdmmo.dto.HousePlacesDTO;
import cz.brno.candg.ttdmmo.dto.MapFieldDTO;

/**
 * Map User service adapter interface for operations on map field.
 *
 * @author lastuvka
 */
public interface MapServiceAdapter {

    /**
     * Adaptar gives all data for service method - user money and map field
     *
     * @param fbReq
     */
    void buildField(MapFieldDTO fbReq);

    /**
     * Adaptar gives all data for service method - all appropriate map fields
     *
     * @param fbReq
     */
    void spreadCity(HousePlacesDTO fbReq);

    /**
     * Adaptar gives all data for service method - house places
     *
     * @param city_id
     */
    void getHousePlaces(String city_id);
}
