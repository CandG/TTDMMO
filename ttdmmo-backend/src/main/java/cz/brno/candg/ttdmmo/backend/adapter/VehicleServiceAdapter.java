/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.adapter;

import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.dao.PathDao;
import cz.brno.candg.ttdmmo.backend.dao.VehicleDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToNewVehicleServiceListener;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToPathVehicleServiceListener;
import cz.brno.candg.ttdmmo.firebase.NewVehicleReq;
import cz.brno.candg.ttdmmo.firebase.PathReq;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;
import javax.inject.Inject;

/**
 *
 * @author lastuvka
 */
public class VehicleServiceAdapter {

    @Inject
    private PathDao pathDao;

    @Inject
    private VehicleDao vehicleDao;

    @Inject
    private AuthUserDao userDao;

    @Inject
    private MapFieldDao mapFieldDao;

    @Inject
    VehicleService vehicleService;

    public void newVehicle(NewVehicleReq fbReq) {
        DataToNewVehicleServiceListener dataToNewVehicleServiceListener = new DataToNewVehicleServiceListener();
        dataToNewVehicleServiceListener.setBusService(vehicleService);
        dataToNewVehicleServiceListener.setFbReq(fbReq);
        dataToNewVehicleServiceListener.setServerOffset(1390609283767L);
        userDao.get(fbReq.getUser_id(), dataToNewVehicleServiceListener);
        mapFieldDao.getXY(fbReq.getX(), fbReq.getY(), dataToNewVehicleServiceListener);
        System.out.println("req" + fbReq.toString());
    }

    public void setPath(PathReq fbReq) {
        /*
         req - bus, Map of stops (x,y)...sklad + farma
         adapter - zjistit zda mezi stops are path
        
        
         */
        DataToPathVehicleServiceListener dataToPathVehicleServiceListener = new DataToPathVehicleServiceListener();
        dataToPathVehicleServiceListener.setBusService(vehicleService);
        dataToPathVehicleServiceListener.setFbReq(fbReq);
        dataToPathVehicleServiceListener.setServerOffset(0);
        vehicleDao.get(fbReq.getBus_id(), dataToPathVehicleServiceListener);
        pathDao.get(fbReq.getStops(), dataToPathVehicleServiceListener);
        System.out.println("req" + fbReq.toString());
    }
}
