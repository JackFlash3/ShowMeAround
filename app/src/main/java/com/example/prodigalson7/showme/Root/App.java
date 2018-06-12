package com.example.prodigalson7.showme.Root;

import android.app.Application;

import com.example.prodigalson7.showme.ShowMeAround.ShowMeModule;
import com.example.prodigalson7.showme.okhttp.ApiModuleForGames;
import com.example.prodigalson7.showme.okhttp.ApiModuleForPlaces;
import com.example.prodigalson7.showme.okhttp.ApiModuleForRoutes;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .apiModuleForPlaces(new ApiModuleForPlaces())
                .showMeModule(new ShowMeModule())
                .apiModuleForRoutes(new ApiModuleForRoutes())
                .apiModuleForGames(new ApiModuleForGames())
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
