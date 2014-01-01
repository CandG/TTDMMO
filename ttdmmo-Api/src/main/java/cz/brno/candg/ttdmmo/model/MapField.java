package cz.brno.candg.ttdmmo.model;

/**
 *
 * @author lastuvka
 */
public class MapField {

    private String type;
    private int x;
    private int y;
    private Object objects;

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

    public Object getObjects() {
        return objects;
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
        return "MapField{" + "type=" + type + ", x=" + x + ", y=" + y + ", objects=" + objects + '}';
    }

}
