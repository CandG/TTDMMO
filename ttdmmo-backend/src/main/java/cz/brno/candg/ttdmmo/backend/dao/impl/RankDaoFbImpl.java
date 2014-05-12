package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.backend.dao.RankDao;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.Rank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Firebase DAO implementation User entities.
 *
 * @author lastuvka
 */
public class RankDaoFbImpl implements RankDao {

    final static Logger log = LoggerFactory.getLogger(RankDaoFbImpl.class);

    private final Firebase ref = new Firebase(FbRef.refD + "ranking");

    @Override
    public String create(Rank entity) {
        String id = entity.getXy();
        ref.child(id).setValue(entity, entity.getPeople());
        return id;
    }

    @Override
    public void get(String id, ValueEventListener valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(Rank entity) {
        Firebase childRef = ref.child(entity.getXy());
        childRef.setValue(entity);
    }

    @Override
    public void remove(String id) {
        Firebase childRef = ref.child(id);
        childRef.removeValue();
    }

}
