/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.firebase.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.firebase.FbChangeColorReq;
import cz.brno.candg.ttdmmo.model.AuthUser;
import cz.brno.candg.ttdmmo.serviceapi.UserService;

/**
 *
 * @author lastuvka
 */
public class DataToChangeColorServiceListener extends ValueEventListenerWithType {

    private FbChangeColorReq fbReq;
    private AuthUser authUser = null;
    private UserService userService;

    public FbChangeColorReq getFbReq() {
        return fbReq;
    }

    public void setFbReq(FbChangeColorReq fbReq) {
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
        setAuthUser(ds.getValue(AuthUser.class));
        userService.changeColor(authUser, fbReq.getColor());
    }

    @Override
    public void onCancelled(FirebaseError fe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
