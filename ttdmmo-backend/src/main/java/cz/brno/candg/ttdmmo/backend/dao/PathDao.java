/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.dao;

import cz.brno.candg.ttdmmo.backend.firebase.listeners.ValueEventListenerWithType;
import cz.brno.candg.ttdmmo.model.Path;

/**
 *
 * @author lastuvka
 */
public interface PathDao extends Dao<Path> {

    void getFromPath(String path_id, String path_position, ValueEventListenerWithType valueEventListener);

}
