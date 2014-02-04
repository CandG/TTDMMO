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
public class NewVehicleReq {

    private String user_id;
    private String name;
    private int x;
    private int y;

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "NewVehicleReq{" + "user_id=" + user_id + ", name=" + name + ", x=" + x + ", y=" + y + '}';
    }

}
