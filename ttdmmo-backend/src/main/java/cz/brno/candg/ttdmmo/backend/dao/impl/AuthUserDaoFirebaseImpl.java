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
    public void get(String id, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = refUsers.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getMoney(String id, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = refUsers.child(id).child("money");
        valueEventListener.setType("money");
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(AuthUser entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMoney(String id, int money) {
        refUsers.child(id).child("money").setValue(Long.toString(money));
    }

    @Override
    public void addBus(String id, String bus_id) {
        Firebase childRef = refUsers.child(id).child("buses").child(bus_id);
        childRef.setValue(true);
    }

}
