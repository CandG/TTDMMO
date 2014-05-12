package cz.brno.candg.ttdmmo.backend.adapter;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.adapterapi.MapServiceAdapter;
import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.HousePlacesDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToMapFieldServiceListener;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToSpreadCityServiceListener;
import cz.brno.candg.ttdmmo.dto.HousePlacesDTO;
import cz.brno.candg.ttdmmo.dto.MapFieldDTO;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * impl. Map User service adapter interface for operations on map field.
 *
 * @author lastuvka
 */
public class MapServiceAdapterImpl implements MapServiceAdapter {

    final static Logger log = LoggerFactory.getLogger(MapServiceAdapterImpl.class);

    @Inject
    MapFieldService mapFieldService;

    @Inject
    private AuthUserDao userDao;

    @Inject
    private MapFieldDao mapFieldDao;

    @Inject
    private HousePlacesDao housePlacesDao;

    @Override
    public void buildField(MapFieldDTO fbReq) {
        log.info("Build field: " + fbReq.toString());
        DataToMapFieldServiceListener dataToServiceListener = new DataToMapFieldServiceListener();
        dataToServiceListener.setMapFieldService(mapFieldService);
        dataToServiceListener.setFbReq(fbReq);
        userDao.getMoney(fbReq.getUser_id(), dataToServiceListener);
        mapFieldDao.get(fbReq.getXy(), dataToServiceListener);
    }

    @Override
    public void spreadCity(HousePlacesDTO fbReq) {
        if (fbReq.getPlaces() != null && fbReq.getPlaces().size() > 0) {
            DataToSpreadCityServiceListener dataToSpreadCityServiceListener = new DataToSpreadCityServiceListener();
            dataToSpreadCityServiceListener.setMapFieldService(mapFieldService);
            dataToSpreadCityServiceListener.setFbReq(fbReq);
            for (String field : fbReq.getPlaces().keySet()) {
                mapFieldDao.get(field, dataToSpreadCityServiceListener);
            }
        }
    }

    @Override
    public void getHousePlaces(String city_id) {
        log.info("getHousePlaces service");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                HousePlacesDTO fbPlacesReq = ds.getValue(HousePlacesDTO.class);
                fbPlacesReq.setCity_id(ds.getName());
                spreadCity(fbPlacesReq);
            }

            @Override
            public void onCancelled(FirebaseError fe) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        };

        housePlacesDao.get(city_id, valueEventListener);

    }
}
