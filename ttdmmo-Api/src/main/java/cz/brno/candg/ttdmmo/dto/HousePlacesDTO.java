package cz.brno.candg.ttdmmo.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * DTO for housePlaces
 *
 * @author lastuvka
 */
public class HousePlacesDTO {

    private Map<String, Boolean> places = new HashMap<String, Boolean>();
    private String city_id;

    public Map<String, Boolean> getPlaces() {
        return places;
    }

    public void setPlaces(Map<String, Boolean> places) {
        this.places = places;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String user_id) {
        this.city_id = user_id;
    }

    @Override
    public String toString() {
        return "HousePlacesDTO{" + "places=" + places + ", city_id=" + city_id + '}';
    }

}
