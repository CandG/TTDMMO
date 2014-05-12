package cz.brno.candg.ttdmmo.backend.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.backend.adapter.VehicleServiceAdapterImpl;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.dto.PathDTO;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class FbVehiclePathServer {

    final static Logger log = LoggerFactory.getLogger(FbVehiclePathServer.class);

    private final ChildEventListener childEventListener;

    @Inject
    VehicleServiceAdapterImpl vehicleServiceAdapter;

    private Firebase refRequests = new Firebase(FbRef.refQ + "pathVehicle");

    private int start;
    private int end;

    public FbVehiclePathServer() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                PathDTO fbReq = snapshot.getValue(PathDTO.class);
                refRequests.child(snapshot.getName()).removeValue();
                vehicleServiceAdapter.setPath(fbReq);
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(FirebaseError fe) {
            }
        };
    }

    public void start(int start, int end) {
        this.start = start;
        this.end = end;
        refRequests.startAt(start).endAt(end).addChildEventListener(childEventListener);
    }

    public void stop() {
        refRequests.startAt(start).endAt(end).removeEventListener(childEventListener);
    }

}
