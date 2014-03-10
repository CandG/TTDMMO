/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.firebase.FbSellVehicleReq;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;

/**
 *
 * @author lastuvka
 */
public class DataToSellVehicleServiceListener extends ValueEventListenerWithType {

    private FbSellVehicleReq fbReq;
    private VehicleService vehicleService;
    private Bus bus = null;
    private AuthUser authUser = null;

    @Override
    public void onDataChange(DataSnapshot ds) {
        if (ds.getName().equals(fbReq.getBus_id())) {
            setBus(ds.getValue(Bus.class));
        } else {
            setAuthUser(ds.getValue(AuthUser.class));
        }

        if (getBus() != null && getAuthUser() != null) {
            vehicleService.sell(authUser, bus, fbReq);
        }
    }

    @Override
    public void onCancelled(FirebaseError fe) {
    }

    public FbSellVehicleReq getFbReq() {
        return fbReq;
    }

    public void setFbReq(FbSellVehicleReq fbReq) {
        this.fbReq = fbReq;
    }

    public VehicleService getVehicleService() {
        return vehicleService;
    }

    public void setVehicleService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }

}
