package cz.brno.candg.ttdmmo.backend.adapter;

import cz.brno.candg.ttdmmo.adapterapi.AuthUserServiceAdapter;
import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToChangeColorServiceListener;
import cz.brno.candg.ttdmmo.dto.ChangeColorDTO;
import cz.brno.candg.ttdmmo.serviceapi.UserService;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Impl. User service adapter interface for operations on User.
 *
 * @author lastuvka
 */
public class AuthUserServiceAdapterImpl implements AuthUserServiceAdapter {

    final static Logger log = LoggerFactory.getLogger(AuthUserServiceAdapterImpl.class);

    @Inject
    UserService userService;

    @Inject
    private AuthUserDao userDao;

    @Override
    public void changeColor(ChangeColorDTO fbReq) {
        log.info("Change Color: " + fbReq);
        DataToChangeColorServiceListener dataToChangeColorServiceListener = new DataToChangeColorServiceListener();
        dataToChangeColorServiceListener.setUserService(userService);
        dataToChangeColorServiceListener.setFbReq(fbReq);
        userDao.get(fbReq.getUser_id(), dataToChangeColorServiceListener);
    }
}
