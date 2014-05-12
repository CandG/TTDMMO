package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.dto.PathDTO;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.model.Path;
import cz.brno.candg.ttdmmo.model.Vehicle;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data to vehicle service for method setPath
 *
 * @author lastuvka
 */
public class DataToPathVehicleServiceListener implements ValueEventListener {
    
    final static Logger log = LoggerFactory.getLogger(DataToPathVehicleServiceListener.class);
    
    private PathDTO fbReq;
    private VehicleService vehicleService;
    private List<MapField> stops = new ArrayList<MapField>();
    private Vehicle vehicle;
    private Path path = null;
    private String isPath = "x";
    
    public Path getPath() {
        return path;
    }
    
    public void setPath(Path path) {
        this.path = path;
    }
    
    public String getIsPath() {
        return isPath;
    }
    
    public void setIsPath(String isPath) {
        this.isPath = isPath;
    }
    
    public PathDTO getFbReq() {
        return fbReq;
    }
    
    public void setFbReq(PathDTO fbReq) {
        this.fbReq = fbReq;
    }
    
    public VehicleService getVehicleService() {
        return vehicleService;
    }
    
    public void setVehicleService(VehicleService busService) {
        this.vehicleService = busService;
    }
    
    public List<MapField> getStops() {
        return stops;
    }
    
    public void setStops(List<MapField> stops) {
        this.stops = stops;
    }
    
    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    
    @Override
    public void onDataChange(DataSnapshot ds) {
        if (ds.getName().equals(fbReq.getVehicle_id()) && ds.hasChild("name")) {
            log.info("Additional data recived: vehicle - " + ds.getValue());
            setVehicle(ds.getValue(Vehicle.class));
        } else if (ds.getName().equals(fbReq.getVehicle_id())) {
            log.info("Additional data recived: path - " + ds.getValue());
            setPath(ds.getValue(Path.class));
            isPath = null;
        } else {
            log.info("Additional data recived: mapField - " + ds.getValue());
            stops.add(ds.getValue(MapField.class));
        }
        
        if ((getPath() != null || isPath == null) && getVehicle() != null && stops != null && stops.size() == fbReq.getStops().size()) {
            vehicleService.setPath(vehicle, path, stops, fbReq);
        }
    }
    
    @Override
    public void onCancelled(FirebaseError fe) {
    }
    
}
