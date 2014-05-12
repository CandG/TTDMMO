package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.dto.HousePlacesDTO;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data to map service for method spreadCity
 *
 * @author lastuvka
 */
public class DataToSpreadCityServiceListener implements ValueEventListener {

    final static Logger log = LoggerFactory.getLogger(DataToSpreadCityServiceListener.class);
    private HousePlacesDTO fbReq;
    private MapFieldService mapFieldService;
    private List<MapField> fields = new ArrayList<MapField>();

    public HousePlacesDTO getFbReq() {
        return fbReq;
    }

    public void setFbReq(HousePlacesDTO fbReq) {
        this.fbReq = fbReq;
    }

    public MapFieldService getMapFieldService() {
        return mapFieldService;
    }

    public void setMapFieldService(MapFieldService mapFieldService) {
        this.mapFieldService = mapFieldService;
    }

    public List<MapField> getFields() {
        return fields;
    }

    public void setFields(List<MapField> fields) {
        this.fields = fields;
    }

    @Override
    public void onDataChange(DataSnapshot ds) {
        log.info("Additional data recived: map field - " + ds.getValue());
        fields.add(ds.getValue(MapField.class));

        if (fields != null && fields.size() == fbReq.getPlaces().size()) {
            mapFieldService.spreadCity(fields, fbReq);
        }
    }

    @Override
    public void onCancelled(FirebaseError fe) {
    }

}
