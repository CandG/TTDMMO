package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;
import cz.brno.candg.ttdmmo.backend.dao.VehicleDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.model.MapField;

public class VehicleDaoFirebaseImpl implements VehicleDao {

    private final Firebase refBuses = new Firebase("https://ttdmmo1.firebaseio-demo.com/buses");

    @Override
    public Long create(Bus entity) {
        double priority = entity.getStartTime();
        Firebase newPushRef = refBuses.push();
        entity.setBus_id(newPushRef.getName());
        Firebase childRef = refBuses.child(entity.getBus_id());
        childRef.setValue(entity, priority);
        return 0L;
    }

    @Override
    public void get(String id, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = refBuses.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(Bus entity) {
        double priority = entity.getStartTime();
        Firebase childRef = refBuses.child(entity.getBus_id());
        childRef.setValue(entity, priority);
    }

    @Override
    public void remove(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void move(int x, int y, String bus_id) {
        Firebase childRef = refBuses.child(bus_id);
        childRef.updateChildren(MapField.MapFromXY(x, y));
    }

    @Override
    public void setArriveTime(double estimatedArriveTimeMs, String bus_id) {
        Firebase childRef = refBuses.child(bus_id);
        childRef.setPriority(estimatedArriveTimeMs);
        childRef.child("startTime").setValue(ServerValue.TIMESTAMP);
    }

}
