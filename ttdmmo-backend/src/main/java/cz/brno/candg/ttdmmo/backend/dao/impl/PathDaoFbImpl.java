/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import cz.brno.candg.ttdmmo.backend.dao.PathDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;

/**
 *
 * @author lastuvka
 */
public class PathDaoFbImpl implements PathDao {

    private final Firebase ref = new Firebase("https://ttdmmo1.firebaseio-demo.com/paths");

    @Override
    public Long create(PathDao entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void get(String id, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(PathDao entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
