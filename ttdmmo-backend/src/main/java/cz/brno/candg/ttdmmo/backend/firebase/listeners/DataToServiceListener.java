/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.firebase.FirebaseReq;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import javax.inject.Inject;

/**
 *
 * @author lastuvka
 */
public class DataToServiceListener extends ValueEventListenerWithType {

    private int money = -1;
    private MapField mapField = null;
    private FirebaseReq fbReq;
    private MapFieldService mapFieldService;

    public MapFieldService getMapFieldService() {
        return mapFieldService;
    }

    public void setMapFieldService(MapFieldService mapFieldService) {
        this.mapFieldService = mapFieldService;
    }

    public FirebaseReq getFbReq() {
        return fbReq;
    }

    public void setFbReq(FirebaseReq fbReq) {
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
        System.out.println(ds.getValue() + " name> " + ds.getName() + " a type/");
        if (ds.getName().equals("money")) {
            setMoney(ds.getValue(Integer.class));
        } else {
            setMapField(ds.getValue(MapField.class));
        }

        if (getMoney() != -1 && getMapField() != null) {
            System.out.println("req to service" + fbReq.toString());
            mapFieldService.build(getMoney(), getMapField(), getFbReq());
        }
    }

    @Override
    public void onCancelled(FirebaseError fe) {
    }
}
