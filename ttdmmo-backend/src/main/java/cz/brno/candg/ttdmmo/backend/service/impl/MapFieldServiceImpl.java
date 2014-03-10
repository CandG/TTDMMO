/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.service.impl;

import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.dao.MessageDao;
import cz.brno.candg.ttdmmo.backend.firebase.FbServerTime;
import cz.brno.candg.ttdmmo.firebase.FbMapReq;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.model.Message;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import cz.brno.candg.ttdmmo.serviceapi.dto.MapFieldDto;
import javax.inject.Inject;

/**
 *
 * @author lastuvka
 */
public class MapFieldServiceImpl implements MapFieldService {

    @Inject
    FbServerTime serverTime;

    @Inject
    private MapFieldDao mapFieldDao;

    @Inject
    private MessageDao messageDao;

    @Inject
    private AuthUserDao authUserDao;

    private static int cislo = 0;

    public MapFieldServiceImpl() {
        System.out.println("Inicializace user service" + cislo);
        cislo++;
    }

    @Override
    public void insertField(MapFieldDto mapFieldDto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void build(int money, MapField mapField, FbMapReq firebaseReq) {
        if (money > 0 && (mapField.getType().equals("B") || (isRoad(mapField.getType()) && firebaseReq.getType().equals("B")))) {
            authUserDao.setMoney(firebaseReq.getUser_id(), -100);
            mapField.setType(firebaseReq.getType());
            mapFieldDao.create(mapField);
        } else {
            Message message = new Message();
            message.setText("The operation can not be done!");
            message.setUser_id(firebaseReq.getUser_id());
            message.setXy(firebaseReq.getXy());
            messageDao.create(message);
        }
    }

    public boolean isRoad(String type) {
        return type.equals("A") || type.equals("A2") || type.equals("A3");
    }
}
