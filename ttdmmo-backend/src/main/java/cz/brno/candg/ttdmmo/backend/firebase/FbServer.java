package cz.brno.candg.ttdmmo.backend.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.firebase.FbServerRangeReq;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Firebase server for Java - checking Loadbalancer state start/stop queue
 * servers
 *
 * @author lastuvka
 */
public class FbServer {

    final static Logger log = LoggerFactory.getLogger(FbServer.class);

    @Inject
    FbServerTime serverTime;

    @Inject
    FbConstants fbConstants;

    @Inject
    FbSellVehicleServer sellVehicle;

    @Inject
    FbMapServer fbMap;

    @Inject
    FbNewVehicleServer newVehicle;

    @Inject
    FbVehiclePathServer pathVehicle;

    @Inject
    FbVehicleServer vehicleServer;

    @Inject
    FbNewUserServer fbNewUserServer;

    @Inject
    FbChangeColorServer fbChangeColorServer;

    @Inject
    LoadBalancer loadBalancer;

    Firebase dataRef = new Firebase(FbRef.refQ);

    final Firebase myConnectionsRef = new Firebase(FbRef.refS + "connections/list");

    final Firebase messageRef = new Firebase(FbRef.refS + "/messages");
// stores the timestamp of my last disconnect (the last time I was seen online)
    final Firebase lastOnlineRef = new Firebase(FbRef.refS + "lastOnline");

    final Firebase connectedRef = new Firebase(FbRef.ref + ".info/connected");

    private final ValueEventListener childEventListener;
    private final ValueEventListener messageEventListener;
    private final ChildEventListener checkEventListener;

    String server_id = null;

    public FbServer() {
        dataRef.auth("JYO4zRqCrtQLkBvuKf2F7r1KpQT8ZfmLt2df0DUv", new Firebase.AuthListener() {

            @Override
            public void onAuthError(FirebaseError error) {
                log.error("Login Failed! " + error.getMessage());
            }

            @Override
            public void onAuthSuccess(Object authData) {
                log.info("Login Succeeded!");
            }

            @Override
            public void onAuthRevoked(FirebaseError fe) {
                log.error("Authenticcation status was cancelled! " + fe.getMessage());
            }

        });

        checkEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                log.info("loadbalancer check");
                if (snapshot.getName().equals(server_id)) {
                    log.info("My loadbalancer is running, server id: " + snapshot.getName());
                    loadBalancer.start();
                } else {
                    loadBalancer.stop();
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

        messageEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    log.info("range for this server is: " + snapshot.getValue());
                    FbServerRangeReq fbServerRangeReq = snapshot.getValue(FbServerRangeReq.class);

                    newVehicle.stop();
                    pathVehicle.stop();
                    vehicleServer.stop();
                    sellVehicle.stop();
                    fbNewUserServer.stop();
                    fbChangeColorServer.stop();
                    fbMap.stop();
                    newVehicle.start(fbServerRangeReq.getStart(), fbServerRangeReq.getEnd());
                    pathVehicle.start(fbServerRangeReq.getStart(), fbServerRangeReq.getEnd());
                    vehicleServer.start(fbServerRangeReq.getStart(), fbServerRangeReq.getEnd());
                    sellVehicle.start(fbServerRangeReq.getStart(), fbServerRangeReq.getEnd());
                    fbNewUserServer.start(fbServerRangeReq.getStart(), fbServerRangeReq.getEnd());
                    fbChangeColorServer.start(fbServerRangeReq.getStart(), fbServerRangeReq.getEnd());
                    fbMap.start(fbServerRangeReq.getStart(), fbServerRangeReq.getEnd());
                }
            }

            @Override
            public void onCancelled(FirebaseError fe) {

            }
        };

        childEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    // add this device to my connections list
                    // this value could contain info about the device or a timestamp too
                    Firebase con = myConnectionsRef.push();
                    con.setValue(Boolean.TRUE);
                    server_id = con.getName();

                    myConnectionsRef.startAt().limit(1).addChildEventListener(checkEventListener);

                    messageRef.child(server_id).addValueEventListener(messageEventListener);

                    // when this device disconnects, remove it
                    con.onDisconnect().removeValue();
                    messageRef.child(server_id).onDisconnect().removeValue();
                    // when I disconnect, update the last time I was seen online
                    lastOnlineRef.child(server_id).onDisconnect().setValue(ServerValue.TIMESTAMP);
                }
            }

            @Override
            public void onCancelled(FirebaseError fe) {
                log.error("Listener was cancelled at .info/connected");
            }
        };

    }

    public void start() {
        log.info("start FbServer");
        Firebase.goOnline();
        serverTime.start();
        fbConstants.start();
        connectedRef.addValueEventListener(childEventListener);
    }

    public void stop() {
        log.info("stop FbServer");
        Firebase.goOffline();
        serverTime.stop();
        fbConstants.stop();
        loadBalancer.stop();
        myConnectionsRef.startAt().limit(1).removeEventListener(checkEventListener);
        newVehicle.stop();
        pathVehicle.stop();
        vehicleServer.stop();
        sellVehicle.stop();
        fbNewUserServer.stop();
        fbChangeColorServer.stop();
        fbMap.stop();
        if (server_id != null) {
            messageRef.child(server_id).removeEventListener(messageEventListener);
        }
        connectedRef.removeEventListener(childEventListener);
    }
}
