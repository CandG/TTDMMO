/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.service.impl;

import cz.brno.candg.ttdmmo.backend.dao.BusDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.serviceapi.BusService;
import javax.inject.Inject;

/**
 *
 * @author lastuvka
 */
public class BusServiceImpl implements BusService {

    @Inject
    private MapFieldDao mapFieldDao;

    @Inject
    private BusDao busDao;

    private static int cislo = 0;

    public BusServiceImpl() {
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
        } else {
            bus.setNextField(nextMapField_id);
        }

        bus.setPath_position(bus.getPath_position() + bus.getDirection());
        bus.setStartTime(estimatedArriveTimeMs);
        bus.setX(mapField.getX());
        bus.setY(mapField.getY());
        busDao.update(bus);
        mapFieldDao.addBus(mapField.getX(), mapField.getY(), bus.getBus_id());
    }

}
