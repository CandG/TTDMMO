/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.firebase;

import cz.brno.candg.ttdmmo.model.MapField;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lastuvka
 */
public class PathReq {
//private Map<String, Boolean> stops = new HashMap<String, Boolean>();

    private List<String> stops = new ArrayList<String>();
    private String bus_id;

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }

    public List<String> getStops() {
        return stops;
    }

    @Override
    public String toString() {
        return "PathReq{" + "stops=" + stops + ", bus_id=" + bus_id + '}';
    }

}
