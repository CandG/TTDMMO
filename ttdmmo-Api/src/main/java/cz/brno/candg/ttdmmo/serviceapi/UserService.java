package cz.brno.candg.ttdmmo.serviceapi;

import cz.brno.candg.ttdmmo.dto.NewUserDTO;
import cz.brno.candg.ttdmmo.model.AuthUser;

/**
 * User service interface for operations on User.
 *
 * @author Lastuvka
 */
public interface UserService {

    /**
     * Change color for given user
     *
     * @param user
     * @param color
     */
    void changeColor(AuthUser user, String color);

    /**
     * Add to existing user money, color,position - basic settings.
     *
     * @param user
     * @param x
     * @param y
     */
    void register(NewUserDTO user, int x, int y);

}
