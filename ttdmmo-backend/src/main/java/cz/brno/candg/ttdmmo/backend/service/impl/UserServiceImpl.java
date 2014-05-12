package cz.brno.candg.ttdmmo.backend.service.impl;

import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.VehicleDao;
import cz.brno.candg.ttdmmo.backend.firebase.FbServerTime;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.dto.NewUserDTO;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.serviceapi.UserService;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User service for all operations on User.
 *
 * @author Lastuvka
 */
public class UserServiceImpl implements UserService {

    final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    FbServerTime serverTime;

    @Inject
    private AuthUserDao userDao;

    @Inject
    private VehicleDao vehicleDao;

    @Override
    public void register(NewUserDTO userDTO, int x, int y) {
        AuthUser user = new AuthUser();
        user.setName(userDTO.getName());
        user.setUser_id(userDTO.getUser_id());
        user.setEmail(userDTO.getEmail());
        user.setMoney(FbRef.START_MONEY);
        String position = MapField.indexFromXY(x, y);
        user.setPosition(position);
        user.setColor(FbRef.START_COLOR);
        userDao.update(user);
    }

    @Override
    public void changeColor(AuthUser user, String color) {
        for (String vehicle_id : user.getVehicles().keySet()) {
            vehicleDao.changeColor(vehicle_id, color);
        }
        userDao.changeColor(user.getUser_id(), color);
    }
}
