package cz.brno.candg.ttdmmo.frontend.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.firebase.client.core.ServerValues;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToVehicleServiceListener;
import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.serviceapi.VehicleService;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import static java.util.concurrent.TimeUnit.SECONDS;
import javax.inject.Inject;

/**
 *
 * @author lastuvka
 */
public class BusController {

    @Inject
    VehicleService busService;

    @Inject
    private MapFieldDao mapFieldDao;

    private final Firebase refBuses = new Firebase("https://ttdmmo1.firebaseio-demo.com/buses");
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> beeperHandle;
    Runnable beeper;
    private static double serverOffset = 0;

    public BusController() {
        getServerTime();
        beeper = new Runnable() {
            public void run() {
                checkBuses();
                System.out.println("checking...");
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
        double estimatedServerTimeMs = System.currentTimeMillis() + serverOffset;
        System.out.println(estimatedServerTimeMs + " case " + serverOffset);

        refBuses.endAt(estimatedServerTimeMs).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot ds) {
                for (DataSnapshot busDs : ds.getChildren()) {
                    Bus bus = busDs.getValue(Bus.class);
                    prepareDataToService(bus);
                    System.out.println("Bus: " + bus);
                }
            }

            @Override
            public void onCancelled(FirebaseError fe) {
            }

        }
        );

    }

    public void getServerTime() {
        //ServerValue.TIMESTAMP
        // ServerValue.TIMESTAMP // This displays all chat messages set at or before 7:32 PM on 2/11/2012:
        Firebase offsetRef = new Firebase("https://ttdmmo1.firebaseio-demo.com/.info/serverTimeOffset");
        offsetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                double offset = snapshot.getValue(Double.class);
                serverOffset = offset;
            }

            @Override
            public void onCancelled(FirebaseError fe) {
            }
        });

    }

    public void prepareDataToService(final Bus bus) {
        DataToVehicleServiceListener dataToBusServiceListener = new DataToVehicleServiceListener();
        dataToBusServiceListener.setBusService(busService);
        dataToBusServiceListener.setBus(bus);
        dataToBusServiceListener.setServerOffset(serverOffset);

        String pos;

        pos = Integer.toString(bus.getPath_position() + bus.getDirection());

        mapFieldDao.getFromPath(bus.getPath_id(), pos, dataToBusServiceListener);
        mapFieldDao.getByID(bus.getNextField(), dataToBusServiceListener);
    }
}
