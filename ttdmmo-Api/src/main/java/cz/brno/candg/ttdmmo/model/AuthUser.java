package cz.brno.candg.ttdmmo.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This entity represents user.
 *
 * @author Lastuvka
 */
public class AuthUser {

    private int money;
    private String name;
    private String email;
    private String user_id;
    private String position;
    private String color;
    private Map<String, Boolean> vehicles = new HashMap<String, Boolean>();

    public AuthUser() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Boolean> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Map<String, Boolean> vehicles) {
        this.vehicles = vehicles;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "AuthUser{" + "money=" + money + ", name=" + name + ", email=" + email + ", user_id=" + user_id + ", position=" + position + ", color=" + color + ", buses=" + vehicles + '}';
    }

}
