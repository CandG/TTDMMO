package cz.brno.candg.ttdmmo.backend.dao.impl;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.backend.adapter.MapServiceAdapterImpl;
import cz.brno.candg.ttdmmo.backend.dao.CityDao;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAO impl. for cities
 *
 * @author lastuvka
 */
public class CityDaoFbImpl implements CityDao {

    final static Logger log = LoggerFactory.getLogger(CityDaoFbImpl.class);

    private final Firebase ref = new Firebase(FbRef.refD + "cities");

    private final Firebase refRanking = new Firebase(FbRef.refD + "ranking");

    @Override
    public String create(City entity) {
        String id = entity.getXy();
        ref.child(id).setValue(entity);
        return id;
    }

    @Override
    public void get(String id, ValueEventListener valueEventListener) {
        Firebase childRef = ref.child(id);
        childRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void update(City entity) {
        Firebase childRef = ref.child(entity.getXy());
        childRef.setValue(entity);
    }

    @Override
    public void remove(String id) {
        Firebase childRef = ref.child(id);
        childRef.removeValue();
    }

    @Override
    public void changeFood(final String id, final int food, final MapServiceAdapterImpl mapServiceAdapter) {
        Firebase childRef = ref.child(id);
        childRef.runTransaction(new Transaction.Handler() {
            private int oldPeople;

            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if (currentData.getValue() != null) {
                    City city = currentData.getValue(City.class);
                    oldPeople = city.getPeople();
                    city.setFood(city.getFood() + food);
                    if (city.getPeople() * 2 <= city.getFood()) {
                        city.setFood(city.getFood() - city.getPeople() * 2);
                        city.setPeople(city.getPeople() + 1);
                        refRanking.child(id).child("people").setValue(city.getPeople());
                        refRanking.child(id).setPriority(city.getPeople());
                    }
                    currentData.setValue(city);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(FirebaseError error, boolean committed, DataSnapshot currentData) {
                if (error != null) {
                    System.err.println("There was there an error for change food : " + error.getMessage());
                } else {
                    if (!committed) {
                        System.err.println("Transaction for change food did not commit!");
                    } else {
                        System.out.println("Transaction for change food succeeded!");
                    }
                    City city = currentData.getValue(City.class);
                    if (city.getPeople() != oldPeople && city.getPeople() % 10 == 0) {
                        mapServiceAdapter.getHousePlaces(id);
                    }
                }
            }
        });
    }

    @Override
    public void changeWood(final String id, final int wood, final MapServiceAdapterImpl mapServiceAdapter) {
        Firebase childRef = ref.child(id);
        childRef.runTransaction(new Transaction.Handler() {
            private int oldPeople;

            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if (currentData.getValue() != null) {
                    City city = currentData.getValue(City.class);
                    oldPeople = city.getPeople();
                    city.setWood(city.getWood() + wood);
                    if (city.getPeople() * 2 <= city.getWood()) {
                        city.setWood(city.getWood() - city.getPeople() * 2);
                        city.setPeople(city.getPeople() + 1);
                        refRanking.child(id).child("people").setValue(city.getPeople());
                        refRanking.child(id).setPriority(city.getPeople());
                    }
                    currentData.setValue(city);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(FirebaseError error, boolean committed, DataSnapshot currentData) {
                if (error != null) {
                    System.err.println("There was there an error for change wood : " + error.getMessage());
                } else {
                    if (!committed) {
                        System.err.println("Transaction for change wood did not commit!");
                    } else {
                        System.out.println("Transaction for change wood succeeded!");
                    }
                    City city = currentData.getValue(City.class);
                    if (city.getPeople() != oldPeople && city.getPeople() % 10 == 0) {
                        mapServiceAdapter.getHousePlaces(id);
                    }
                }
            }
        });
    }
}
