/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;

/**
 *
 * @author lastuvka
 */
public class DataToVehicleServiceListener extends ValueEventListenerWithType {

    private MapField mapField = null;
    private String nextMapField_id = "x";
    private Bus bus;
    private VehicleService busService;

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public VehicleService getBusService() {
        return busService;
    }

    public void setBusService(VehicleService busService) {
        this.busService = busService;
    }

    public MapField getMapField() {
        return mapField;
    }

    public void setMapField(MapField mapField) {
        this.mapField = mapField;
    }

    public String getNextMapField_id() {
        return nextMapField_id;
    }

    public void setNextMapField_id(String nextMapField_id) {
        this.nextMapField_id = nextMapField_id;
    }

    @Override
    public void onDataChange(DataSnapshot ds) {
        System.out.println(ds.getName() + " / " + ds.getValue());
        if (ds.getName().equals(bus.getNextField())) {
            setMapField(ds.getValue(MapField.class));
        } else {
            setNextMapField_id(ds.getValue(String.class));
        }

        if (getMapField() != null && getNextMapField_id() != "x") {
            busService.move(mapField, nextMapField_id, bus);
        }
    }

    @Override
    public void onCancelled(FirebaseError fe) {
    }

}
