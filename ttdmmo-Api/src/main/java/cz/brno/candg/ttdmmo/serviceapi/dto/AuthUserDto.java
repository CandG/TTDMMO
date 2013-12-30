package cz.brno.candg.ttdmmo.serviceapi.dto;

import java.util.Objects;

/**
 * DTO for AuthUser entity.
 *
 * @author Lastuvka
 */
public class AuthUserDto {

    int money;
    String name;
    int user_id;

    public AuthUserDto() {
    }

    public AuthUserDto(int money, String name, int user_id) {
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
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.user_id;
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
        final AuthUserDto other = (AuthUserDto) obj;
        if (this.user_id != other.user_id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AuthUserDto{" + "money=" + money + ", name=" + name + ", user_id=" + user_id + '}';
    }

}
