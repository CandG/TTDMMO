package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.MapField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Map DAO impl for Firebase
 *
 * @author lastuvka
 */
public class MapFieldDaoFbImpl implements MapFieldDao {

    final static Logger log = LoggerFactory.getLogger(MapFieldDaoFbImpl.class);

    private final Firebase ref = new Firebase(FbRef.refD + "map");

    @Override
    public String create(MapField entity) {
        String id = MapField.indexFromXY(entity.getX(), entity.getY());
        ref.child(id).setValue(entity);
        return id;
    }

    @Override
    public void get(String id, ValueEventListener valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void getByXY(int x, int y, ValueEventListener valueEventListener) {
        Firebase childRef = ref.child(MapField.indexFromXY(x, y));
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(MapField entity) {
        String id = MapField.indexFromXY(entity.getX(), entity.getY());
        ref.child(id).setValue(entity);
    }

    @Override
    public void remove(String id) {
        Firebase childRef = ref.child(id);
        childRef.removeValue();
    }

    @Override
    public void removePath(String id, String path_id) {
        Firebase childRef = ref.child(id).child("paths").child(path_id);
        childRef.removeValue();
    }

    @Override
    public void addPath(int x, int y, String path_id) {
        Firebase childRef = ref.child(MapField.indexFromXY(x, y)).child("paths").child(path_id);
        childRef.setValue(true);
    }
}
