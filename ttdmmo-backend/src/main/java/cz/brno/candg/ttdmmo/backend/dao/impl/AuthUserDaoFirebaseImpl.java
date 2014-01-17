package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;
import cz.brno.candg.ttdmmo.model.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Firebase DAO implementation User entities.
 *
 * @author Lastuvka
 */
public class AuthUserDaoFirebaseImpl implements AuthUserDao {

    final static Logger log = LoggerFactory.getLogger(AuthUserDaoFirebaseImpl.class);

    private final Firebase refUsers = new Firebase("https://ttdmmo1.firebaseio-demo.com/users");

    private static int cislo = 0;

    // injected from Spring
    public AuthUserDaoFirebaseImpl() {
        System.out.println("Inicializace AuthUserDaoFirebaseImpl" + cislo);
        cislo++;
    }

    @Override
    public Long create(AuthUser entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void get(int id, ValueEventListenerWithType valueEventListener) {
        String sid = Long.toString(id);
        Firebase childRef = refUsers.child(sid);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getMoney(int id, ValueEventListenerWithType valueEventListener) {
        String sid = Long.toString(id);
        Firebase childRef = refUsers.child(sid).child("money");
        valueEventListener.setType("money");
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(AuthUser entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMoney(int id, int money) {
        String sid = Long.toString(id);
        refUsers.child(sid).child("money").setValue(Long.toString(money));
    }

}
