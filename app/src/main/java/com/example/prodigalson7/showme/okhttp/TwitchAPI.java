package com.example.prodigalson7.showme.okhttp;



import com.example.prodigalson7.showme.okhttp.apimodelgames.Twitch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface TwitchAPI {

    @GET("games/top")
    Call<Twitch> getTopGames(@Header("Client-Id") String clientId);





}
