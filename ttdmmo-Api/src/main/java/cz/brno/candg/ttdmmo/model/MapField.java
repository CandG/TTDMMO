package cz.brno.candg.ttdmmo.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This entity represents map field.
 *
 * @author lastuvka
 */
public class MapField {

    private String type;
    private int x;
    private int y;
    private String owner_id;
    private String city;
    private Map<String, Boolean> paths = new HashMap<String, Boolean>();

    public Map<String, Boolean> getPaths() {
        return paths;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPaths(Map<String, Boolean> paths) {
        this.paths = paths;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static String indexFromXY(int x, int y) {
        return x + ":" + y;
    }

    public static Map<String, Object> mapFromXY(int x, int y) {
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
        return "MapField{" + "type=" + type + ", x=" + x + ", y=" + y + ", owner_id=" + owner_id + ", city=" + city + ", paths=" + paths + '}';
    }

}
