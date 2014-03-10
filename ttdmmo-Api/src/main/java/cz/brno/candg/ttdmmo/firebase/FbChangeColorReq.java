/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.firebase;

/**
 *
 * @author lastuvka
 */
public class FbChangeColorReq {

    private String user_id;
    private String color;

    public String getUser_id() {
        return user_id;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "FbChangeColorReq{" + "user_id=" + user_id + ", color=" + color + '}';
    }

}
