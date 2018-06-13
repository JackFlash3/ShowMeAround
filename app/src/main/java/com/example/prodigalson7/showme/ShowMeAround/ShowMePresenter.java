package com.example.prodigalson7.showme.ShowMeAround;

import com.example.prodigalson7.showme.DataBase.DataBaseConnector;
import com.example.prodigalson7.showme.Model.MyLocation;
import com.example.prodigalson7.showme.okhttp.apimodel.Step;
import com.example.prodigalson7.showme.Model.Target;
import com.example.prodigalson7.showme.Model.Util;
import com.example.prodigalson7.showme.ShowMeAround.ShowMeAroundServices.ShowMeAroundServices;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import static com.example.prodigalson7.showme.Model.Util.Subject.Atm;
import static com.example.prodigalson7.showme.Model.Util.Subject.Bar;
import static com.example.prodigalson7.showme.Model.Util.Subject.Coffee;
import static com.example.prodigalson7.showme.Model.Util.Subject.Dancebar;
import static com.example.prodigalson7.showme.Model.Util.Subject.Gym;
import static com.example.prodigalson7.showme.Model.Util.Subject.Restaurant;
import static com.example.prodigalson7.showme.Model.Util.Subject.Spa;

public class ShowMePresenter implements ShowMeActivityMVP.Presenter {

    private ShowMeActivityMVP.View view;    //will be null until we inject the Activity, dagger creates only the Repository, Presenter and the Model
    private DisposableObserver<String> clearDBSubscriber = null;                   //subscribing to clearing DB data Observable
    private DisposableObserver<String> loadFromDBSubscriber = null;         //subscribing to loading data from db Observable
    private ShowMeActivityMVP.Model model;                                                  //dagger injection of the Model Layer
    private ShowMeAroundServices mServices;                                                 //aid class  for boilerplate code

    //Variables
    private DataBaseConnector contactsDB;
    private List<Target> data = null;

    public ShowMePresenter(ShowMeActivityMVP.Model model) {

        this.model = model;
    }

    /*If the subscription isn't null but it is still subscribed, then unsubscribe the subscription
    this method  will be called when the view is destroyed in TopMoviesActivity
    this will make sure that the subscription doesn't work in the background and cause memory leaks
    after the view is destroyed
*/


    @Override
    public void setView(ShowMeActivityMVP.View view)
    {
          this.view = view;

    }

    @Override
    public void setServices(ShowMeAroundServices mServices)
    {
        this.mServices = mServices;
        this.contactsDB = new DataBaseConnector(mServices.getViewContext());
    }

    @Override
    //clear the DB
    public void clearDB() {
        model.clearDB(contactsDB, clearDBSubscriber);
    }

    //search for places accoridng to subjects
    @Override
    public void searchPlaces() {
        Util.SearchOptions searchOptions = Util.getInstance().getSearchOptions();

        //search for coffee shops
        if (searchOptions.isCoffee()) {
            Util.getInstance().setConcurrencyFlag(true);
            model.searchBySubject(contactsDB, Coffee, mServices.getViewContext());
            while (Util.getInstance().isConcurrencyFlag()){}
        }

        //search for bar
        if (searchOptions.isBar()) {
            Util.getInstance().setConcurrencyFlag(true);
            model.searchBySubject(contactsDB, Bar, mServices.getViewContext());
            while (Util.getInstance().isConcurrencyFlag()){}
        }

        //search for gym
        if (searchOptions.isPool()) {
             Util.getInstance().setConcurrencyFlag(true);
             model.searchBySubject(contactsDB, Gym, mServices.getViewContext());
             while (Util.getInstance().isConcurrencyFlag()){}
        }
        //search for restaurant
        if (searchOptions.isRestaurant()) {
             Util.getInstance().setConcurrencyFlag(true);
             model.searchBySubject(contactsDB, Restaurant, mServices.getViewContext());
             while (Util.getInstance().isConcurrencyFlag()){}
        }
        //search for night clubs
        if (searchOptions.isDancebar()) {
            Util.getInstance().setConcurrencyFlag(true);
            model.searchBySubject(contactsDB, Dancebar, mServices.getViewContext());
            while (Util.getInstance().isConcurrencyFlag()){}
        }
        //search for spas
        if (searchOptions.isSpa()) {
            Util.getInstance().setConcurrencyFlag(true);
            model.searchBySubject(contactsDB, Spa, mServices.getViewContext());
            while (Util.getInstance().isConcurrencyFlag()){}
        }
        //search for Atm
        if (searchOptions.isAtm()) {
             Util.getInstance().setConcurrencyFlag(true);
             model.searchBySubject(contactsDB, Atm, mServices.getViewContext());
             while (Util.getInstance().isConcurrencyFlag()){}
        }
    }

