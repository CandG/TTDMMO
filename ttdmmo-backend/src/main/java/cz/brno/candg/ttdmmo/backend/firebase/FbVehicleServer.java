package cz.brno.candg.ttdmmo.backend.firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.backend.adapter.VehicleServiceAdapterImpl;
import cz.brno.candg.ttdmmo.backend.firebase.FbServerTime;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.Vehicle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import static java.util.concurrent.TimeUnit.SECONDS;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class FbVehicleServer {

    final static Logger log = LoggerFactory.getLogger(FbVehicleServer.class);
    private int start;
    private int end;
    private boolean isRunning;

    @Inject
    FbServerTime serverTime;

    @Inject
    VehicleServiceAdapterImpl vehicleServiceAdapter;

    private final Firebase refBuses = new Firebase(FbRef.refD + "vehicles");

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> beeperHandle;
    Runnable beeper;

    public FbVehicleServer() {
        isRunning = false;
        beeper = new Runnable() {
            @Override
            public void run() {
                log.info("Check for vehicles arriving at " + serverTime.getServerTime());
                checkBuses();
            }
        };
    }

    public void start(int start, int end) {
        log.info("start FbVehicleServer");
        this.start = start;
        this.end = end;
        if (!isRunning) {
            beeperHandle = scheduler.scheduleAtFixedRate(beeper, 2, FbRef.SYNC_INTERVAL, SECONDS);
            isRunning = true;
        }
    }

    public void stop() {
        log.info("stop FbVehicleServer");
        if (isRunning) {
            beeperHandle.cancel(true);
            isRunning = false;
        }
    }

    public void checkBuses() {

        refBuses.endAt(serverTime.getServerTime()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot ds) {
                for (DataSnapshot vehicleDs : ds.getChildren()) {
                    Vehicle vehicle = vehicleDs.getValue(Vehicle.class);
                    if (vehicle.getP() >= start && vehicle.getP() <= end) {
                        log.info("Vehicle: " + vehicle);
                        vehicleServiceAdapter.moveVehicle(vehicle);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError fe) {
            }

        });
    }
}
