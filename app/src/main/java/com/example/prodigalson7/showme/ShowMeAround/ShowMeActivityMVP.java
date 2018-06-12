package com.example.prodigalson7.showme.ShowMeAround;

import android.content.Context;
import android.view.KeyEvent;

import com.example.prodigalson7.showme.DataBase.DataBaseConnector;
import com.example.prodigalson7.showme.Model.MyLocation;
import com.example.prodigalson7.showme.Model.Target;
import com.example.prodigalson7.showme.Model.Util;
import com.example.prodigalson7.showme.ShowMeAround.ShowMeAroundServices.ShowMeAroundServices;
import com.example.prodigalson7.showme.okhttp.apimodel.Step;
import com.example.prodigalson7.showme.okhttp.apimodelplaces.Result;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public interface ShowMeActivityMVP {

    interface View{


    }

    interface Presenter {

        public void setView(View view);   //set the View of the MVP model

        public void setServices(ShowMeAroundServices mServices); //set a Services object

        public void clearDB();     //clear the DB

        public void searchPlaces() ;   //search for places accoridng to subjects

        public void loadPlacesFromDB();  //load the places data from the SQLite DB

        public void loadImages() ;      //download the targets images

        public List<Target> getData();      //get the data

        public void onKeyDown(int keyCode, KeyEvent event);      //callback of keydown press

        public void onMarkerClick();          // Called when the user clicks a marker

        public void onLocationClicked();       //apply when a line in the recycler view is clicked

        public void fillMap();          //fill markers in the map

        public void setNewPosition();       //set new position in the Map due to roaming - GPS

        public void rxUnsubscribe();        //unsubscribe the Observable subscription

        public void onGpsStatusChanged(int i);    //apply when GPS cahnges status of connectivity

        public boolean onLocationChanged();     //location change by GPS
    }

    interface Model {

        public void clearDB(DataBaseConnector contactsDB, DisposableObserver<String> clearDBSubscriber);     //clear the DB

        public void searchBySubject(DataBaseConnector contactsDB, Util.Subject subject, Context context) ;   //search for places accoridng to subjects

        public void loadPlacesFromDB(DataBaseConnector contactsDB, DisposableObserver<String> loadFromDBSubscriber);  //load the places data from the SQLite DB

        public List<Target> loadImages() ;      //download the targets images

        public Observable<Step> getRoutesFromNetwork(final Marker marker);     //Load the route from target to destination for a given destination as Marker

        public Observable<Step> getRoutesFromNetwork2(final MyLocation location);       // //Load the route from target to destination for a given destination as location

        public void rxUnsubscribe();                        //unsubscribe places subscription
        //  void createUser(String name, String lastName);

   //     User getUser();


    }
}
