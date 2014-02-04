/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.service.impl;

import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.VehicleDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.firebase.NewVehicleReq;
import cz.brno.candg.ttdmmo.firebase.PathReq;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;
import javax.inject.Inject;

/**
 *
 * @author lastuvka
 */
public class VehicleServiceImpl implements VehicleService {

    @Inject
    private AuthUserDao userDao;

    @Inject
    private MapFieldDao mapFieldDao;

    @Inject
    private VehicleDao vehicleDao;

    private static int cislo = 0;

    public VehicleServiceImpl() {
        System.out.println("Inicializace bus service" + cislo);
        cislo++;
    }

    @Override
    public void move(double serverOffset, MapField mapField, String nextMapField_id, Bus bus) {
        double estimatedArriveTimeMs = System.currentTimeMillis() + serverOffset + 3000;
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
    public void create(double serverOffset, MapField mapField, AuthUser authUser, NewVehicleReq req) {
        double estimatedArriveTimeMs = System.currentTimeMillis() + serverOffset + 3000 * 60 * 24 * 100;

        Bus bus = new Bus();
        bus.setName(req.getName());
        bus.setStartTime(estimatedArriveTimeMs);
        bus.setX(mapField.getX());
        bus.setY(mapField.getY());
        bus.setDirection(1);
        bus.setNextField(MapField.indexFromXY(mapField.getX(), mapField.getY()));
        bus.setUser_id(authUser.getUser_id());
        vehicleDao.create(bus);
        mapFieldDao.addBus(mapField.getX(), mapField.getY(), bus.getBus_id());
        userDao.addBus(authUser.getUser_id(), bus.getBus_id());
    }

    @Override
    public void setPath(double serverOffset, Bus bus, String path, PathReq req) {
        double estimatedArriveTimeMs = System.currentTimeMillis() + serverOffset + 3000;
        /*turn around*/
        if (path == null) {
            bus.setPath_id("1");
        } else {
            bus.setPath_id(path);
        }
        bus.setStartTime(estimatedArriveTimeMs);
        vehicleDao.update(bus);
    }

}
