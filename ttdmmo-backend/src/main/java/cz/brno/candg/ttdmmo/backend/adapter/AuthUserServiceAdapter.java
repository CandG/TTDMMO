/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.adapter;

import static cz.brno.candg.ttdmmo.backend.adapter.VehicleServiceAdapter.log;
import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToChangeColorServiceListener;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToMapFieldServiceListener;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToNewVehicleServiceListener;
import cz.brno.candg.ttdmmo.firebase.FbChangeColorReq;
import cz.brno.candg.ttdmmo.firebase.FbMapReq;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import cz.brno.candg.ttdmmo.serviceapi.UserService;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class AuthUserServiceAdapter {

    final static Logger log = LoggerFactory.getLogger(AuthUserServiceAdapter.class);

    @Inject
    UserService userService;

    @Inject
    private AuthUserDao userDao;

    public void changeColor(FbChangeColorReq fbReq) {
        DataToChangeColorServiceListener dataToChangeColorServiceListener = new DataToChangeColorServiceListener();
        dataToChangeColorServiceListener.setUserService(userService);
        dataToChangeColorServiceListener.setFbReq(fbReq);
        userDao.get(fbReq.getUser_id(), dataToChangeColorServiceListener);
        log.info("req changeColor:" + fbReq);
    }
}
