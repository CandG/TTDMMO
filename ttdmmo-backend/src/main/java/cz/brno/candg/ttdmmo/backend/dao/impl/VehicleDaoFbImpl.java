package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;
import cz.brno.candg.ttdmmo.backend.dao.VehicleDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.model.MapField;

public class VehicleDaoFbImpl implements VehicleDao {

    private final Firebase ref = new Firebase(FbRef.ref + "buses");

    @Override
    public String create(Bus entity) {
        double priority = entity.getStartTime();
        Firebase newPushRef = ref.push();
        entity.setBus_id(newPushRef.getName());
        Firebase childRef = ref.child(entity.getBus_id());
        childRef.setValue(entity, priority);
        return newPushRef.getName();
    }

    @Override
    public void get(String id, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(Bus entity) {
        double priority = entity.getStartTime();
        Firebase childRef = ref.child(entity.getBus_id());
        childRef.setValue(entity, priority);
    }

    @Override
    public void remove(String id) {
        Firebase childRef = ref.child(id);
        childRef.removeValue();
    }

    @Override
    public void move(int x, int y, String bus_id) {
        Firebase childRef = ref.child(bus_id);
        childRef.updateChildren(MapField.MapFromXY(x, y));
    }

    @Override
    public void setArriveTime(double estimatedArriveTimeMs, String bus_id) {
        Firebase childRef = ref.child(bus_id);
        childRef.setPriority(estimatedArriveTimeMs);
        childRef.child("startTime").setValue(ServerValue.TIMESTAMP);
    }

    @Override
    public void changeColor(String id, String color) {
        Firebase childRef = ref.child(id).child("color");
        childRef.setValue(color);
    }

}
