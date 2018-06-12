package com.example.prodigalson7.showme.ShowMeAround;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;


import com.example.prodigalson7.showme.Adapters.TargetsRecyclerAdapter;
import com.example.prodigalson7.showme.Model.MyLocation;
import com.example.prodigalson7.showme.Model.Util;
import com.example.prodigalson7.showme.R;
import com.example.prodigalson7.showme.Root.App;
import com.example.prodigalson7.showme.ShowMeAround.ShowMeAroundServices.ShowMeAroundServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
TODO: 1. transfer code to butterknife - ok
TODO: 2. Implement the MVP through dagger - ok
TODO: 2.1 implement the View methods - ok
TODO: 3. Implement retrofit methods of connection to the internet - ok
TODO: 4. Implement RxJava methods - ok
TODO: 4.1 replace all Asynctask methods with RxJava methods - ok
TODO: 5. check the RecyclerView data - ok
TODO: 6. Add OBservables to butoons: FAB, Settings, Cancel, Apply
TODO: 7. Move the GPS from the presenter (?)
TODO: 8. Check if can multiple subscribe in searchplaces - ok
TODO: 9. check routing by clicking markers and list targets - ok
TODO: 9. Implement JUnit tests with Mockito
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        TargetsRecyclerAdapter.ClickListener,
        LocationListener,
        GpsStatus.Listener,
        GoogleMap.OnMarkerClickListener,
        ShowMeActivityMVP.View
        {

    private GoogleMap mMap;                                                                                                                                                                            //Map object
    private LocationManager lm;                                                                                                                                                                        //The GPS Location Manager
    private ShowMeAroundServices mServices = new ShowMeAroundServices(this);             //Services Object             //Targets List

    //Adapters
    TargetsRecyclerAdapter targetsRVAdapter;
    //Views
    @BindView(R.id.targetsRV)
    android.support.v7.widget.RecyclerView targetsRV;                               //Our RecyclerView

    @BindView(R.id.sliding_layout)
    com.sothree.slidinguppanel.SlidingUpPanelLayout sliding_layout;     //the sliding panel

    @BindView(R.id.fab)
    android.support.design.widget.FloatingActionButton fab;                    //floating button for refreashing places search

    @Inject
    ShowMeActivityMVP.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1. Map Configurations
        setContentView(R.layout.activity_maps);

        //2. Wiring up all the dependencies
        ((App) getApplication()).getComponent().inject(this);   //wiring up all the dependencies

        //3. Bind to butterknife
        ButterKnife.bind(this);                 //binding butterknife

        //4. window and top action bar configuration
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //4.1 Change Actionbar title
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle("ShowMeAround");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //4.2 sliding panel configuration
        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        sliding_layout.setAnchorPoint(0.5f);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //5.   Set GPS manager
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) this);
        lm.addGpsStatusListener(this);
        Location currentLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (currentLocation != null)
            Util.getInstance().setCurrentLocation(new MyLocation(currentLocation.getLatitude(),currentLocation.getLongitude()));
        else
            Util.getInstance().setCurrentLocation(new MyLocation(52.5200,13.4050));         //Berlin Location

        //6. floating button
        this.fab.setOnClickListener(fabListener);
        //7. load data from  sharedPreferences
        this.loadFromSharedPreferences();
    }

//ToDO: https://developers.google.com/places/web-service/search
//ToDO: Google places API key: AIzaSyAc63LwuQZ_6yjFJvpkHyelI8DQavkcT0E
//ToDO: Search shall be made: 1. When center location updates. 2. onResume() 3. after clicking the floating button

