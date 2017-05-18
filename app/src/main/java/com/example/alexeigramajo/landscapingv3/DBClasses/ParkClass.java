package com.example.alexeigramajo.landscapingv3.DBClasses;

/**
 * Created by alexeigramajo on 5/7/2017.
 */

public class ParkClass {
    String id;
    String park;

    public ParkClass(){
    }
    public ParkClass(String id, String park){
        this.id = id;
        this.park = park;
    }
    public String getId(){
        return id;
    }
    public String getPark(){
        return park;
    }
}
