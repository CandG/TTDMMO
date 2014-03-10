/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.adapter;

import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToMapFieldServiceListener;
import cz.brno.candg.ttdmmo.firebase.FbMapReq;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class MapServiceAdapter {

    final static Logger log = LoggerFactory.getLogger(MapServiceAdapter.class);

    @Inject
    MapFieldService mapFieldService;

    @Inject
    private AuthUserDao userDao;

    @Inject
    private MapFieldDao mapFieldDao;

    public void buildField(FbMapReq fbReq) {
        DataToMapFieldServiceListener dataToServiceListener = new DataToMapFieldServiceListener();
        dataToServiceListener.setMapFieldService(mapFieldService);
        dataToServiceListener.setFbReq(fbReq);
        userDao.getMoney(fbReq.getUser_id(), dataToServiceListener);
        mapFieldDao.getByID(fbReq.getXy(), dataToServiceListener);
        log.info("req" + fbReq.toString());
    }
}
