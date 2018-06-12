package com.example.prodigalson7.showme.okhttp;

/**
 * Created by ProdigaLsON7 on 05/06/2018.
 */

import com.example.prodigalson7.showme.okhttp.apimodel.Routes;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RouteApiService {
    @GET("json")
    Observable<Routes> getRoute(@Query("origin") String origin,
                                @Query("destination") String destination);

}
