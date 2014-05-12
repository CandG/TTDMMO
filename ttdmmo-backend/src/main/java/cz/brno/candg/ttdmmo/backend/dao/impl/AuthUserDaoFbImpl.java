package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.ServerValue;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
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

    private final Firebase ref = new Firebase(FbRef.refD + "users");

    @Override
    public String create(AuthUser entity) {
        String id = entity.getUser_id();
        ref.child(id).setValue(entity);
        return id;
    }

    @Override
    public void get(String id, ValueEventListener valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getMoney(String id, ValueEventListener valueEventListener) {
        Firebase childRef = ref.child(id).child("money");
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(AuthUser entity) {
        Firebase childRef = ref.child(entity.getUser_id());
        childRef.setValue(entity, ServerValue.TIMESTAMP);
    }

    @Override
    public void remove(String id) {
        Firebase childRef = ref.child(id);
        childRef.removeValue();
    }

    @Override
    public void updateMoney(String id, final int money) {
        Firebase childRef = ref.child(id).child("money");
        childRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if (currentData.getValue() != null) {
                    int count = currentData.getValue(Integer.class);
                    currentData.setValue(count + money);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(FirebaseError error, boolean committed, DataSnapshot currentData) {
                if (error != null) {
                    System.err.println("There was there an error for update money: " + error.getMessage());
                } else {
                    if (!committed) {
                        System.err.println("Transaction for update money did not commit!");
                    } else {
                        System.out.println("Transaction for update money succeeded!");
                    }
                }
            }
        });
    }

    @Override
    public void addVehicle(String id, String vehicle_id) {
        Firebase childRef = ref.child(id).child("vehicles").child(vehicle_id);
        childRef.setValue(true);
    }

    @Override
    public void removeVehicle(String id, String vehicle_id) {
        Firebase childRef = ref.child(id).child("vehicles").child(vehicle_id);
        childRef.removeValue();
    }

    @Override
    public void changeColor(String id, String color) {
        Firebase childRef = ref.child(id).child("color");
        childRef.setValue(color);
    }
}
