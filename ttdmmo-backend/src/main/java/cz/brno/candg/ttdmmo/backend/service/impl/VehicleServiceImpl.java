package cz.brno.candg.ttdmmo.backend.service.impl;

import cz.brno.candg.ttdmmo.backend.adapter.MapServiceAdapterImpl;
import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.CityDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.dao.MessageDao;
import cz.brno.candg.ttdmmo.backend.dao.PathDao;
import cz.brno.candg.ttdmmo.backend.dao.VehicleDao;
import cz.brno.candg.ttdmmo.backend.firebase.FbConstants;
import cz.brno.candg.ttdmmo.backend.firebase.FbServerTime;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.constants.MapTypes;
import cz.brno.candg.ttdmmo.constants.VehicleTypes;
import cz.brno.candg.ttdmmo.dto.NewVehicleDTO;
import cz.brno.candg.ttdmmo.dto.PathDTO;
import cz.brno.candg.ttdmmo.dto.SellVehicleDTO;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.model.Message;
import cz.brno.candg.ttdmmo.model.Path;
import cz.brno.candg.ttdmmo.model.Vehicle;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service implementation for vehicle
 *
 * @author lastuvka
 */
public class VehicleServiceImpl implements VehicleService {

    final static Logger log = LoggerFactory.getLogger(VehicleServiceImpl.class);

    @Inject
    FbServerTime serverTime;

    @Inject
    FbConstants fbConstants;

    @Inject
    private CityDao cityDao;

    @Inject
    private AuthUserDao userDao;

    @Inject
    private MapFieldDao mapFieldDao;

    @Inject
    private MessageDao messageDao;

    @Inject
    private PathDao pathDao;

    @Inject
    private VehicleDao vehicleDao;

    @Inject
    MapServiceAdapterImpl mapServiceAdapter;

    @Override
    public void move(Vehicle vehicle) {
        double estimatedArriveTimeMs = serverTime.getServerTime();
        double diff = estimatedArriveTimeMs - (vehicle.getStartTime() + vehicle.getP_size() * vehicle.getSpeed() - FbRef.PROCCESING_DELAY);
        estimatedArriveTimeMs -= (diff % (FbRef.SYNC_INTERVAL * 1000));

        if (vehicle.getDirection() == 1) {
            vehicle.setDirection(-1);
            vehicle.setCargo(VehicleTypes.getAmount(vehicle.getType()));
        } else {
            vehicle.setDirection(1);
            vehicle.setCargo(0);
            userDao.updateMoney(vehicle.getUser_id(), VehicleTypes.getPrice(vehicle.getType()));
            if (VehicleTypes.getName(vehicle.getType()).equals("Farm")) {
                cityDao.changeFood(vehicle.getCity_id(), VehicleTypes.getAmount(vehicle.getType()), mapServiceAdapter);
            } else if (VehicleTypes.getName(vehicle.getType()).equals("Wood")) {
                cityDao.changeWood(vehicle.getCity_id(), VehicleTypes.getAmount(vehicle.getType()), mapServiceAdapter);
            } else if (VehicleTypes.getName(vehicle.getType()).equals("Bus")) {
                cityDao.changeFood(vehicle.getCity_id(), VehicleTypes.getAmount(vehicle.getType()), mapServiceAdapter);
            }

        }

        vehicle.setStartTime(estimatedArriveTimeMs);
        vehicleDao.update(vehicle);
    }

    @Override
    public void create(AuthUser authUser, NewVehicleDTO req) {
        if (authUser.getMoney() >= VehicleTypes.getBuyPrice(req.getType())) {
            log.info("Create vehicle service");
            double estimatedArriveTimeMs = serverTime.getFutureTime();
            Vehicle vehicle = new Vehicle();
            vehicle.setName(req.getName());
            vehicle.setSpeed(VehicleTypes.getSpeed(req.getType()));
            vehicle.setType(req.getType());
            vehicle.setStartTime(estimatedArriveTimeMs);
            vehicle.setDirection(1);
            vehicle.setColor(authUser.getColor());
            vehicle.setUser_id(authUser.getUser_id());
            vehicle.setCity_id(authUser.getPosition());
            Random rand = new Random();
            int n = rand.nextInt(FbRef.MAX_SERVERS) + 1;
            vehicle.setP(n);
            userDao.updateMoney(vehicle.getUser_id(), -VehicleTypes.getBuyPrice(req.getType()));
            vehicleDao.create(vehicle);
            userDao.addVehicle(authUser.getUser_id(), vehicle.getVehicle_id());
        } else {
            Message message = new Message();
            message.setText("The operation can not be done!");
            message.setUser_id(authUser.getUser_id());
            messageDao.create(message);
        }
    }

    @Override
    public void setPath(Vehicle vehicle, Path oldPath, List<MapField> path, PathDTO req) {
        log.info("Set path service");
        if (oldPath != null) {
            int count = oldPath.getPath().size();
            for (int i = 0; i < count; i++) {
                mapFieldDao.removePath(oldPath.getPath().get(i), vehicle.getVehicle_id());
            }
            pathDao.remove(vehicle.getVehicle_id());
        }

        double estimatedArriveTimeMs = serverTime.getServerTime();

        if (path.get(0).getType().equals(MapTypes.Warehouse) && path.get(path.size() - 1).getType().startsWith(MapTypes.stop)) {
            for (int i = 1; i < path.size() - 1; i++) {
                if (!path.get(i).getType().startsWith(MapTypes.road)) {
                    return;
                }
            }
        } else {
            return;
        }
        for (int i = 0; i < path.size(); i++) {
            mapFieldDao.addPath(path.get(i).getX(), path.get(i).getY(), vehicle.getVehicle_id());
        }

        Path newPath = new Path();
        newPath.setPath(req.getStops());
        newPath.setId(vehicle.getVehicle_id());
        pathDao.update(newPath);
        vehicle.setP_size(path.size());
        vehicle.setStartTime(estimatedArriveTimeMs);
        vehicleDao.update(vehicle);
    }

    @Override
    public void sell(AuthUser authUser, Path path, SellVehicleDTO req) {
        if (path != null) {
            int count = path.getPath().size();
            for (int i = 0; i < count; i++) {
                mapFieldDao.removePath(path.getPath().get(i), req.getVehicle_id());
            }
            pathDao.remove(req.getVehicle_id());
        }
        vehicleDao.remove(req.getVehicle_id());
        userDao.removeVehicle(authUser.getUser_id(), req.getVehicle_id());
        userDao.updateMoney(req.getUser_id(), 5000);
    }

}
