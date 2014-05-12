package cz.brno.candg.ttdmmo.dto;

/**
 * DTO for selling vehicle
 *
 * @author lastuvka
 */
public class SellVehicleDTO {

    private String vehicle_id;
    private String user_id;

    public String getVehicle_id() {
        return vehicle_id;
    }

    public String getUser_id() {
        return user_id;
    }

    @Override
    public String toString() {
        return "SellVehicleDTO{" + "vehicle_id=" + vehicle_id + ", user_id=" + user_id + '}';
    }

}
