/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.serviceapi;

import cz.brno.candg.ttdmmo.firebase.FbSellVehicleReq;
import cz.brno.candg.ttdmmo.firebase.NewVehicleReq;
import cz.brno.candg.ttdmmo.firebase.PathReq;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.model.MapField;
import java.util.List;

/**
 *
 * @author lastuvka
 */
public interface VehicleService {

    public void move(MapField mapField, String nextMapField_id, Bus bus);

    public void create(MapField mapField, AuthUser authUser, NewVehicleReq req);

    public void sell(AuthUser authUser, Bus bus, FbSellVehicleReq req);

    public void setPath(Bus bus, List<MapField> path, PathReq req);
}
