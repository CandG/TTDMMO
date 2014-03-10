package cz.brno.candg.ttdmmo.serviceapi;

import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.serviceapi.dto.AuthUserDto;

/**
 * User service interface for operations on User DTO.
 *
 * @author Jan Kucera (Greld)
 */
public interface UserService {

    void changeColor(AuthUser user, String color);

    void setPosition(AuthUser user);

    /**
     * Verify if user with given username and password exists.
     *
     * @param username
     * @param password
     * @return User if user with given username and password exist, null
     * otherwise
     */
    AuthUserDto login(String username, String password);

    /**
     * Create new user.
     *
     * @param user
     * @param password
     * @return User id if registration was successfull, null otherwise
     */
    Long register(AuthUserDto user, String password);

    /**
     * Update user
     *
     * @param user
     */
    void update(AuthUserDto user);

    /**
     * Remove user
     *
     * @param user
     */
    void remove(AuthUserDto user);

    /**
     * Find user by username
     *
     * @param username
     * @return user with given username.
     */
    AuthUserDto getByUsername(String username);

    /**
     * Find user by id
     *
     * @param id
     * @return user with given id.
     */
    AuthUserDto getById(Long id);

}
