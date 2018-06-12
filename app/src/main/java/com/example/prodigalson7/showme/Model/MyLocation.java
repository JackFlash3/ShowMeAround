package com.example.prodigalson7.showme.Model;

/**
 * Created by ProdigaLsON7 on 08/03/2018.
 */

public class MyLocation {
    private double lat;
    private double lon;

    public MyLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }
    public double getLon() {return lon;}
    public void setLon(double lon) {this.lon = lon;}
}
