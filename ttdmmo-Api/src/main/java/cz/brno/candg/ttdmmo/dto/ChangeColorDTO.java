package cz.brno.candg.ttdmmo.dto;

/**
 * DTO for change color
 *
 * @author lastuvka
 */
public class ChangeColorDTO {

    private String user_id;
    private String color;

    public String getUser_id() {
        return user_id;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "ChangeColorDTO{" + "user_id=" + user_id + ", color=" + color + '}';
    }

}
