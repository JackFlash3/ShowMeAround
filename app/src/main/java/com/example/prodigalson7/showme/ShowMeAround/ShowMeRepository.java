package com.example.prodigalson7.showme.ShowMeAround;

import android.content.Context;
import android.database.Cursor;

import com.example.prodigalson7.showme.DataBase.DataBaseConnector;
import com.example.prodigalson7.showme.Model.MyLocation;
import com.example.prodigalson7.showme.Model.Target;
import com.example.prodigalson7.showme.Model.Util;
import com.example.prodigalson7.showme.okhttp.PlaceApiService;
import com.example.prodigalson7.showme.okhttp.apimodelplaces.Places;
import com.example.prodigalson7.showme.okhttp.apimodelplaces.Result;



import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ShowMeRepository implements Repository {

    private TwitchAPI twitchAPI;
    private PlaceApiService placeApiService;
    private Disposable placesSubscriber = null;         //subscriber to placesObservable

    public ShowMeRepository(PlaceApiService placeApiService, TwitchAPI twitchAPI) {
        this.placeApiService = placeApiService;
        this.twitchAPI = twitchAPI;
    }

    //clear the DB -- via Observables instead of Asynctask
    @Override
    public void clearDB(DataBaseConnector contactsDB, DisposableObserver<String> clearDBSubscriber) {
        Observable<String> clearDBObservable = Observable.just(clearDBOperation(contactsDB));
        clearDBSubscriber = clearDBObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {}

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String value) { //called each time a new ViewModel is created

                    }
                });
    }

    //load the places data from the SQLite DB via Observables
    @Override
    public void loadPlacesFromDB(DataBaseConnector contactsDB, List<Target> dataTmp, DisposableObserver<String> loadFromDBSubscriber) {
        Observable<String> loadFromDBObservable = Observable.just(loadPlacesFromRepository(contactsDB, dataTmp));
        loadFromDBSubscriber = loadFromDBObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {}

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String value) {
                        int i = 1;
                    }
                    });
    }
    //download placs through the network
    @Override
    public void getPlacesFromNetwork(DataBaseConnector contactsDB, Util.Subject subject, Context context) {
        MyLocation currentLocation = Util.getInstance().getCurrentLocation();
        String location = currentLocation.getLat() + "," + currentLocation.getLon();
        double radius = Util.getInstance().getSearchRadius();
        String type = subject.toString().toLowerCase();

        //get Places from the URL
        Observable<Places> placesObservable = placeApiService.getPlacesInArea(location, radius, type);

        Observable<Result> placeObservable =  placesObservable.flatMap(new Function<Places, Observable<Result>>() {
            //Observable stream of inner objects of results
            @Override
            public Observable<Result> apply(Places places) {
                return Observable.fromIterable(places.getResults());   //topRated.results is the results field of the JSON object
            }
        });
        registerResultsInDB(placeObservable, contactsDB, subject);

    }


    @Override
    public void loadImagesFromNetwork(List<Target> dataTmp) {
        ImagesDownloader imagesDownloader = new ImagesDownloader(dataTmp);
        Util.getInstance().setConcurrencyFlag(true);
        imagesDownloader.runnableFactory();
        while (Util.getInstance().isConcurrencyFlag()) {
        }
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Tools>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//


    private String clearDBOperation(DataBaseConnector contactsDB){
        contactsDB.delete_all_destinations();
        return "complete";
    }

    private void removeRecordsFromList(List<Target> dataTmp) {
        Target target;

        int listSize = dataTmp.size();
        for (int i = 0; i < listSize; i++) {
            target = dataTmp.remove(0);
        }
    }

    private void retrieveDataFromSqlite(Cursor c, List<Target> dataTmp) {
        if (c.getCount() > 0) {
            while (c.moveToNext() == true) {
                Target target = new Target(c.getInt(c.getColumnIndex("_id")),
                        c.getString(c.getColumnIndex("name")),
                        c.getDouble(c.getColumnIndex("rating")),
                        Util.Subject.valueOf(c.getString(c.getColumnIndex("subject"))),
                        c.getString(c.getColumnIndex("address")),
                        c.getString(c.getColumnIndex("photo_ref")),
                        c.getString(c.getColumnIndex("place_id")),
                        new MyLocation(c.getDouble(c.getColumnIndex("latitude")), c.getDouble(c.getColumnIndex("longitude"))));
                dataTmp.add(target);
            }
        }
    }

    private String loadPlacesFromRepository(DataBaseConnector contactsDB, List<Target> dataTmp)
    {
        removeRecordsFromList(dataTmp);                                                //clear the data List
        Cursor c = contactsDB.getDestinations();                                        //retrieve the Data from SQLite
        retrieveDataFromSqlite(c, dataTmp);                                               //extract the data from the cursor
        return "complete";
    }

    //register a place in the Database
    private void registerResultsInDB(Observable<Result> placeObservable, DataBaseConnector contactsDB, Util.Subject subject) {

        placesSubscriber = placeObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io()/*AndroidSchedulers.mainThread()*/)
                .subscribeWith(new DisposableObserver<Result>() {
                    @Override
                    public void onComplete() {
                        Util.getInstance().setConcurrencyFlag(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Result result) { //called each time a new ViewModel is created

                        int dbSize = getDBSize(contactsDB);                                                       //retrieve dbsize
                        //2. parse through results

                        MyLocation mLocation = new MyLocation(result.getGeometry().getLocation().getLat(),
                                result.getGeometry().getLocation().getLng());
                        String name = result.getName();
                        String photo_ref = result.getPhotos()==null?"":result.getPhotos().get(0).getPhotoReference();
                        double rating = result.getRating()==null?0: result.getRating();
                        String vicinity = result.getVicinity();
                        String place_id = result.getPlaceId();
                        //Add a new Target to the DB
                        Target target = new Target(1 + dbSize, name, rating, subject, vicinity, photo_ref, place_id, mLocation);

                        //insert a contact to the DB
                        fillTargetRecordInDB(target, contactsDB);

                    }

                });
    }

    private int getDBSize(DataBaseConnector contactsDB) {
        int size = contactsDB.get_destination_table_size();
        return size;
    }

    private void fillTargetRecordInDB(Target target, DataBaseConnector contactsDB) {
        //int task_id, String address, String name, String subject, int rating,
        // double latitude, double longitude, String photo_ref
        contactsDB.insertDestination(target.getId(), target.getAddress(), target.getName(), target.getSubject().toString(), target.getRating(),
                target.getmLocation().getLat(),    target.getmLocation().getLon(), target.getPlaceID(), target.getPhoto_ref());
    }
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<End Tools>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
}
