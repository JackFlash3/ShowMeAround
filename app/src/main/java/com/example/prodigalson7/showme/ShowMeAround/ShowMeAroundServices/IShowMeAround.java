package com.example.prodigalson7.showme.ShowMeAround.ShowMeAroundServices;

import android.content.Context;

import com.example.prodigalson7.showme.Model.MyLocation;
import com.example.prodigalson7.showme.Model.MyStep;
import com.example.prodigalson7.showme.Model.Target;
import com.example.prodigalson7.showme.okhttp.apimodel.Step;
import com.example.prodigalson7.showme.okhttp.apimodelplaces.Result;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ProdigaLsON7 on 04/06/2018.
 */

public interface IShowMeAround {

    public Context getViewContext();                                     //get the context of the View in MVP

    public Marker getMarker();                                               //get a Marker

    public void setMarker(final Marker marker);               //set a Marker

    public void setLocation(MyLocation location);             //set a target for drawLine

    public MyLocation getLocation();                                      //set a target for drawLine

    public void focusCameraOnNewLocation();                  //set camera on the target

    public void setMap(final GoogleMap mMap);              //sertting the google map

    public void drawPolyLine(Observable<Step> mSteps);    //draw a polyline for the route from current location to the destination

    public void clearMap();                 //clear the googlemap

    public void fillMap(List<Target> data);                      //fill the map with

    public void addNewMarkers(List<Target> data);   //add new markers

    public void setNewPosition();       //set a new position on the map due to roaming - GPS

    public void rxUnsubscribe();        //Unsubscribe when finishing

    public void onGpsStatusChanged(int i);      //apply when GPS cahnges status of connectivity

    public void updateCurrentLocation();           //current location update
}
