package cz.brno.candg.ttdmmo.backend.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.backend.adapter.MapServiceAdapterImpl;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.dto.MapFieldDTO;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class FbMapServer {

    final static Logger log = LoggerFactory.getLogger(FbMapServer.class);

    private final ChildEventListener childEventListener;

    @Inject
    MapServiceAdapterImpl mapServiceAdapter;

    private Firebase refRequests = new Firebase(FbRef.refQ + "build");

    private int start;
    private int end;

    public FbMapServer() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                MapFieldDTO fbReq = snapshot.getValue(MapFieldDTO.class);
                refRequests.child(snapshot.getName()).removeValue();
                mapServiceAdapter.buildField(fbReq);
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
