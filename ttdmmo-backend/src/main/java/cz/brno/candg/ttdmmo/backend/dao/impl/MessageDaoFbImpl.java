package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.backend.dao.MessageDao;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Firebase DAO implementation User entities.
 *
 * @author lastuvka
 */
public class MessageDaoFbImpl implements MessageDao {

    final static Logger log = LoggerFactory.getLogger(MessageDaoFbImpl.class);

    private final Firebase ref = new Firebase(FbRef.refD + "messages");

    @Override
    public String create(Message entity) {
        Firebase newPushRef = ref.push();
        String id = newPushRef.getName();
        Firebase childRef = ref.child(entity.getUser_id()).child(id);
        childRef.setValue(entity);
        return id;
    }

    @Override
    public void get(String id, ValueEventListener valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(Message entity) {
        Firebase childRef = ref.child(entity.getUser_id());
        childRef.setValue(entity);
    }

    @Override
    public void remove(String id) {
        Firebase childRef = ref.child(id);
        childRef.removeValue();
    }

}
