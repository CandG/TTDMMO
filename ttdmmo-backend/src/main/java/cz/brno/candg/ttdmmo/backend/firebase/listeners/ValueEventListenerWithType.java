package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.ValueEventListener;

/**
 *
 * @author lastuvka
 */
public abstract class ValueEventListenerWithType implements ValueEventListener {

    public String type = null;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
