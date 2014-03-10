package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.ServerValue;
import com.firebase.client.Transaction;
import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Firebase DAO implementation User entities.
 *
 * @author Lastuvka
 */
public class AuthUserDaoFbImpl implements AuthUserDao {

    final static Logger log = LoggerFactory.getLogger(AuthUserDaoFbImpl.class);

    private final Firebase ref = new Firebase(FbRef.ref + "users");

    private static int cislo = 0;

    // injected from Spring
    public AuthUserDaoFbImpl() {
        log.info("Inicializace AuthUserDaoFirebaseImpl" + cislo);
        cislo++;
    }

    @Override
    public String create(AuthUser entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void get(String id, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getMoney(String id, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = ref.child(id).child("money");
        valueEventListener.setType("money");
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(AuthUser entity) {
        Firebase childRef = ref.child(entity.getUser_id());
        childRef.setValue(entity, ServerValue.TIMESTAMP);
    }

    @Override
    public void remove(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMoney(String id, final int money) {
        //ref.child(id).child("money").setValue(Long.toString(money));
        Firebase childRef = ref.child(id).child("money");
        childRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                int count = currentData.getValue(Integer.class);
                currentData.setValue(count + money);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(FirebaseError error, boolean committed, DataSnapshot currentData) {

            }
        });
    }

    @Override
    public void addBus(String id, String bus_id) {
        Firebase childRef = ref.child(id).child("buses").child(bus_id);
        childRef.setValue(true);
    }

    @Override
    public void removeBus(String id, String bus_id) {
        Firebase childRef = ref.child(id).child("buses").child(bus_id);
        childRef.removeValue();
    }

    @Override
    public void changeColor(String id, String color) {
        Firebase childRef = ref.child(id).child("color");
        childRef.setValue(color);
    }
}
