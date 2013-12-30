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

    private final Firebase refMap = new Firebase("https://ttdmmo.firebaseio-demo.com/map");

    private static int cislo = 0;

    // injected from Spring
    public MapFieldDaoFirebaseImpl() {
        System.out.println("Inicializace MapFieldDaoFirebaseImpl" + cislo);
        cislo++;
    }

    @Override
    public Long create(MapField entity) {
        Map<String, Object> toSet = new HashMap<String, Object>();
        Map<String, Object> toSetObj = new HashMap<String, Object>();
        toSetObj.put("bus", "1");
        toSetObj.put("bus_stop", "1");
        toSet.put("x", "0");
        toSet.put("y", "1");
        toSet.put("type", "r3");
        toSet.put("objects", toSetObj);
        refMap.child(Integer.toString(entity.getX())).child(Integer.toString(entity.getY())).setValue(toSet);
        return 0L;
    }

    @Override
    public void getXY(int x, int y, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = refMap.child(Long.toString(x)).child(Long.toString(y));
        valueEventListener.setType("mapField");
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

}
