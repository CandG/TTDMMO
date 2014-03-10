/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.firebase.NewVehicleReq;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class DataToNewVehicleServiceListener extends ValueEventListenerWithType {

    final static Logger log = LoggerFactory.getLogger(DataToNewVehicleServiceListener.class);

    private NewVehicleReq fbReq;
    private VehicleService busService;
    private MapField mapField = null;
    private AuthUser authUser = null;

    public MapField getMapField() {
        return mapField;
    }

    public void setMapField(MapField mapField) {
        this.mapField = mapField;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }

    public NewVehicleReq getFbReq() {
        return fbReq;
    }

    public void setFbReq(NewVehicleReq fbReq) {
        this.fbReq = fbReq;
    }

    @Override
    public void onDataChange(DataSnapshot ds) {
        if (ds.getName().equals(fbReq.getXy())) {
            setMapField(ds.getValue(MapField.class));
            log.info("mapField" + ds.getValue(MapField.class));
        } else {
            setAuthUser(ds.getValue(AuthUser.class));
            log.info("user" + ds.getValue());
        }
        log.info(ds.getName());
        if (getMapField() != null && getAuthUser() != null) {
            busService.create(mapField, authUser, fbReq);
            log.info("all");
        }
    }

    @Override
    public void onCancelled(FirebaseError fe) {
    }

    public VehicleService getBusService() {
        return busService;
    }

    public void setBusService(VehicleService busService) {
        this.busService = busService;
    }

}
