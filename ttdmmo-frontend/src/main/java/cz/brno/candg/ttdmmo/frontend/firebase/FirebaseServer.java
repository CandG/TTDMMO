package cz.brno.candg.ttdmmo.frontend.firebase;

import cz.brno.candg.ttdmmo.firebase.FirebaseReq;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.backend.dao.AuthUserDao;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.firebase.listeners.DataToMapFieldServiceListener;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import javax.inject.Inject;

/**
 *
 * @author lastuvka
 */
public class FirebaseServer {

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

    public FirebaseServer() {
        System.out.println("Inicializace firebaseserver" + cislo);
        cislo++;

        refRequests.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                FirebaseReq fbReq = snapshot.getValue(FirebaseReq.class);
                refRequests.child(snapshot.getName()).removeValue();
                prepareDataToService(fbReq);
                refRequests.removeEventListener(this);
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
        });
    }

    public void prepareDataToService(FirebaseReq fbReq) {
        DataToMapFieldServiceListener dataToServiceListener = new DataToMapFieldServiceListener();
        dataToServiceListener.setMapFieldService(mapFieldService);
        dataToServiceListener.setFbReq(fbReq);
        userDao.getMoney(fbReq.getUser_id(), dataToServiceListener);
        mapFieldDao.getXY(fbReq.getX(), fbReq.getY(), dataToServiceListener);
        System.out.println("req" + fbReq.toString());
    }
}
