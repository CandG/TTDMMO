package cz.brno.candg.ttdmmo.backend.firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.constants.FbRef;
import javax.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
@ApplicationScoped
public class FbConstants {

    final static Logger log = LoggerFactory.getLogger(FbConstants.class);

    private final ValueEventListener childEventListener;
    private final Firebase constRef = new Firebase(FbRef.refC);
    private int USER_NUM_OFFSET = 0;

    public FbConstants() {
        childEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                log.info("Const USER_NUM_OFFSET was changed. New value is " + snapshot.getValue());
                if (snapshot.getValue() != null && snapshot.hasChildren()) {
                    USER_NUM_OFFSET = snapshot.child("USER_NUM_OFFSET").getValue(Integer.class);
                }
            }

            @Override
            public void onCancelled(FirebaseError fe) {
            }
        };
    }

    public void start() {
        constRef.addValueEventListener(childEventListener);
    }

    public void stop() {
        constRef.removeEventListener(childEventListener);
    }

    public int getUSER_NUM_OFFSET() {
        return USER_NUM_OFFSET;
    }

}
