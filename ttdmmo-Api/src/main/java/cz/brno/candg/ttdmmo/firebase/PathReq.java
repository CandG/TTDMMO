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
public class PathReq {
//private Map<String, Boolean> stops = new HashMap<String, Boolean>();

    private String stops;
    private String bus_id;

    public PathReq() {
    }

    public String getStops() {
        return stops;
    }

    public void setStops(String stops) {
        this.stops = stops;
    }

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }

    @Override
    public String toString() {
        return "PathReq{" + "stops=" + stops + ", bus_id=" + bus_id + '}';
    }

}
