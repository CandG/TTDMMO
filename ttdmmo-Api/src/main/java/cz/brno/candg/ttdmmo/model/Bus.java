/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author lastuvka
 */
public class Bus {

    private int x;
    private int y;
    private int direction;
    private int cargo;
    private String user_id;
    private String bus_id;
    private String name;
    private String path_id;
    private String nextField;
    private int path_position;
    private int position;
    private double startTime;
    private List<Long> bus_stops = new ArrayList<Long>();

    public Bus() {
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNextField() {
        return nextField;
    }

    public void setNextField(String nextField) {
        this.nextField = nextField;
    }

    public String getPath_id() {
        return path_id;
    }

    public void setPath_id(String path_id) {
        this.path_id = path_id;
    }

    public int getPath_position() {
        return path_position;
    }

    public void setPath_position(int path_position) {
        this.path_position = path_position;
    }

    public int getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public List<Long> getBus_stops() {
        return bus_stops;
    }

    public void setBus_stops(List<Long> bus_stops) {
        this.bus_stops = bus_stops;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.bus_id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bus other = (Bus) obj;
        if (!Objects.equals(this.bus_id, other.bus_id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Bus{" + "x=" + x + ", y=" + y + ", direction=" + direction + ", user_id=" + user_id + ", bus_id=" + bus_id + ", name=" + name + ", path_id=" + path_id + ", nextField=" + nextField + ", path_position=" + path_position + ", position=" + position + ", startTime=" + startTime + ", bus_stops=" + bus_stops + '}';
    }

}
