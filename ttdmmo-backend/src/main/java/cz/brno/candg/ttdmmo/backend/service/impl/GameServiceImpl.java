package cz.brno.candg.ttdmmo.backend.service.impl;

import cz.brno.candg.ttdmmo.backend.dao.ConstDao;
import cz.brno.candg.ttdmmo.serviceapi.GameService;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import javax.inject.Inject;

/**
 * Impl. service for game
 *
 * @author lastuvka
 */
public class GameServiceImpl implements GameService {

    @Inject
    MapFieldService mapFieldService;

    @Inject
    ConstDao constDao;

    @Override
    public void createGame(int USER_NUM_OFFSET) {
        constDao.removeAll();
        mapFieldService.generateMiddle(0, 0);
        constDao.setUSER_NUM_OFFSET(USER_NUM_OFFSET);
    }

}
