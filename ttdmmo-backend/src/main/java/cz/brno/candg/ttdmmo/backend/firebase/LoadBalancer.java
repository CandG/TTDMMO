package cz.brno.candg.ttdmmo.backend.firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.firebase.FbServerRangeReq;
import cz.brno.candg.ttdmmo.firebase.FbServers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loadbalancer set start-end values for servers
 *
 * @author lastuvka
 */
public class LoadBalancer {

    final static Logger log = LoggerFactory.getLogger(LoadBalancer.class);

    private final ValueEventListener childEventListener;

    private final Firebase refRequests = new Firebase(FbRef.refS + "connections");
    private final Firebase messageRef = new Firebase(FbRef.refS + "messages");
    private int start;
    private int end;

    public LoadBalancer() {
        childEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    FbServers fbServers = snapshot.getValue(FbServers.class);
                    int count = fbServers.getList().size();
                    int length = FbRef.MAX_SERVERS / count;
                    int num = 0;
                    for (String server_id : fbServers.getList().keySet()) {
                        FbServerRangeReq fbServerRangeReq = new FbServerRangeReq();
                        if ((num + 1) == count) {
                            fbServerRangeReq.setEnd(FbRef.MAX_SERVERS);
                        } else {
                            fbServerRangeReq.setEnd((num + 1) * length);
                        }
                        fbServerRangeReq.setStart(1 + num * length);
                        Firebase childRef = messageRef.child(server_id);
                        childRef.setValue(fbServerRangeReq);
                        num++;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError fe) {
            }
        };
    }

    public void start() {
        log.info("start loadBalancer");
        refRequests.addValueEventListener(childEventListener);
    }

    public void stop() {
        log.info("stop loadBalancer");
        refRequests.removeEventListener(childEventListener);
    }
}
