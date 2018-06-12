package com.example.prodigalson7.showme.Model;

import android.graphics.Bitmap;

/**
 * Created by ProdigaLsON7 on 07/03/2018.
 */

public class Target {
    private int id;
    private String name;
    private double rating;
    private Util.Subject subject;
    private String address;
    private String photo_ref;
    private MyLocation mLocation;
    private String placeID;
    private Bitmap photo;

    public Target(int id, String name, double rating, Util.Subject subject, String address, String photo_ref, String placeID,  MyLocation location) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.subject = subject;
        this.address = address;
        this.mLocation = location;
        this.photo_ref = photo_ref;
        this.placeID = placeID;
        this.photo = null;
    }

    public int getId() {  return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getRating() { return rating;}
    public void setRating(double rating) {this.rating = rating;}
    public Util.Subject getSubject() {return subject;}
    public void setSubject(Util.Subject subject) {this.subject = subject;}
    public String getAddress() {return address;}
    public void setAddress(String address) { this.address = address;}
    public MyLocation getmLocation() { return mLocation;}
    public void setmLocation(MyLocation mLocation) { this.mLocation = mLocation;}
    public String getPhoto_ref() {return photo_ref;}
    public void setPhoto_ref(String photo_ref) {this.photo_ref = photo_ref;}
    public String getPlaceID() {return placeID;}
    public void setPlaceID(String placeID) {this.placeID = placeID;}
    public Bitmap getPhoto() {return photo;}
    public void setPhoto(Bitmap photo) {this.photo = photo;}
}
