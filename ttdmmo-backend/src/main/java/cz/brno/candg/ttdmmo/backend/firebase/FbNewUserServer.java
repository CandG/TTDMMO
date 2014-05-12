package cz.brno.candg.ttdmmo.backend.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.dto.NewUserDTO;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import cz.brno.candg.ttdmmo.serviceapi.UserService;
import java.text.Normalizer;
import java.util.Map;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Take newUser requests - then put users position and generate map
 *
 * @author lastuvka
 */
public class FbNewUserServer {

    @Inject
    UserService userService;

    @Inject
    MapFieldService mapFieldService;

    final static Logger log = LoggerFactory.getLogger(FbNewUserServer.class);
    private final ChildEventListener childEventListener;
    private Firebase refRequests = new Firebase(FbRef.refQ + "newUsers");
    private int start;
    private int end;

    public FbNewUserServer() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                log.info("New user: " + snapshot.getValue());
                NewUserDTO authUserDTO = snapshot.getValue(NewUserDTO.class);
                authUserDTO.setCity(Normalizer.normalize(authUserDTO.getCity(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", ""));
                refRequests.child(snapshot.getName()).removeValue();
                if (authUserDTO.getName() != null) {
                    int num = authUserDTO.findUser_num();
                    Map<String, Integer> xy = mapFieldService.findPosition(num);
                    mapFieldService.generateCity(authUserDTO.getCity(), xy.get("x"), xy.get("y"));
                    mapFieldService.generateHousePlaces(xy.get("x"), xy.get("y"));
                    userService.register(authUserDTO, xy.get("x"), xy.get("y"));
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

    public void start(int start, int end) {
        this.start = start;
        this.end = end;
        refRequests.startAt(start).endAt(end).addChildEventListener(childEventListener);
    }

    public void stop() {
        refRequests.startAt(start).endAt(end).removeEventListener(childEventListener);
    }

}
