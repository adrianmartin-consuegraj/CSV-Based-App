package com.standings.allKarting.models;

import java.util.HashMap;

public class Driver {

    private String name;
    private int points;

    public Driver(){
    }

    public Driver(String name){
        this.name = name;
    }

    public Driver(String name, int points){
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
