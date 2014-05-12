package cz.brno.candg.ttdmmo.dto;

/**
 * DTO for map field
 *
 * @author lastuvka
 */
public class MapFieldDTO {

    private String user_id;
    private String type;
    private String xy;

    public String getUser_id() {
        return user_id;
    }

    public String getType() {
        return type;
    }

    public String getXy() {
        return xy;
    }

    @Override
    public String toString() {
        return "MapFieldDTO{" + "user_id=" + user_id + ", type=" + type + ", xy=" + xy + '}';
    }

}
