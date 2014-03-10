package cz.brno.candg.ttdmmo.frontend.firebase;

import cz.brno.candg.ttdmmo.firebase.FbMapReq;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.backend.adapter.MapServiceAdapter;
import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToMapFieldServiceListener;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class FbMapServer {

    final static Logger log = LoggerFactory.getLogger(FbMapServer.class);

    private ChildEventListener childEventListener;

    @Inject
    MapServiceAdapter mapServiceAdapter;

    private Firebase refRequests = new Firebase(FbRef.refQ + "build");

    private static int cislo = 0;

    public FbMapServer() {
        cislo++;
        log.info("Inicializace FirebaseMapServer: " + cislo);
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                FbMapReq fbReq = snapshot.getValue(FbMapReq.class);
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

    public void start() {
        refRequests.addChildEventListener(childEventListener);
    }

    public void stop() {
        refRequests.removeEventListener(childEventListener);
    }
}
