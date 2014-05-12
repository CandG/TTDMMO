package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import cz.brno.candg.ttdmmo.dto.ChangeColorDTO;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.serviceapi.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data to user service for method changeColor
 *
 * @author lastuvka
 */
public class DataToChangeColorServiceListener implements ValueEventListener {

    final static Logger log = LoggerFactory.getLogger(DataToChangeColorServiceListener.class);

    private ChangeColorDTO fbReq;
    private AuthUser authUser = null;
    private UserService userService;

    public ChangeColorDTO getFbReq() {
        return fbReq;
    }

    public void setFbReq(ChangeColorDTO fbReq) {
        this.fbReq = fbReq;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onDataChange(DataSnapshot ds) {
        log.info("Additional data recived: user - " + ds.getValue());
        setAuthUser(ds.getValue(AuthUser.class));
        userService.changeColor(authUser, fbReq.getColor());
    }

    @Override
    public void onCancelled(FirebaseError fe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
