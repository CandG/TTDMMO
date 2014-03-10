/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lastuvka
 */
public class Path {

    private List<String> path = new ArrayList<String>();

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Path{" + "path=" + path + '}';
    }

}
