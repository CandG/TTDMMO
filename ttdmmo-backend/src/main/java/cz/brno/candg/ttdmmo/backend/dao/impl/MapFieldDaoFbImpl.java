/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.MapField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class MapFieldDaoFbImpl implements MapFieldDao {

    final static Logger log = LoggerFactory.getLogger(MapFieldDaoFbImpl.class);

    private final Firebase ref = new Firebase(FbRef.ref + "map");

    private static int cislo = 0;

    // injected from Spring
    public MapFieldDaoFbImpl() {
        log.info("Inicializace MapFieldDaoFirebaseImpl" + cislo);
        cislo++;
    }

    @Override
    public String create(MapField entity) {
        String id = MapField.indexFromXY(entity.getX(), entity.getY());
        ref.child(id).setValue(entity);
        return id;
    }

    @Override
    public void get(String id, ValueEventListenerWithType valueEventListener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getByXY(int x, int y, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = ref.child(MapField.indexFromXY(x, y));
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getByID(String id, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(MapField entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeBus(int x, int y, String bus_id) {
        Firebase childRef = ref.child(MapField.indexFromXY(x, y)).child("obj").child("buses").child(bus_id);
        childRef.removeValue();
    }

    @Override
    public void addBus(int x, int y, String bus_id) {
        Firebase childRef = ref.child(MapField.indexFromXY(x, y)).child("obj").child("buses").child(bus_id);
        childRef.setValue(1);
    }

}
