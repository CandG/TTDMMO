/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.firebase.PathReq;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;

/**
 *
 * @author lastuvka
 */
public class DataToPathVehicleServiceListener extends ValueEventListenerWithType {

    private double serverOffset;
    private PathReq fbReq;
    private VehicleService busService;
    private String path = "x";
    private Bus bus;

    public double getServerOffset() {
        return serverOffset;
    }

    public void setServerOffset(double serverOffset) {
        this.serverOffset = serverOffset;
    }

    public PathReq getFbReq() {
        return fbReq;
    }

    public void setFbReq(PathReq fbReq) {
        this.fbReq = fbReq;
    }

    public VehicleService getBusService() {
        return busService;
    }

    public void setBusService(VehicleService busService) {
        this.busService = busService;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    @Override
    public void onDataChange(DataSnapshot ds) {
        System.out.println(ds.getName() + " / " + ds.getValue() + "QQ:");
        if (ds.getName().equals(fbReq.getBus_id())) {
            setBus(ds.getValue(Bus.class));
        } else {
            setPath(ds.getName());
        }

        if (getBus() != null && getPath() != "x") {
            busService.setPath(serverOffset, bus, path, fbReq);
        }
    }

    @Override
    public void onCancelled(FirebaseError fe) {
    }

}
