/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.dao;

import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;
import cz.brno.candg.ttdmmo.model.MapField;

/**
 *
 * @author Zdenek Lastuvka
 */
public interface MapFieldDao extends Dao<MapField> {

    void getXY(int x, int y, ValueEventListenerWithType valueEventListener);
}
