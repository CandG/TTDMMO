/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.model;

/**
 *
 * @author lastuvka
 */
public class Message {

    String text;
    String user_id;
    String xy;

    public String getText() {
        return text;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getXy() {
        return xy;
    }

    @Override
    public String toString() {
        return "Message{" + "text=" + text + ", user_id=" + user_id + ", xy=" + xy + '}';
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setXy(String xy) {
        this.xy = xy;
    }

}
