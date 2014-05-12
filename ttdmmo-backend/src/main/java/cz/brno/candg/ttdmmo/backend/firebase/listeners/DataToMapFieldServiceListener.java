package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.dto.MapFieldDTO;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data to map service for method build
 *
 * @author lastuvka
 */
public class DataToMapFieldServiceListener implements ValueEventListener {

    final static Logger log = LoggerFactory.getLogger(DataToMapFieldServiceListener.class);
    private int money = -1;
    private MapField mapField = null;
    private MapFieldDTO fbReq;
    private MapFieldService mapFieldService;

    public MapFieldService getMapFieldService() {
        return mapFieldService;
    }

    public void setMapFieldService(MapFieldService mapFieldService) {
        this.mapFieldService = mapFieldService;
    }

    public MapFieldDTO getFbReq() {
        return fbReq;
    }

    public void setFbReq(MapFieldDTO fbReq) {
        this.fbReq = fbReq;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public MapField getMapField() {
        return mapField;
    }

    public void setMapField(MapField mapField) {
        this.mapField = mapField;
    }

    @Override
    public void onDataChange(DataSnapshot ds) {
        if (ds.getName().equals("money")) {
            log.info("Additional data recived: user money - " + ds.getValue());
            setMoney(ds.getValue(Integer.class));
        } else {
            log.info("Additional data recived: map field - " + ds.getValue());
            setMapField(ds.getValue(MapField.class));
        }

        if (getMoney() != -1 && getMapField() != null) {
            mapFieldService.build(getMoney(), getMapField(), getFbReq());
        }
    }

    @Override
    public void onCancelled(FirebaseError fe) {
    }
}
