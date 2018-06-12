package com.example.prodigalson7.showme.Root;

import com.example.prodigalson7.showme.ShowMeAround.MapsActivity;
import com.example.prodigalson7.showme.ShowMeAround.ShowMeModule;
import com.example.prodigalson7.showme.okhttp.ApiModuleForPlaces;
import com.example.prodigalson7.showme.okhttp.ApiModuleForRoutes;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModuleForPlaces.class, ApiModuleForRoutes.class,ShowMeModule.class})
public interface ApplicationComponent {

    //we inject only the TopMoviesActivity because we have only one Activity in our project
    void inject(MapsActivity target);

}
