package cz.brno.candg.ttdmmo.frontend.firebase;

import cz.brno.candg.ttdmmo.firebase.FBMapReq;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToMapFieldServiceListener;
import cz.brno.candg.ttdmmo.frontend.MainServlet;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class FirebaseMapServer {

    final static Logger log = LoggerFactory.getLogger(FirebaseMapServer.class);

    private ChildEventListener childEventListener;

    @Inject
    MapFieldService mapFieldService;

    @Inject
    private AuthUserDao userDao;

    @Inject
    private MapFieldDao mapFieldDao;

    // Create a reference to a Firebase location
    private Firebase refRequests = new Firebase("https://ttdmmoq1.firebaseio-demo.com/fronta");

    //   private Firebase ref = new Firebase("https://oxv0sz0x1iy.firebaseio-demo.com/");
    //   private Firebase refOut = new Firebase("https://zoqukd20vgy.firebaseio-demo.com/");
    private static int cislo = 0;

    public FirebaseMapServer() {
        cislo++;
        log.info("Inicializace FirebaseMapServer: " + cislo);
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                FBMapReq fbReq = snapshot.getValue(FBMapReq.class);
                refRequests.child(snapshot.getName()).removeValue();
                prepareDataToService(fbReq);
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

    public void prepareDataToService(FBMapReq fbReq) {
        DataToMapFieldServiceListener dataToServiceListener = new DataToMapFieldServiceListener();
        dataToServiceListener.setMapFieldService(mapFieldService);
        dataToServiceListener.setFbReq(fbReq);
        userDao.getMoney(fbReq.getUser_id(), dataToServiceListener);
        mapFieldDao.getXY(fbReq.getX(), fbReq.getY(), dataToServiceListener);
        System.out.println("req" + fbReq.toString());
    }
}
