package cz.brno.candg.ttdmmo.backend.service.impl;

import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.VehicleDao;
import cz.brno.candg.ttdmmo.backend.firebase.FbServerTime;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.serviceapi.UserService;
import cz.brno.candg.ttdmmo.serviceapi.dto.AuthUserDto;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User service for all operations on UserStats DTO.
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
    
    private static int cislo = 0;
    
    public UserServiceImpl() {
        System.out.println("Inicializace user service" + cislo);
        cislo++;
    }
    
    @Override
    public AuthUserDto login(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Long register(AuthUserDto user, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void update(AuthUserDto user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void remove(AuthUserDto user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public AuthUserDto getByUsername(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public AuthUserDto getById(Long id) {
        return null;
        //     return AuthUserConvert.fromEntityToDto(userDao.get(1L));
    }
    
    @Override
    public void setPosition(AuthUser user) {
        user.setMoney(500);
        user.setPosition(cityPosition(getUser_num(user.getUser_id())));
        user.setColor("#ffffff");
        userDao.update(user);
    }
    
    public int getUser_num(String user_id) {
        String[] splits = user_id.split(":");
        return Integer.parseInt(splits[1]);
    }
    
    public String cityPosition(int num) {
        int x = 0;
        int y = 0;
        int padding = (FbRef.NUM_FIELDS / FbRef.NUM_CITIES_IN_ROW);
        int margin = padding / 2;
        int row = num / FbRef.NUM_CITIES_IN_ROW;
        int col = num % FbRef.NUM_CITIES_IN_ROW;
        if (num != 0) {
            x = (margin + row * padding) - FbRef.NUM_FIELDS / 2;
            y = (margin + col * padding) - FbRef.NUM_FIELDS / 2;
        }
        return MapField.indexFromXY(x, y);
    }
    
    @Override
    public void changeColor(AuthUser user, String color) {
        for (String bus_id : user.getBuses().keySet()) {
            vehicleDao.changeColor(bus_id, color);
        }
        userDao.changeColor(user.getUser_id(), color);
    }
}
