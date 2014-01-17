/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;
import cz.brno.candg.ttdmmo.model.MapField;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class MapFieldDaoFirebaseImpl implements MapFieldDao {

    final static Logger log = LoggerFactory.getLogger(AuthUserDaoFirebaseImpl.class);

    private final Firebase refMap = new Firebase("https://ttdmmo1.firebaseio-demo.com/map");

    private final Firebase refPath = new Firebase("https://ttdmmo1.firebaseio-demo.com/paths");

    private static int cislo = 0;

    // injected from Spring
    public MapFieldDaoFirebaseImpl() {
        System.out.println("Inicializace MapFieldDaoFirebaseImpl" + cislo);
        cislo++;
    }

    @Override
    public Long create(MapField entity) {
        refMap.child(MapField.indexFromXY(entity.getX(), entity.getY())).setValue(entity);
        return 0L;
    }

    @Override
    public void getXY(int x, int y, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = refMap.child(MapField.indexFromXY(x, y));
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(MapField entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void get(int id, ValueEventListenerWithType valueEventListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeBus(int x, int y, String bus_id) {
        Firebase childRef = refMap.child(MapField.indexFromXY(x, y)).child("obj").child("buses").child(bus_id);
        childRef.removeValue();
    }

    @Override
    public void addBus(int x, int y, String bus_id) {
        Firebase childRef = refMap.child(MapField.indexFromXY(x, y)).child("obj").child("buses").child(bus_id);
        childRef.setValue(1);
    }

    @Override
    public void getFromPath(String path_id, String path_position, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = refPath.child(path_id).child(path_position);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getByID(String id, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = refMap.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

}
