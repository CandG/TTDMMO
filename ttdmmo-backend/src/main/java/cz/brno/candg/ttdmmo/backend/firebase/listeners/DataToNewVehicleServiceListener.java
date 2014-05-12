package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.dto.NewVehicleDTO;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener for additional data to service
 *
 *
 * @author lastuvka
 */
public class DataToNewVehicleServiceListener implements ValueEventListener {

    final static Logger log = LoggerFactory.getLogger(DataToNewVehicleServiceListener.class);

    private NewVehicleDTO fbReq;
    private VehicleService vehicleService;
    private AuthUser authUser = null;

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }

    public NewVehicleDTO getFbReq() {
        return fbReq;
    }

    public void setFbReq(NewVehicleDTO fbReq) {
        this.fbReq = fbReq;
    }

    @Override
    public void onDataChange(DataSnapshot ds) {
        setAuthUser(ds.getValue(AuthUser.class));
        log.info("Additional data recived: user - " + ds.getValue());

        if (getAuthUser() != null) {
            vehicleService.create(authUser, fbReq);
        }
    }

    @Override
    public void onCancelled(FirebaseError fe) {
    }

    public VehicleService getVehicleService() {
        return vehicleService;
    }

    public void setVehicleService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

}
