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
 * Management of Firebase server time
 *
 * @author lastuvka
 */
@ApplicationScoped
public class FbServerTime {

    final static Logger log = LoggerFactory.getLogger(FbServerTime.class);

    private final ValueEventListener childEventListener;
    private final Firebase offsetRef = new Firebase(FbRef.ref + ".info/serverTimeOffset");
    private double serverOffset = 0;

    public FbServerTime() {
        childEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                double offset = snapshot.getValue(Double.class);
                serverOffset = offset;
            }

            @Override
            public void onCancelled(FirebaseError fe) {
            }
        };
    }

    public void start() {
        offsetRef.addValueEventListener(childEventListener);
    }

    public void stop() {
        offsetRef.removeEventListener(childEventListener);
    }

    public double getServerOffset() {
        return serverOffset;
    }

    public double getServerTime() {
        double estimatedServerTimeMs = System.currentTimeMillis() + serverOffset;
        return estimatedServerTimeMs;
    }

    public double getFutureTime() {
        double estimatedServerTimeMs = System.currentTimeMillis() + serverOffset + 1000 * 60 * 60 * 24 * 365 * 10;
        return estimatedServerTimeMs;
    }
}
