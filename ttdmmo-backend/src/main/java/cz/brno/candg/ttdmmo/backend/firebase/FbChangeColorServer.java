package cz.brno.candg.ttdmmo.backend.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.backend.adapter.AuthUserServiceAdapterImpl;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.dto.ChangeColorDTO;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
public class FbChangeColorServer {

    @Inject
    AuthUserServiceAdapterImpl authUserServiceAdapter;

    final static Logger log = LoggerFactory.getLogger(FbChangeColorServer.class);
    private final ChildEventListener childEventListener;
    private Firebase refRequests = new Firebase(FbRef.refQ + "changeColor");
    private int start;
    private int end;

    public FbChangeColorServer() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                ChangeColorDTO fbReq = snapshot.getValue(ChangeColorDTO.class);
                refRequests.child(snapshot.getName()).removeValue();
                authUserServiceAdapter.changeColor(fbReq);
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
