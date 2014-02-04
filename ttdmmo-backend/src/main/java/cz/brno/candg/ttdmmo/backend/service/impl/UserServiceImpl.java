package cz.brno.candg.ttdmmo.backend.service.impl;

import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
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
    private AuthUserDao userDao;

    private static int cislo = 0;

    public UserServiceImpl() {
        System.out.println("Inicializace user service" + cislo);
        cislo++;
    }

    public void setUserDao(AuthUserDao userDao) {
        this.userDao = userDao;
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

    public void getByIdFirebase(final Long id) {

    }
}
