package cz.brno.candg.ttdmmo.serviceapi.dto;

/**
 *
 * @author lastuvka
 */
public class MapFieldDto {

    private String type;
    private int x;
    private int y;
    private Object objects;

    public MapFieldDto() {
    }

    public MapFieldDto(String type, int x, int y, Object objects) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.objects = objects;
    }

    @Override
    public String toString() {
        return "MapField{" + "type=" + type + ", x=" + x + ", y=" + y + ", objects=" + objects + '}';
    }

}
