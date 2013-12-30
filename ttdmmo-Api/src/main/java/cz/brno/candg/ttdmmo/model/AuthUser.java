package cz.brno.candg.ttdmmo.model;

/**
 * This entity represents registered, logged and authenticated user.
 *
 * @author Lastuvka
 */
public class AuthUser {

    int money;
    String name;
    int user_id;

    public AuthUser() {
    }

    public AuthUser(int money, String name, int user_id) {
        this.money = money;
        this.name = name;
        this.user_id = user_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "AuthUser{" + "money=" + money + ", name=" + name + ", user_id=" + user_id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.user_id;
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
        final AuthUser other = (AuthUser) obj;
        if (this.user_id != other.user_id) {
            return false;
        }
        return true;
    }

}
