package cz.brno.candg.ttdmmo.serviceapi;

import cz.brno.candg.ttdmmo.dto.NewVehicleDTO;
import cz.brno.candg.ttdmmo.dto.PathDTO;
import cz.brno.candg.ttdmmo.dto.SellVehicleDTO;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.model.Path;
import cz.brno.candg.ttdmmo.model.Vehicle;
import java.util.List;

/**
 * Vehicle service interface for operations on vehicles.
 *
 * @author lastuvka
 */
public interface VehicleService {

    /**
     * Move vehicle (change direction, in/out cargo)
     *
     * @param bus
     */
    public void move(Vehicle bus);

    /**
     * Create new vehicle for user
     *
     * @param authUser
     * @param req
     */
    public void create(AuthUser authUser, NewVehicleDTO req);

    /**
     * Sell vehicle of user / delete vehicles path
     *
     * @param authUser
     * @param path
     * @param req
     */
    public void sell(AuthUser authUser, Path path, SellVehicleDTO req);

    /**
     * Set path for vehicle
     *
     * @param vehicle
     * @param oldPath
     * @param path
     * @param req
     */
    public void setPath(Vehicle vehicle, Path oldPath, List<MapField> path, PathDTO req);
}
