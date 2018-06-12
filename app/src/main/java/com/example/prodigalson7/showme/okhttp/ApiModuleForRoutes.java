package com.example.prodigalson7.showme.okhttp;

/**
 * Created by ProdigaLsON7 on 05/06/2018.
 */
import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModuleForRoutes {
    public final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    public final String API_KEY = "AIzaSyAc63LwuQZ_6yjFJvpkHyelI8DQavkcT0E";


    @Provides
    public OkHttpClient provideClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter(
                        "key",
                        API_KEY
                ).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        }).build();
    }

    @Provides
    public Retrofit provideRetrofit(String baseURL, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //this helps to return the Observable values
                .addConverterFactory(GsonConverterFactory.create())             //convert JSON into Java objects
                .build();
    }

    //creates an Anonymous object of PlaceApiService that performs the "GET" call
    @Provides
    public RouteApiService provideApiService() {
        return provideRetrofit(BASE_URL, provideClient()).create(RouteApiService.class);
    }




}
