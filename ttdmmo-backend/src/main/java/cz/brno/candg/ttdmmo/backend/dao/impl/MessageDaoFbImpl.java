/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import cz.brno.candg.ttdmmo.backend.dao.MessageDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class MessageDaoFbImpl implements MessageDao {

    final static Logger log = LoggerFactory.getLogger(MessageDaoFbImpl.class);

    private final Firebase ref = new Firebase(FbRef.ref + "messages");

    private static int cislo = 0;

    // injected from Spring
    public MessageDaoFbImpl() {
        log.info("Inicializace MessageDaoFbImpl" + cislo);
        cislo++;
    }

    @Override
    public String create(Message entity) {
        Firebase newPushRef = ref.push();
        String id = newPushRef.getName();
        Firebase childRef = ref.child(entity.getUser_id()).child(id);
        childRef.setValue(entity);
        return id;
    }

    @Override
    public void get(String id, ValueEventListenerWithType valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(Message entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String id) {
        Firebase childRef = ref.child(id);
        childRef.removeValue();
    }

}
