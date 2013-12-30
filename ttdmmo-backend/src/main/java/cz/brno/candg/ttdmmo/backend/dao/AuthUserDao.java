package cz.brno.candg.ttdmmo.backend.dao;

import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;
import cz.brno.candg.ttdmmo.model.AuthUser;

/**
 *
 * @author Zdenek Lastuvka
 */
public interface AuthUserDao extends Dao<AuthUser> {

    void getMoney(int id, ValueEventListenerWithType valueEventListener);
}
