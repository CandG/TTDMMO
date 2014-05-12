package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.backend.dao.PathDao;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.Path;

/**
 * Firebase DAO implementation User entities.
 *
 * @author lastuvka
 */
public class PathDaoFbImpl implements PathDao {

    private final Firebase ref = new Firebase(FbRef.refD + "paths");

    @Override
    public String create(Path entity) {
        Firebase newPushRef = ref.push();
        String id = newPushRef.getName();
        Firebase childRef = ref.child(id);
        childRef.setValue(entity.getPath());
        return id;
    }

    @Override
    public void get(String id, ValueEventListener valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(Path entity) {
        Firebase childRef = ref.child(entity.transientGetId());
        childRef.setValue(entity);
    }

    @Override
    public void remove(String id) {
        Firebase childRef = ref.child(id);
        childRef.removeValue();
    }

}
