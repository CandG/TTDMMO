package cz.brno.candg.ttdmmo.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for path
 *
 * @author lastuvka
 */
public class PathDTO {

    private final List<String> stops = new ArrayList<String>();
    private String vehicle_id;

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public List<String> getStops() {
        return stops;
    }

    @Override
    public String toString() {
        return "PathDTO{" + "stops=" + stops + ", vehicle_id=" + vehicle_id + '}';
    }

}
