package com.example.prodigalson7.showme.ShowMeAround;


import android.content.Context;

import com.example.prodigalson7.showme.DataBase.DataBaseConnector;
import com.example.prodigalson7.showme.Model.Target;
import com.example.prodigalson7.showme.Model.Util;
import com.example.prodigalson7.showme.okhttp.apimodelplaces.Result;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public interface Repository {

    public void getPlacesFromNetwork(DataBaseConnector contactsDB, Util.Subject subject, Context context);      //download places from the network

    public void clearDB(DataBaseConnector contactsDB, DisposableObserver<String> clearDBSubscriber);      //clear the SQLite DB

    public void loadPlacesFromDB(DataBaseConnector contactsDB, List<Target> dataTmp, DisposableObserver<String> loadFromDBSubscriber);       //Load records from the SQLite DB

    public void loadImagesFromNetwork(List<Target> dataTmp);                   //Load images of targets from the Network

}