//perform first search
    @Override
    protected void onResume() {
        super.onResume();
        //0. set the view and the Srvices in the presenter
        presenter.setView(this);                              //setting the Activity in the Presenter
        presenter.setServices(mServices);             //setting the services object in the presenter
        try {
        //1. clear the data
        //2. clean DB
        presenter.clearDB();

        //3. search for places
        presenter.searchPlaces();

        //4. load places from the DB
        presenter.loadPlacesFromDB();
        //5. load the targets images from the internet

        presenter.loadImages();
         //6. configure the RecyclerView
        configureRecyclerView();
            //targetsRVAdapter.notifyDataSetChanged();
        } catch (Exception e){
            String msg = e.getMessage();
        }

  }
     @Override
     protected void onDestroy(){
           super.onDestroy();
           presenter.rxUnsubscribe();
     }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                MapsActivity.this.finishAffinity();
                break;
            default:
                Settings settingsDialog = new Settings(MapsActivity.this, targetsRVAdapter);
                settingsDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            mServices.setMap(mMap);                                                    //setting the google map in the services
            presenter.fillMap();                                                                //fill markers in the map
            mMap.setOnMarkerClickListener(this);                             // Set a listener for marker click.
        }catch (Exception e){
            String msg = e.getMessage();
        }
    }

    //Response to clicking on a RecyclerView item- fetching route from current location to the destination and draw it
    @Override
    public void itemClicked(View view, MyLocation mLocation) {
        //1. set map camera on the new location
        mServices.setLocation(mLocation);                //set the location in Services
        //2. draw the route between the center location and the destination
        presenter.onLocationClicked();                  //draw the polyLine

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
    public void onLocationChanged(Location location) {

        //1. is it the first found location
        if (Util.getInstance().getCurrentLocation() == null) {                                                                                      //first locaion after App startUp
            MyLocation loc = new MyLocation(location.getLatitude(), location.getLongitude());
            Util.getInstance().setCurrentLocation(loc);
        } else {                                                                                                                                                                       //Not the first  locaion after App startUp
            MyLocation tmpLoc = new MyLocation(Util.getInstance().getCurrentLocation().getLat(),
                    Util.getInstance().getCurrentLocation().getLon());                                                                              //current location
            MyLocation refLoc = new MyLocation(location.getLatitude(), location.getLongitude());                  //reference location

            //Update center of map if
            if (Util.getInstance().calculateDistance(tmpLoc, refLoc) > Util.MAX_CENTER_LOC) {
                //1. update current center location
                MyLocation loc = Util.getInstance().getCurrentLocation();
                loc.setLat(location.getLatitude());
                loc.setLon(location.getLongitude());
                //2. set new center position on the map
                mServices.setLocation(loc);
                presenter.setNewPosition();

            }
            if (Util.getInstance().calculateDistance(tmpLoc, refLoc) > Util.MAX_DISTANCE) {
                //2. refresh places search and set the recyclerview
                //1. clean DB
                presenter.clearDB();
                //2. search for places
                presenter.searchPlaces();
                //3. load places from the DB
                presenter.loadPlacesFromDB();
                //4. add new markers
                presenter.fillMap();
                //5. download images
                presenter.loadImages();
                //6. Update the RecyclerView Adapter
                targetsRVAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    //set back button to return to IntroActivity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        presenter.onKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Listeners>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
    View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //1. clean DB
            presenter.clearDB();
            //2. search for places
            presenter.searchPlaces();
            //3. load places from the DB
            presenter.loadPlacesFromDB();
            //4. set new markers
            presenter.fillMap();
            //5. download images
            presenter.loadImages();
            //6. Update the RecyclerView Adapter
            targetsRVAdapter.notifyDataSetChanged();
        }
    };

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        //1. set the marker+
        mServices.setMarker(marker);
        //2. get the route to the target destination
        presenter.onMarkerClick();

        return false;
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<End Listeners>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//






    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<TOOLS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
    private void loadFromSharedPreferences()
    {
        Context context = getApplicationContext();
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(
                    "mySharedPreferences", Context.MODE_PRIVATE);
            //read from sharedpreferences

           int spa = sharedPref.getInt("spa", 0);
           int bar = sharedPref.getInt("bar", 0);
           int gym = sharedPref.getInt("gym", 0);
           int dancebar = sharedPref.getInt("dancebar", 0);
           int restaurant = sharedPref.getInt("restaurant", 0);
           int coffee = sharedPref.getInt("coffee", 0);
            int atm = sharedPref.getInt("atm", 0);
           //set Util
            Util.getInstance().getSearchOptions().setSpa(spa != 0);
            Util.getInstance().getSearchOptions().setBar(bar != 0);
            Util.getInstance().getSearchOptions().setPool(gym != 0);
            Util.getInstance().getSearchOptions().setDancebar(dancebar != 0);
            Util.getInstance().getSearchOptions().setRestaurant(restaurant != 0);
            Util.getInstance().getSearchOptions().setCoffee(coffee != 0);
            Util.getInstance().getSearchOptions().setAtm(atm != 0);
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
        }
    }

    //Access to the Presener from the Settings
    public ShowMeActivityMVP.Presenter getPresenter() { return presenter;}

    private void configureRecyclerView(){
        targetsRVAdapter = new TargetsRecyclerAdapter(this, presenter.getData());
        targetsRV.setAdapter(targetsRVAdapter);
        targetsRV.setLayoutManager(new LinearLayoutManager(this));
        targetsRVAdapter.setListener(this);                         //setting the delegate
    }
}
