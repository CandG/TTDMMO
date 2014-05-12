package cz.brno.candg.ttdmmo.adapterapi;

import cz.brno.candg.ttdmmo.dto.ChangeColorDTO;

/**
 * User service adapter interface for operations on User.
 *
 * @author lastuvka
 */
public interface AuthUserServiceAdapter {

    /**
     * Adaptar gives all data for service method change color - user
     *
     * @param changeColorDTO
     */
    void changeColor(ChangeColorDTO changeColorDTO);
}
