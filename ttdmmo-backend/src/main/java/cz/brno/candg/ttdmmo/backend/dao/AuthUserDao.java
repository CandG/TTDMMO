package cz.brno.candg.ttdmmo.backend.dao;

import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;
import cz.brno.candg.ttdmmo.model.AuthUser;

/**
 *
 * @author Zdenek Lastuvka
 */
public interface AuthUserDao extends Dao<AuthUser> {

    void getMoney(String id, ValueEventListenerWithType valueEventListener);

    void setMoney(String id, int money);

    void addBus(String id, String bus_id);

    void removeBus(String id, String bus_id);

    void changeColor(String id, String color);
}
