package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.backend.dao.VehicleDao;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.Vehicle;

/**
 * Firebase DAO implementation Vehicle entities.
 *
 * @author Lastuvka
 */
public class VehicleDaoFbImpl implements VehicleDao {

    private final Firebase ref = new Firebase(FbRef.refD + "vehicles");

    @Override
    public String create(Vehicle entity) {
        double priority = entity.getStartTime();
        Firebase newPushRef = ref.push();
        entity.setVehicle_id(newPushRef.getName());
        Firebase childRef = ref.child(entity.getVehicle_id());
        childRef.setValue(entity, priority);
        return newPushRef.getName();
    }

    @Override
    public void get(String id, ValueEventListener valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(Vehicle entity) {
        double priority = entity.getStartTime() + entity.getP_size() * entity.getSpeed() - FbRef.PROCCESING_DELAY;
        Firebase childRef = ref.child(entity.getVehicle_id());
        childRef.setValue(entity, priority);
    }

    @Override
    public void remove(String id) {
        Firebase childRef = ref.child(id);
        childRef.removeValue();
    }

    @Override
    public void changeColor(String id, String color) {
        Firebase childRef = ref.child(id).child("color");
        childRef.setValue(color);
    }

}
