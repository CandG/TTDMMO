/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.dao;

import cz.brno.candg.ttdmmo.model.Bus;

/**
 *
 * @author lastuvka
 */
public interface VehicleDao extends Dao<Bus> {

    void move(int x, int y, String bus_id);

    void setArriveTime(double estimatedArriveTimeMs, String bus_id);

    void changeColor(String id, String color);
}
