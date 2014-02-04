package cz.brno.candg.ttdmmo.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This entity represents registered, logged and authenticated user.
 *
 * @author Lastuvka
 */
public class AuthUser {

    int money;
    String name;
    String user_id;
    private Map<String, Boolean> buses = new HashMap<String, Boolean>();

    public AuthUser() {
    }

    public AuthUser(int money, String name, String user_id) {
        this.money = money;
        this.name = name;
        this.user_id = user_id;
    }

    public Map<String, Boolean> getBuses() {
        return buses;
    }

    public void setBuses(Map<String, Boolean> buses) {
        this.buses = buses;
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
        return "AuthUser{" + "money=" + money + ", name=" + name + ", user_id=" + user_id + '}';
    }

}
