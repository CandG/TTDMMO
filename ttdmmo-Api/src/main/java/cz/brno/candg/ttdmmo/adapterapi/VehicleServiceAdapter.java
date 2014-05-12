package cz.brno.candg.ttdmmo.adapterapi;

import cz.brno.candg.ttdmmo.dto.NewVehicleDTO;
import cz.brno.candg.ttdmmo.dto.PathDTO;
import cz.brno.candg.ttdmmo.dto.SellVehicleDTO;
import cz.brno.candg.ttdmmo.model.Vehicle;

/**
 * Vehicle service adapter interface for operations on Vehicle.
 *
 * @author lastuvka
 */
public interface VehicleServiceAdapter {

    /**
     * Adaptar gives all data for service method - user
     *
     * @param fbReq
     */
    void newVehicle(NewVehicleDTO fbReq);

    /**
     * Adaptar gives all data for service method - old path, vehicle and map
     * field on path
     *
     * @param fbReq
     */
    public void setPath(PathDTO fbReq);

    /**
     * Adaptar gives all data for service method
     *
     * @param vehicle
     */
    public void moveVehicle(Vehicle vehicle);

    /**
     * Adaptar gives all data for service method - user and path
     *
     * @param fbReq
     */
    public void sellVehicle(SellVehicleDTO fbReq);
}
