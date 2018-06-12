package com.example.prodigalson7.showme.okhttp;


//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=32.7940,34.9896&radius=1000&type=restaurant&key=AIzaSyAc63LwuQZ_6yjFJvpkHyelI8DQavkcT0E

import com.example.prodigalson7.showme.okhttp.apimodelplaces.Places;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceApiService {

    @GET("api/place/nearbysearch/json?sensor=true")
    Observable<Places> getPlacesInArea(@Query("location") String location,
                                       @Query("radius") double radius,
                                       @Query("type") String type);


    @GET("api/place/nearbysearch/json?sensor=true&")
    Call<Places> getPlacesInAreaCall(@Query("location") String location,
                                         @Query("radius") double radius,
                                         @Query("type") String type);

}
