package com.example.prodigalson7.showme.Root;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    //We could inject a context of the application. This injection isn't really used in the App
    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }


}
