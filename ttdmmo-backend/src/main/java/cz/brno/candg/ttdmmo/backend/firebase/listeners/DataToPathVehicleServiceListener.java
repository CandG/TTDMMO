/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.firebase.PathReq;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lastuvka
 */
public class DataToPathVehicleServiceListener extends ValueEventListenerWithType {

    private PathReq fbReq;
    private VehicleService vehicleService;
    private List<MapField> stops = new ArrayList<MapField>();
    private Bus bus;

    public PathReq getFbReq() {
        return fbReq;
    }

    public void setFbReq(PathReq fbReq) {
        this.fbReq = fbReq;
    }

    public VehicleService getBusService() {
        return vehicleService;
    }

    public void setBusService(VehicleService busService) {
        this.vehicleService = busService;
    }

    public List<MapField> getStops() {
        return stops;
    }

    public void setStops(List<MapField> stops) {
        this.stops = stops;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    @Override
    public void onDataChange(DataSnapshot ds) {
        System.out.println("Ds name: " + ds.getName() + " / ds value: " + ds.getValue() + " ;QQ");
        if (ds.getName().equals(fbReq.getBus_id())) {
            setBus(ds.getValue(Bus.class));
        } else {
            // Path.class
            stops.add(ds.getValue(MapField.class));
        }

        if (getBus() != null && stops != null && stops.size() == fbReq.getStops().size()) {
            vehicleService.setPath(bus, stops, fbReq);
        }
    }

    @Override
    public void onCancelled(FirebaseError fe) {
    }

}
