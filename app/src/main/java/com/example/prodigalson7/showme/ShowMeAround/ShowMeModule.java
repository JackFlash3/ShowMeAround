package com.example.prodigalson7.showme.ShowMeAround;

import com.example.prodigalson7.showme.okhttp.PlaceApiService;
import com.example.prodigalson7.showme.okhttp.RouteApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

//Dagger Module - creating the Presenter, Model and Repository before the actual running of the App
@Module
public class ShowMeModule {

    @Provides
    public ShowMeActivityMVP.Presenter provideShowMeActivityPresenter(ShowMeActivityMVP.Model showMeModel) {
        return new ShowMePresenter(showMeModel);
    }

    @Provides
    public ShowMeActivityMVP.Model provideShowMeActivityModel(Repository repository, RouteApiService routeApiService) {
        return new ShowMeModel(repository, routeApiService);
    }

    @Singleton
    @Provides
    public Repository provideRepo(PlaceApiService placeApiService) {
        return new ShowMeRepository(placeApiService);
    }


}
