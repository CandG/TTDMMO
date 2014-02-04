/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.serviceapi;

import cz.brno.candg.ttdmmo.firebase.FBMapReq;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.serviceapi.dto.MapFieldDto;

/**
 *
 * @author lastuvka
 */
public interface MapFieldService {

    void insertField(MapFieldDto mapFieldDto);

    void build(int money, MapField mapField, FBMapReq firebaseReq);

}
