/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.backend.service.impl;

import com.firebase.client.Firebase;
import cz.brno.candg.ttdmmo.backend.dao.MapFieldDao;
import cz.brno.candg.ttdmmo.backend.firebase.FirebaseReq;
import cz.brno.candg.ttdmmo.model.MapField;
import cz.brno.candg.ttdmmo.serviceapi.MapFieldService;
import cz.brno.candg.ttdmmo.serviceapi.dto.AuthUserDto;
import cz.brno.candg.ttdmmo.serviceapi.dto.MapFieldDto;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

/**
 *
 * @author lastuvka
 */
public class MapFieldServiceImpl implements MapFieldService {

    @Inject
    private MapFieldDao mapFieldDao;

    private static int cislo = 0;

    public MapFieldServiceImpl() {
        System.out.println("Inicializace user service" + cislo);
        cislo++;
    }

    @Override
    public void insertField(MapFieldDto mapFieldDto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void build(int money, MapField mapField, MapFieldDto mapFieldDto) {
        mapFieldDao.create(mapField);
    }

}
