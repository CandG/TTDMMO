package cz.brno.candg.ttdmmo.model;

/**
 *
 * @author lastuvka
 */
public class BusStop {

    private int x;
    private int y;
    private int bus_stop_id;

    public BusStop() {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getBus_stop_id() {
        return bus_stop_id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.bus_stop_id;
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
        final BusStop other = (BusStop) obj;
        if (this.bus_stop_id != other.bus_stop_id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BusStop{" + "x=" + x + ", y=" + y + ", bus_stop_id=" + bus_stop_id + '}';
    }

}
