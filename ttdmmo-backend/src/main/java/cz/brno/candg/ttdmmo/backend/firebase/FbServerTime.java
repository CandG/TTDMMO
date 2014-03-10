/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class FbServerTime {

    final static Logger log = LoggerFactory.getLogger(FbServerTime.class);
    private static int cislo = 0;

    private final ValueEventListener childEventListener;
    private Firebase offsetRef = new Firebase(FbRef.ref + ".info/serverTimeOffset");
    private double serverOffset = 0;

    public FbServerTime() {
        cislo++;
        log.info("Inicializace ServerTime: " + cislo);
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
}
