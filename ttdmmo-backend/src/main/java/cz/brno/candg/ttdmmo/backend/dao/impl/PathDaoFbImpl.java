/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import cz.brno.candg.ttdmmo.backend.dao.PathDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.Path;

/**
 *
 * @author lastuvka
 */
public class PathDaoFbImpl implements PathDao {

    private final Firebase ref = new Firebase(FbRef.ref + "paths");

    @Override
    public String create(Path entity) {
        Firebase newPushRef = ref.push();
        String id = newPushRef.getName();
        Firebase childRef = ref.child(id);
        childRef.setValue(entity.getPath());
        return id;
    }

    @Override
    public void get(String id, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getFromPath(String path_id, String path_position, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = ref.child(path_id).child(path_position);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(Path entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String id) {
        Firebase childRef = ref.child(id);
        childRef.removeValue();
    }

}
