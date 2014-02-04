/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.serviceapi;

import cz.brno.candg.ttdmmo.firebase.NewVehicleReq;
import cz.brno.candg.ttdmmo.firebase.PathReq;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.model.MapField;

/**
 *
 * @author lastuvka
 */
public interface VehicleService {

    public void move(double serverOffset, MapField mapField, String nextMapField_id, Bus bus);

    public void create(double serverOffset, MapField mapField, AuthUser authUser, NewVehicleReq req);

    public void setPath(double serverOffset, Bus bus, String path, PathReq req);
}
