package com.example.prodigalson7.showme.ShowMeAround.ShowMeAroundServices;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsStatus;
import android.location.Location;
import com.example.prodigalson7.showme.DataBase.DataBaseConnector;
import com.example.prodigalson7.showme.Model.MyLocation;
import com.example.prodigalson7.showme.Model.MyStep;
import com.example.prodigalson7.showme.okhttp.apimodel.Step;
import com.example.prodigalson7.showme.Model.Target;
import com.example.prodigalson7.showme.Model.Util;
import com.example.prodigalson7.showme.R;
import com.example.prodigalson7.showme.ShowMeAround.ShowMeActivityMVP;
import com.example.prodigalson7.showme.okhttp.apimodelplaces.Result;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ProdigaLsON7 on 04/06/2018.
 */

public class ShowMeAroundServices implements IShowMeAround {

    ShowMeActivityMVP.View view;                             //The view from the MVP
    private Disposable subscription = null;                 //subscriber to Observable<Route>

    private Context context;                                           //context of the view in the MVP
    private Marker marker;                                            //a marker from the View for click events
    private GoogleMap mMap;                                     //The google map
    private MyLocation location;                                  //a location clicked on the RecyclerView
    private Polyline mPolyline = null;                          //the route polyline
    private Location locationUpdate;                        //new location from the GPS

    public ShowMeAroundServices(ShowMeActivityMVP.View view) {
        this.view = view;
        this.context = ((Context)((Activity)view));
    }

    @Override
    public Context getViewContext() {
        return context;
    }

    @Override
    public void setLocation(MyLocation location)
    {
        this.location = location;
    }

    @Override
    public MyLocation getLocation()
    {
        return location;
    }

    @Override
    public Marker getMarker() {
        return this.marker;
    }

    @Override
    public void setMarker(final Marker marker) {
        this.marker = marker;
    }

    @Override
    public void setMap(final GoogleMap mMap)
    {
        this.mMap = mMap;
    }

    @Override
    public void focusCameraOnNewLocation()
    {
        //1. set focus on the new destination
        MyLocation location = new MyLocation(marker.getPosition().latitude, marker.getPosition().longitude);
        LatLng destination = new LatLng(location.getLat(), location.getLon());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

        //draw a route between current location and destination
    @Override
    public void drawPolyLine(Observable<Step> mSteps)
    {
        //Draw a PolyLine between 2 destinations

        PolylineOptions po = new PolylineOptions();
        po.clickable(true);

        subscription = mSteps.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Step>() {
                    @Override
                    public void onComplete() {
                        //2. add all the vertices of the route

                        po.width(5);
                        po.color(0xff9C27B0);
                        if (mPolyline != null)                              //remove the previous route polyline
                            mPolyline.remove();
                        mPolyline = mMap.addPolyline(po);
                        mPolyline.setTag("Yay");
                        mPolyline.setJointType(JointType.ROUND);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Step step) { //called each time a new ViewModel is created
                        if (view != null) {
                            MyStep myStep = new MyStep(new LatLng(step.getStartLocation().getLat(),step.getStartLocation().getLng()),
                                                                                    new LatLng(step.getEndLocation().getLat(),step.getEndLocation().getLng()) );
                            po.add(myStep.getStart_point());
                            po.add(myStep.getEnd_point());
                        }
                    }
                });

    }

    //fill the markers in the map
    @Override
    public void fillMap(List<Target> data) {
        double mLat = Util.getInstance().getCurrentLocation().getLat();
        double mLon = Util.getInstance().getCurrentLocation().getLon();
        clearMap();
        //1. draw all the destinations
        addNewMarkers(data);
        // 2. Add a marker in current location and move the camera
        LatLng berlin = new LatLng(mLat, mLon);
        Marker mMarker = mMap.addMarker(new MarkerOptions().position(berlin).title("First Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(berlin));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    //fill the new markers in the map
    @Override
    public void addNewMarkers(List<Target> data) {
        LatLng marker;
        for (Target target : data) {
            if ((target.getmLocation() != null) && (!target.getName().isEmpty())) {
                marker = new LatLng(target.getmLocation().getLat(), target.getmLocation().getLon());
                Bitmap b = scaleDrawing(selectDrawing(target.getSubject()));                            //make marker drawing smaller to fit the screen
                MarkerOptions mo = new MarkerOptions();
                mo.position(marker);
                mo.title(target.getName());
                mo.snippet(target.getSubject()+", Address:  "+target.getAddress());
                mo.icon(BitmapDescriptorFactory.fromBitmap(b));
                mMap.addMarker(mo);
            }
        }
    }

    //clear the googlemap
    @Override
    public void clearMap() {
        mMap.clear();
    }

    @Override
    public void rxUnsubscribe() {
        if (subscription != null) {
            if (!subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }

    @Override
    public void onGpsStatusChanged(int i) {
        switch (i) {
            case GpsStatus.GPS_EVENT_STOPPED:
                Util.getInstance().setGps_alive(false);
                break;
            case GpsStatus.GPS_EVENT_STARTED:
                Util.getInstance().setGps_alive(true);
                break;
        }
    }

    @Override
    public void updateCurrentLocation(){
        MyLocation loc = new MyLocation(locationUpdate.getLatitude(), locationUpdate.getLongitude());
        Util.getInstance().setCurrentLocation(loc);
    }


    public Location getLocationUpdate() {
        return locationUpdate;
    }

    public void setLocationUpdate(Location locationUpdate) {
        this.locationUpdate = locationUpdate;
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Tools>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
  //scale image size
  private Bitmap scaleDrawing(int resource) {
      Bitmap b = BitmapFactory.decodeResource(context.getResources(),   resource);
      Bitmap bhalfsize=Bitmap.createScaledBitmap(b, b.getWidth()/10,b.getHeight()/10, false);
      Bitmap bCroped = Util.getInstance().getCroppedBitmap(bhalfsize, bhalfsize.getHeight());
      return bCroped;
  }

    //select drawing ffor scaling
    private int selectDrawing(Util.Subject subject) {
        int resource;
        switch (subject){
            case Bar:
                resource = R.drawable.bar;
                break;
            case Gym:
                resource = R.drawable.pool;
                break;
            case Restaurant:
                resource = R.drawable.restaurant;
                break;
            case Dancebar:
                resource = R.drawable.nightclub;
                break;
            case Spa:
                resource = R.drawable.spa;
                break;
            case Coffee:
                resource = R.drawable.coffee;
                break;
            default:
                resource = R.drawable.atm;
        }
        return resource;

    }

    @Override
    public void setNewPosition() {
        //1. first remove the marker of the old position
        marker.remove();
        //2. add the new marker
        LatLng berlin = new LatLng(location.getLat(), location.getLon());
        marker  = mMap.addMarker(new MarkerOptions().position(berlin).title("Marker in town"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(berlin));
    }



}
