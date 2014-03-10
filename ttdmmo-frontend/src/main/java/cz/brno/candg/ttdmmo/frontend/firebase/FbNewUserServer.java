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
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.serviceapi.UserService;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class FbNewUserServer {

    @Inject
    UserService userService;

    final static Logger log = LoggerFactory.getLogger(FbNewUserServer.class);
    private final ChildEventListener childEventListener;
    private Firebase refRequests = new Firebase(FbRef.refQ + "newUsers");
    private static int cislo = 0;

    public FbNewUserServer() {
        cislo++;
        log.info("Inicializace FbNewUserServer: " + cislo);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                log.info("User: " + snapshot.getValue());
                AuthUser authUser = snapshot.getValue(AuthUser.class);
                refRequests.child(snapshot.getName()).removeValue();
                log.info("User: " + authUser.getName());
                if (authUser.getName() != null) {
                    userService.setPosition(authUser);
                }
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
        //only users without priority 
        refRequests.addChildEventListener(childEventListener);
    }

    public void stop() {
        //only users without priority 
        refRequests.removeEventListener(childEventListener);
    }
}
