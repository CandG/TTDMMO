package cz.brno.candg.ttdmmo.frontend.firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.backend.adapter.VehicleServiceAdapter;
import cz.brno.candg.ttdmmo.backend.firebase.FbServerTime;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.Bus;
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
public class BusController {

    final static Logger log = LoggerFactory.getLogger(BusController.class);

    @Inject
    FbServerTime serverTime;

    @Inject
    VehicleServiceAdapter vehicleServiceAdapter;

    private final Firebase refBuses = new Firebase(FbRef.ref + "buses");

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> beeperHandle;
    Runnable beeper;

    public BusController() {

        beeper = new Runnable() {
            @Override
            public void run() {
                log.info("checking... " + serverTime.getServerTime());
                checkBuses();
            }
        };

    }

    public void start() {
        beeperHandle = scheduler.scheduleAtFixedRate(beeper, 5, 3, SECONDS);
    }

    public void stop() {
        scheduler.schedule(new Runnable() {
            public void run() {
                beeperHandle.cancel(true);
            }
        }, 0, SECONDS);
    }

    public void checkBuses() {

        refBuses.endAt(serverTime.getServerTime()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot ds) {
                for (DataSnapshot busDs : ds.getChildren()) {
                    Bus bus = busDs.getValue(Bus.class);
                    vehicleServiceAdapter.moveVehicle(bus);
                    log.info("Bus: " + bus);
                }
            }

            @Override
            public void onCancelled(FirebaseError fe) {
            }

        }
        );

    }
}
