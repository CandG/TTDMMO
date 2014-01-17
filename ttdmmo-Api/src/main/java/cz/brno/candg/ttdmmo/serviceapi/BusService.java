/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.serviceapi;

import cz.brno.candg.ttdmmo.model.Bus;
import cz.brno.candg.ttdmmo.model.MapField;

/**
 *
 * @author lastuvka
 */
public interface BusService {

    public void move(double serverOffset, MapField mapField, String nextMapField_id, Bus bus);
}
