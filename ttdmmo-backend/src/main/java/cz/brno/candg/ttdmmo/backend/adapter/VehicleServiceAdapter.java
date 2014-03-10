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
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToSellVehicleServiceListener;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToVehicleServiceListener;
import cz.brno.candg.ttdmmo.firebase.FbSellVehicleReq;
import cz.brno.candg.ttdmmo.firebase.NewVehicleReq;
import cz.brno.candg.ttdmmo.firebase.PathReq;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class VehicleServiceAdapter {

    final static Logger log = LoggerFactory.getLogger(VehicleServiceAdapter.class);

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
        userDao.get(fbReq.getUser_id(), dataToNewVehicleServiceListener);
        mapFieldDao.getByID(fbReq.getXy(), dataToNewVehicleServiceListener);
        log.info("req" + fbReq);
    }

    public void setPath(PathReq fbReq) {
        /*
         old type:
         req - bus, Map of stops (x,y)...sklad + farma
         adapter - zjistit zda mezi stops are path
         */
        if (fbReq.getStops() != null && fbReq.getStops().size() > 1) {
            DataToPathVehicleServiceListener dataToPathVehicleServiceListener = new DataToPathVehicleServiceListener();
            dataToPathVehicleServiceListener.setBusService(vehicleService);
            dataToPathVehicleServiceListener.setFbReq(fbReq);
            vehicleDao.get(fbReq.getBus_id(), dataToPathVehicleServiceListener);
            for (String field : fbReq.getStops()) {
                mapFieldDao.getByID(field, dataToPathVehicleServiceListener);
            }
            //pathDao.get(fbReq.getStops(), dataToPathVehicleServiceListener);
            log.info("setPath: " + fbReq.toString());
        }
    }

    public void moveVehicle(Bus bus) {
        DataToVehicleServiceListener dataToBusServiceListener = new DataToVehicleServiceListener();
        dataToBusServiceListener.setBusService(vehicleService);
        dataToBusServiceListener.setBus(bus);
        String pos;
        pos = Integer.toString(bus.getPath_position() + bus.getDirection());
        pathDao.getFromPath(bus.getPath_id(), pos, dataToBusServiceListener);
        mapFieldDao.getByID(bus.getNextField(), dataToBusServiceListener);
    }

    public void sellVehicle(FbSellVehicleReq fbReq) {
        DataToSellVehicleServiceListener dataToSellVehicleServiceListener = new DataToSellVehicleServiceListener();
        dataToSellVehicleServiceListener.setVehicleService(vehicleService);
        dataToSellVehicleServiceListener.setFbReq(fbReq);
        userDao.get(fbReq.getUser_id(), dataToSellVehicleServiceListener);
        vehicleDao.get(fbReq.getBus_id(), dataToSellVehicleServiceListener);
        log.info("req" + fbReq.toString());
    }
}
