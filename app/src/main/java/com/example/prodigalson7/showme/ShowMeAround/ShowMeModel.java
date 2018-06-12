package com.example.prodigalson7.showme.ShowMeAround;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.prodigalson7.showme.DataBase.DataBaseConnector;
import com.example.prodigalson7.showme.Model.MyLocation;
import com.example.prodigalson7.showme.Model.MyStep;
import com.example.prodigalson7.showme.Model.Target;
import com.example.prodigalson7.showme.Model.Util;
import com.example.prodigalson7.showme.okhttp.RouteApiService;
import com.example.prodigalson7.showme.okhttp.apimodel.Routes;
import com.example.prodigalson7.showme.okhttp.apimodel.Step;
import com.example.prodigalson7.showme.okhttp.apimodelplaces.Result;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;


public class ShowMeModel implements ShowMeActivityMVP.Model {
    private List<Target> dataTmp = new ArrayList<Target>();
    private List<Target> data = new ArrayList<Target>();
    private Repository repository;                               //the injected repository by dagger
    private RouteApiService routeApiService;          //the injected routes service

    public ShowMeModel(Repository repository, RouteApiService routeApiService) {
        this.repository = repository;
        this.routeApiService = routeApiService;
    }

    //clear the DB
    @Override
    public void clearDB(DataBaseConnector contactsDB, DisposableObserver<String> clearDBSubscriber) {
        repository.clearDB(contactsDB, clearDBSubscriber);
    }

    //search locations of subject.
    // Input - Util.Subject subject
    @Override
    public void searchBySubject(DataBaseConnector contactsDB, Util.Subject subject, Context context){
        repository.getPlacesFromNetwork(contactsDB, subject, context);
    }

    //load the places data from the SQLite DB
    @Override
    public void loadPlacesFromDB(DataBaseConnector contactsDB, DisposableObserver<String> loadFromDBSubscriber) {
        repository.loadPlacesFromDB(contactsDB, this.dataTmp, loadFromDBSubscriber);
    }

    @Override
    public List<Target> loadImages() {
        repository.loadImagesFromNetwork(this.dataTmp);
        removeRecordsFromList();
        while (dataTmp.size() > 0) {
            Target target = dataTmp.remove(0);
            data.add(target);
        }
        return data;
    }


        @Override
        public Observable<Step> getRoutesFromNetwork (@NonNull Marker marker){
            //1. extract target location from Marker
            MyLocation location = new MyLocation(marker.getPosition().latitude, marker.getPosition().longitude);
            LatLng origin = new LatLng(Util.getInstance().getCurrentLocation().getLat(), Util.getInstance().getCurrentLocation().getLon());                                    //origin Marker
            LatLng destination = new LatLng(location.getLat(), location.getLon());                         //destination Marker
            String Origin = origin.latitude + "," + origin.longitude;
            String Destination = destination.latitude + "," + destination.longitude;
            //2.
            Observable<Routes> routesObservable = routeApiService.getRoute(Origin, Destination);

            return routesObservable.flatMap(new Function<Routes, Observable<Step>>() {
                @Override
                public Observable<Step> apply(Routes routes) {
                    List<Step> steps = routes.getRoutes().get(0).getLegs().get(0).steps;
                    return Observable.fromIterable(steps);

                    //return Observable.fromIterable(routes.getRoutes().get(0).getLegs().get(0).steps);
                }
            });
        }

        @Override
        public Observable<Step> getRoutesFromNetwork2 ( final MyLocation location){
            //1. extract target location from Marker
            LatLng origin = new LatLng(Util.getInstance().getCurrentLocation().getLat(), Util.getInstance().getCurrentLocation().getLon());                                    //origin Marker
            LatLng destination = new LatLng(location.getLat(), location.getLon());                         //destination Marker
            String Origin = origin.latitude + "," + origin.longitude;
            String Destination = destination.latitude + "," + destination.longitude;
            //2.
            Observable<Routes> routesObservable = routeApiService.getRoute(Origin, Destination);

            return routesObservable.flatMap(new Function<Routes, Observable<Step>>() {
                @Override
                public Observable<Step> apply(Routes routes) {
                    List<Step> steps = routes.getRoutes().get(0).getLegs().get(0).steps;
                    return Observable.fromIterable(steps);

                    //return Observable.fromIterable(routes.getRoutes().get(0).getLegs().get(0).steps);
                }
            });
        }


//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Tools>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
        //clear the data List
        private void removeRecordsFromList() {
            Target target;

            int listSize = this.data.size();
            for (int i = 0; i < listSize; i++) {
                target = this.data.remove(0);
            }
        }



//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<END TOOLS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//

}
