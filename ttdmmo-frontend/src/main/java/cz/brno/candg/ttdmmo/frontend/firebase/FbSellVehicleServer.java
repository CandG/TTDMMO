/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.frontend.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.backend.adapter.VehicleServiceAdapter;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.firebase.FbSellVehicleReq;
import cz.brno.candg.ttdmmo.firebase.PathReq;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class FbSellVehicleServer {

    final static Logger log = LoggerFactory.getLogger(FbSellVehicleServer.class);

    private ChildEventListener childEventListener;

    @Inject
    VehicleServiceAdapter vehicleServiceAdapter;

    private Firebase refRequests = new Firebase(FbRef.refQ + "sellVehicle");
    private static int cislo = 0;

    public FbSellVehicleServer() {
        cislo++;
        log.info("Inicializace FbSellVehicleServer: " + cislo);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                FbSellVehicleReq fbReq = snapshot.getValue(FbSellVehicleReq.class);
                refRequests.child(snapshot.getName()).removeValue();
                /*maybe stop bus first*/
                vehicleServiceAdapter.sellVehicle(fbReq);
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
