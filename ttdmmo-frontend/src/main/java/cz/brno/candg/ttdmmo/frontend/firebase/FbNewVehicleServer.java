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
import cz.brno.candg.ttdmmo.firebase.NewVehicleReq;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class FbNewVehicleServer {

    final static Logger log = LoggerFactory.getLogger(FbNewVehicleServer.class);
    private final ChildEventListener childEventListener;

    @Inject
    VehicleServiceAdapter vehicleServiceAdapter;

    private Firebase refRequests = new Firebase(FbRef.refQ + "newVehicle");
    private static int cislo = 0;

    public FbNewVehicleServer() {
        cislo++;
        log.info("Inicializace FirebaseNewVehicleServer: " + cislo);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                NewVehicleReq fbReq = snapshot.getValue(NewVehicleReq.class);
                refRequests.child(snapshot.getName()).removeValue();
                vehicleServiceAdapter.newVehicle(fbReq);
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
