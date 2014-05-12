package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.dto.SellVehicleDTO;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.model.Path;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data to vehicle service for method sell
 *
 * @author lastuvka
 */
public class DataToSellVehicleServiceListener implements ValueEventListener {

    final static Logger log = LoggerFactory.getLogger(DataToSellVehicleServiceListener.class);

    private SellVehicleDTO fbReq;
    private VehicleService vehicleService;
    private Path path = null;
    private String isPath = "x";
    private AuthUser authUser = null;

    @Override
    public void onDataChange(DataSnapshot ds) {
        if (ds.getName().equals(fbReq.getVehicle_id())) {
            log.info("Additional data recived: path - " + ds.getValue());
            setPath(ds.getValue(Path.class));
            isPath = null;
        } else {
            log.info("Additional data recived: user - " + ds.getValue());
            setAuthUser(ds.getValue(AuthUser.class));
        }

        if ((getPath() != null || isPath == null) && getAuthUser() != null) {
            vehicleService.sell(authUser, path, fbReq);
        }
    }

    @Override
    public void onCancelled(FirebaseError fe) {
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public SellVehicleDTO getFbReq() {
        return fbReq;
    }

    public void setFbReq(SellVehicleDTO fbReq) {
        this.fbReq = fbReq;
    }

    public VehicleService getVehicleService() {
        return vehicleService;
    }

    public void setVehicleService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }

}
