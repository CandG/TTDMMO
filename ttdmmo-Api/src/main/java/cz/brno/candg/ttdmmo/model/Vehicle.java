package cz.brno.candg.ttdmmo.model;

/**
 * Entity for vehicle
 *
 * @author lastuvka
 */
public class Vehicle {

    private int direction;
    private int cargo;
    private int type;
    private String user_id;
    private String city_id;
    private String vehicle_id;
    private String name;
    private String color;
    private double startTime;
    private int p_size;
    private int speed;
    private int p; //for loadbalancing

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public int getP_size() {
        return p_size;
    }

    public void setP_size(int p_size) {
        this.p_size = p_size;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.vehicle_id != null ? this.vehicle_id.hashCode() : 0);
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
        final Vehicle other = (Vehicle) obj;
        if ((this.vehicle_id == null) ? (other.vehicle_id != null) : !this.vehicle_id.equals(other.vehicle_id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Vehicle{" + "direction=" + direction + ", cargo=" + cargo + ", type=" + type + ", user_id=" + user_id + ", city_id=" + city_id + ", vehicle_id=" + vehicle_id + ", name=" + name + ", color=" + color + ", startTime=" + startTime + ", p_size=" + p_size + ", speed=" + speed + ", p=" + p + '}';
    }

}
