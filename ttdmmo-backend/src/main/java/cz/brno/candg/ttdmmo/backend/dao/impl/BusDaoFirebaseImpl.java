package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;
import cz.brno.candg.ttdmmo.backend.dao.BusDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.model.MapField;

public class BusDaoFirebaseImpl implements BusDao {

    private final Firebase refBuses = new Firebase("https://ttdmmo1.firebaseio-demo.com/buses");

    @Override
    public Long create(Bus entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void get(int id, ValueEventListenerWithType valueEventListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Bus entity) {
        Firebase childRef = refBuses.child(entity.getBus_id());
        childRef.setValue(entity);
    }

    @Override
    public void remove(int id) {
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
