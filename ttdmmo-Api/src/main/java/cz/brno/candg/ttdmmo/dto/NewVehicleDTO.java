package cz.brno.candg.ttdmmo.dto;

/**
 * DTO for new vehicle
 *
 * @author lastuvka
 */
public class NewVehicleDTO {

    private String user_id;
    private int type;
    private String name;
    private String xy;

    public int getType() {
        return type;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getXy() {
        return xy;
    }

    @Override
    public String toString() {
        return "NewVehicleDTO{" + "user_id=" + user_id + ", type=" + type + ", name=" + name + ", xy=" + xy + '}';
    }

}
