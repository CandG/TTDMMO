package cz.brno.candg.ttdmmo.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lastuvka
 */
public class MapField {

    private String type;
    private int x;
    private int y;
    private Object obj;

    public MapField() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public static String indexFromXY(int x, int y) {
        return x + ":" + y;
    }

    public static Map<String, Object> MapFromXY(int x, int y) {
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("x", x);
        updates.put("y", y);
        return updates;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.x;
        hash = 89 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MapField other = (MapField) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MapField{" + "type=" + type + ", x=" + x + ", y=" + y + ", objects=" + obj + '}';
    }

}
