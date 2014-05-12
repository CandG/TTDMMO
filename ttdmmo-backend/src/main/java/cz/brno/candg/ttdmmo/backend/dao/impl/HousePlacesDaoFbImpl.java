package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.backend.dao.HousePlacesDao;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.dto.HousePlacesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAO implementation of house places
 *
 * @author lastuvka
 */
public class HousePlacesDaoFbImpl implements HousePlacesDao {

    final static Logger log = LoggerFactory.getLogger(HousePlacesDaoFbImpl.class);

    private final Firebase ref = new Firebase(FbRef.refD + "housePlaces");

    @Override
    public String create(HousePlacesDTO entity) {
        String id = entity.getCity_id();
        ref.child(id).setValue(entity);
        return id;
    }

    @Override
    public void get(String id, ValueEventListener valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(HousePlacesDTO entity) {
        String id = entity.getCity_id();
        ref.child(id).setValue(entity);
    }

    @Override
    public void remove(String id) {
        Firebase childRef = ref.child(id);
        childRef.removeValue();
    }

    @Override
    public void addPlace(String city_id, String place_id) {
        Firebase childRef = ref.child(city_id).child("places").child(place_id);
        childRef.setValue(true);
    }

    @Override
    public void removePlace(String city_id, String place_id) {
        Firebase childRef = ref.child(city_id).child("places").child(place_id);
        childRef.removeValue();
    }
}
