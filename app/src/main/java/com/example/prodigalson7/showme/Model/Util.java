package com.example.prodigalson7.showme.Model;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import static com.example.prodigalson7.showme.Model.Util.Subject.Bar;

/**
 * Created by ProdigaLsON7 on 07/03/2018.
 */

public class Util {

    //Constants
    public static int MAX_DISTANCE = 1000;                    //distance before updating the map solutions
    public static int MAX_CENTER_LOC = 50;                   //distance before updating the map center location

    private static Util util = null;                                        //the singleton object
    public enum Subject{                                                    //list of possible subjects
        Bar, Gym, Restaurant, Dancebar, Spa, Coffee, Atm
    }
    private boolean gps_alive = false;                           //gps status flag
    private MyLocation currentLocation = null;         //the current location
    private SearchOptions searchOptions = Util.SearchOptions.getInstance();
    private double searchRadius = 2000;                     //searching radius in meters
    private boolean concurrencyFlag = false;            //true while AsyncTask works
    private String travel_mode = "transit";                 //default travel mode
    private String avoid_roads = "f";                           //roads to be avoided by default


    private  Util() { }

    public synchronized static Util getInstance() {
        if (util == null)
            util = new Util();
        return util;
    }

    public SearchOptions getSearchOptions() { return searchOptions;}
    public boolean isGps_alive() { return gps_alive; }
    public void setGps_alive(boolean gps_alive) { this.gps_alive = gps_alive;}
    public MyLocation getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(MyLocation currentLocation) { this.currentLocation = currentLocation;}
    public double getSearchRadius() {return searchRadius;}
    public void setSearchRadius(double searchRadius) {this.searchRadius = searchRadius;}
    public boolean isConcurrencyFlag() {return concurrencyFlag;}
    public void setConcurrencyFlag(boolean concurrencyFlag) {this.concurrencyFlag = concurrencyFlag;}
    public String getTravel_mode() {return travel_mode;}
    public void setTravel_mode(String travel_mode) {this.travel_mode = travel_mode;}
    public String getAvoid_roads() {return avoid_roads;}
    public void setAvoid_roads(String avoid_roads) {this.avoid_roads = avoid_roads;}

    public static class SearchOptions {
        private static SearchOptions searchOptions = null;                                        //the singleton object
        private boolean bar;
        private boolean gym;
        private boolean restaurant;
        private boolean dancebar;
        private boolean spa;
        private boolean coffee;
        private boolean atm;


        private  SearchOptions() {
            this.bar = false;
            this.gym = false;
            this.restaurant = false;
            this.dancebar = false;
            this.spa = false;
            this.coffee = false;
            this.atm = false;
        }

        public synchronized static SearchOptions getInstance() {
            if (searchOptions == null)
                searchOptions = new SearchOptions();
            return searchOptions;
        }

        public boolean isBar() { return bar; }
        public void setBar(boolean bar) {this.bar = bar;}
        public boolean isPool() {return gym;}
        public void setPool(boolean gym) {this.gym = gym;}
        public boolean isRestaurant() {return restaurant;}
        public void setRestaurant(boolean restaurant) {this.restaurant = restaurant;}
        public boolean isDancebar() {return dancebar;}
        public void setDancebar(boolean dancebar) {this.dancebar = dancebar;}
        public boolean isSpa() {return spa;}
        public void setSpa(boolean spa) { this.spa = spa;}
        public boolean isCoffee() { return coffee;}
        public void setCoffee(boolean coffee) { this.coffee = coffee;}
        public boolean isAtm() {return atm;}
        public void setAtm(boolean atm) {this.atm = atm;}
    }

//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Tools>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
//Haversine distance
public  double calculateDistance(MyLocation first, MyLocation second)
{
    //Converting {latitude,longitude} to distance according to the
    // Haversine equation
    final double R =  6372795;
    double gama1 = first.getLat()*Math.PI/180;
    double gama2 = second.getLat()*Math.PI/180;
    double delta1 = first.getLon()*Math.PI/180;
    double delta2 = second.getLon()*Math.PI/180;
    double subGama = (gama2 - gama1)/2;
    double subDelta = (delta2 - delta1)/2;

    double temp1 = Math.pow(Math.sin(subGama),2) +
            Math.cos(gama1)*Math.cos(gama2)*Math.pow(Math.sin(subDelta),2);
    double temp2 = Math.sqrt(temp1);
    double temp3 = Math.sqrt(1-temp1);
    double distance = 2*R*Math.atan2(temp2,temp3);
    distance = (double)Math.round(distance*1000)/1000;
    return distance;
}

//croping a bitmap image
    public  Bitmap getCroppedBitmap(Bitmap bmp, int radius)
    {
        Bitmap sbmp;
        if(bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);			//enable cropping
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,
                sbmp.getWidth() / 2+0.1f, paint);		//Cropping the bitmap to a circle
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);			//Drawing the bitmap to the View canvas

        return output;
    }

}
