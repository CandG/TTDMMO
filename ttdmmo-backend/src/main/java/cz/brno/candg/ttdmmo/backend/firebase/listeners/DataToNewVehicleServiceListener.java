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

/**
 *
 * @author lastuvka
 */
public class DataToNewVehicleServiceListener extends ValueEventListenerWithType {

    private double serverOffset;
    private NewVehicleReq fbReq;
    private VehicleService busService;
    private MapField mapField = null;
    private AuthUser authUser = null;

    public double getServerOffset() {
        return serverOffset;
    }

    public void setServerOffset(double serverOffset) {
        this.serverOffset = serverOffset;
    }

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
        System.out.println(ds.getName() + " / " + ds.getValue() + "QQ:" + MapField.indexFromXY(fbReq.getX(), fbReq.getY()));
        if (ds.getName().equals(MapField.indexFromXY(fbReq.getX(), fbReq.getY()))) {
            setMapField(ds.getValue(MapField.class));
        } else {
            setAuthUser(ds.getValue(AuthUser.class));
        }

        if (getMapField() != null && getAuthUser() != null) {
            busService.create(serverOffset, mapField, authUser, fbReq);
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