    //load the places data from the SQLite DB
    @Override
    public void loadPlacesFromDB() {
       model.loadPlacesFromDB(contactsDB, loadFromDBSubscriber);
    }

    //download the targets images
    @Override
    public void loadImages() {
        data = model.loadImages();
    }

    @Override
    public void rxUnsubscribe() {
        //1. unsubscribe routes subscriber
        mServices.rxUnsubscribe();
        //2. unsubscribe clear DB subscriber
        if (clearDBSubscriber != null) {
            if (!clearDBSubscriber.isDisposed()) {
                clearDBSubscriber.dispose();
            }
        }
        //3. unsubscribe clear DB subscriber
        if (loadFromDBSubscriber != null) {
            if (!loadFromDBSubscriber.isDisposed()) {
                loadFromDBSubscriber.dispose();
            }
        }
        //4. unsubscribe places subscriber
        model.rxUnsubscribe();
    }


    //get the route between the current location and the target
    @Override
    public void onMarkerClick(){
        //1. fetch the route
        Observable<Step> mSteps = model.getRoutesFromNetwork(mServices.getMarker());
        //2. draw the route to the destination
        mServices.drawPolyLine(mSteps);
        //3. focus camera on the target
        mServices.focusCameraOnNewLocation();
    }

    //apply when a line in the recycler view is clicked
    @Override
    public void onLocationClicked(){
        //1. fetch the route
        Observable<Step> mSteps = model.getRoutesFromNetwork2(mServices.getLocation());
        //2. draw the route to the destination
        mServices.drawPolyLine(mSteps);
        //3. focus camera on the target
        mServices.focusCameraOnNewLocation();
    }

    @Override
    public void setNewPosition(){
        mServices.setNewPosition();
    }

    //fill markers in the map
    @Override
    public void fillMap(){
        mServices.fillMap(data);
    }

    //apply when GPS cahnges status of connectivity
    @Override
    public void onGpsStatusChanged(int i) {
        mServices.onGpsStatusChanged(i);
    }

    //onLocationChanged method for reacting to roaming higher than Util.MAX_CENTER_LOC
    @Override
    public boolean onLocationChanged(){
        //1. is it the first found location
        if (Util.getInstance().getCurrentLocation() == null){
            mServices.updateCurrentLocation();
        } else {
            MyLocation tmpLoc = new MyLocation(Util.getInstance().getCurrentLocation().getLat(),
                    Util.getInstance().getCurrentLocation().getLon());                                                                              //current location
            MyLocation refLoc = new MyLocation(mServices.getLocationUpdate().getLatitude(),
                                                                                 mServices.getLocationUpdate().getLongitude());                  //reference location

            //Update center of map if distance from the current map center is higher than Util.MAX_CENTER_LOC
            if (Util.getInstance().calculateDistance(tmpLoc, refLoc) > Util.MAX_CENTER_LOC) {
                //1. update current center location
                MyLocation loc = Util.getInstance().getCurrentLocation();
                loc.setLat(mServices.getLocationUpdate().getLatitude());
                loc.setLon(mServices.getLocationUpdate().getLongitude());
                //2. set new center position on the map
                mServices.setLocation(loc);
                setNewPosition();
                //3. refresh places search and set the recyclerview
               return true;
            }

        }
        return false;
    }

//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Properties>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
@Override
public List<Target> getData() {
    return data;
}
 //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<End Properties>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//

    /////<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<TOOLS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//




    ////<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<END TOOLS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
}
