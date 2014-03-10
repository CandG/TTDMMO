/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.service.impl;

import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.VehicleDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.dao.PathDao;
import cz.brno.candg.ttdmmo.backend.firebase.FbServerTime;
import cz.brno.candg.ttdmmo.firebase.FbSellVehicleReq;
import cz.brno.candg.ttdmmo.firebase.NewVehicleReq;
import cz.brno.candg.ttdmmo.firebase.PathReq;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.model.Path;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class VehicleServiceImpl implements VehicleService {
    
    final static Logger log = LoggerFactory.getLogger(VehicleServiceImpl.class);
    
    @Inject
    FbServerTime serverTime;
    
    @Inject
    private AuthUserDao userDao;
    
    @Inject
    private MapFieldDao mapFieldDao;
    
    @Inject
    private PathDao pathDao;
    
    @Inject
    private VehicleDao vehicleDao;
    
    private static int cislo = 0;
    
    public VehicleServiceImpl() {
        System.out.println("Inicializace bus service" + cislo);
        cislo++;
    }
    
    @Override
    public void move(MapField mapField, String nextMapField_id, Bus bus) {
        double estimatedArriveTimeMs = serverTime.getServerTime() + 3000;
        mapFieldDao.removeBus(bus.getX(), bus.getY(), bus.getBus_id());
        /*turn around*/
        if (nextMapField_id == null) {
            if (bus.getDirection() == 1) {
                bus.setDirection(-1);
            } else {
                bus.setDirection(1);
            }
            bus.setNextField(MapField.indexFromXY(bus.getX(), bus.getY()));
            
            if (mapField.getType().equals("BF")) {
                bus.setCargo(30);
            } else {
                bus.setCargo(0);
                userDao.setMoney(bus.getUser_id(), 50);
            }
            
        } else {
            bus.setNextField(nextMapField_id);
        }
        
        bus.setPath_position(bus.getPath_position() + bus.getDirection());
        bus.setStartTime(estimatedArriveTimeMs);
        bus.setX(mapField.getX());
        bus.setY(mapField.getY());
        vehicleDao.update(bus);
        mapFieldDao.addBus(mapField.getX(), mapField.getY(), bus.getBus_id());
    }
    
    @Override
    public void create(MapField mapField, AuthUser authUser, NewVehicleReq req) {
        log.info("create vehicle");
        double estimatedArriveTimeMs = serverTime.getServerTime() + 1000 * 60 * 60 * 24 * 365 * 10;
        Bus bus = new Bus();
        bus.setName(req.getName());
        bus.setStartTime(estimatedArriveTimeMs);
        bus.setX(mapField.getX());
        bus.setY(mapField.getY());
        bus.setDirection(1);
        bus.setNextField(MapField.indexFromXY(mapField.getX(), mapField.getY()));
        bus.setColor(authUser.getColor());
        bus.setUser_id(authUser.getUser_id());
        userDao.setMoney(bus.getUser_id(), -90);
        vehicleDao.create(bus);
        mapFieldDao.addBus(mapField.getX(), mapField.getY(), bus.getBus_id());
        userDao.addBus(authUser.getUser_id(), bus.getBus_id());
    }
    
    @Override
    public void setPath(Bus bus, List<MapField> path, PathReq req) {
        double estimatedArriveTimeMs = serverTime.getServerTime();
        System.out.println("Path: " + path);
        if (path.get(0).getType().equals("B3") && path.get(path.size() - 1).getType().equals("BF")) {
            for (int i = 1; i < path.size() - 1; i++) {
                if (!path.get(i).getType().equals("A") && !path.get(i).getType().equals("A2") && !path.get(i).getType().equals("A3")) {
                    return;
                }
            }
        } else {
            return;
        }
        Path newPath = new Path();
        newPath.setPath(req.getStops());
        String id = pathDao.create(newPath);
        bus.setPath_position(1);
        bus.setPath_id(id);
        bus.setStartTime(estimatedArriveTimeMs);
        vehicleDao.update(bus);
    }
    
    @Override
    public void sell(AuthUser authUser, Bus bus, FbSellVehicleReq req) {
        vehicleDao.remove(bus.getBus_id());
        if (bus.getPath_id() != null) {
            pathDao.remove(bus.getPath_id());
        }
        mapFieldDao.removeBus(bus.getX(), bus.getY(), bus.getBus_id());
        userDao.removeBus(authUser.getUser_id(), bus.getBus_id());
        userDao.setMoney(bus.getUser_id(), 80);
    }
    
}
