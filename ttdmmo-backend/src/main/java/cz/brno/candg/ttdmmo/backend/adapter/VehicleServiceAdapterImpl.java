package cz.brno.candg.ttdmmo.backend.adapter;

import cz.brno.candg.ttdmmo.adapterapi.VehicleServiceAdapter;
import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.dao.PathDao;
import cz.brno.candg.ttdmmo.backend.dao.VehicleDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToNewVehicleServiceListener;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToPathVehicleServiceListener;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToSellVehicleServiceListener;
import cz.brno.candg.ttdmmo.dto.NewVehicleDTO;
import cz.brno.candg.ttdmmo.dto.PathDTO;
import cz.brno.candg.ttdmmo.dto.SellVehicleDTO;
import cz.brno.candg.ttdmmo.model.Vehicle;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter from request to service for vehicles
 *
 * @author lastuvka
 */
public class VehicleServiceAdapterImpl implements VehicleServiceAdapter {

    final static Logger log = LoggerFactory.getLogger(VehicleServiceAdapterImpl.class);

    @Inject
    private PathDao pathDao;

    @Inject
    private VehicleDao vehicleDao;

    @Inject
    private AuthUserDao userDao;

    @Inject
    private MapFieldDao mapFieldDao;

    @Inject
    VehicleService vehicleService;

    @Override
    public void newVehicle(NewVehicleDTO fbReq) {
        log.info("New vehicle: " + fbReq);
        DataToNewVehicleServiceListener dataToNewVehicleServiceListener = new DataToNewVehicleServiceListener();
        dataToNewVehicleServiceListener.setVehicleService(vehicleService);
        dataToNewVehicleServiceListener.setFbReq(fbReq);
        userDao.get(fbReq.getUser_id(), dataToNewVehicleServiceListener);
    }

    @Override
    public void setPath(PathDTO fbReq) {
        if (fbReq.getStops() != null && fbReq.getStops().size() > 1) {
            log.info("Set Path: " + fbReq);
            DataToPathVehicleServiceListener dataToPathVehicleServiceListener = new DataToPathVehicleServiceListener();
            dataToPathVehicleServiceListener.setVehicleService(vehicleService);
            dataToPathVehicleServiceListener.setFbReq(fbReq);
            pathDao.get(fbReq.getVehicle_id(), dataToPathVehicleServiceListener);
            vehicleDao.get(fbReq.getVehicle_id(), dataToPathVehicleServiceListener);
            for (String field : fbReq.getStops()) {
                mapFieldDao.get(field, dataToPathVehicleServiceListener);
            }
        }
    }

    @Override
    public void moveVehicle(Vehicle vehicle) {
        vehicleService.move(vehicle);
    }

    @Override
    public void sellVehicle(SellVehicleDTO fbReq) {
        DataToSellVehicleServiceListener dataToSellVehicleServiceListener = new DataToSellVehicleServiceListener();
        dataToSellVehicleServiceListener.setVehicleService(vehicleService);
        dataToSellVehicleServiceListener.setFbReq(fbReq);
        userDao.get(fbReq.getUser_id(), dataToSellVehicleServiceListener);
        pathDao.get(fbReq.getVehicle_id(), dataToSellVehicleServiceListener);
    }
}
