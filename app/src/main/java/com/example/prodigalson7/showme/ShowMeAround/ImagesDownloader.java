package com.example.prodigalson7.showme.ShowMeAround;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.prodigalson7.showme.Model.Target;
import com.example.prodigalson7.showme.Model.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ProdigaLsON7 on 26/04/2018.
 */

public class ImagesDownloader {
    private ExecutorService executor = Executors.newFixedThreadPool(8);                                                           //pool thread for downloading the images
    private List<Target> data = new ArrayList<Target>();                                                                                                                 //Targets List
    private String URL = "https://maps.googleapis.com/maps/api/place/photo?";                                                               //photo URL
    private String key = "AIzaSyAc63LwuQZ_6yjFJvpkHyelI8DQavkcT0E";                                                                                //Google places key

    public ImagesDownloader(List<Target> data) {
        this.data = data;
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<TOOLS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
    public void runnableFactory() {
        for (int i = 0; i  < data.size(); i++) {
            myRunnable mRunnable = new myRunnable(data.get(i));
            executor.execute(mRunnable);
        }
        executor.shutdown();
        while (!executor.isTerminated()){}
        Util.getInstance().setConcurrencyFlag(false);
    }

    private String buildURL(String photoref) {
        String newURL = URL +"maxwidth=800&"+"photoreference="+photoref+"&key="+key;
        return newURL;
    }

    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class myRunnable implements Runnable {

        Target target;

        public myRunnable( Target target) {
            this.target = target;
        }

        @Override
        public void run() {
            String url = buildURL(target.getPhoto_ref()) == ""?buildURL(defaultPhotoref()): buildURL(target.getPhoto_ref());
            Bitmap targetPhoto = getBitmapFromURL(url);
            target.setPhoto(targetPhoto);
        }
    }

    private String defaultPhotoref(){
        return "CmRaAAAAVJVUU1GIzkcJCFX0LpGf61ZXlKUIu5bsVfnhrDBF6cR6TMvZmxhuY8erxxsHUPHKSkZswl7E5plJJwxtQNPfy8Zp5opdVIHOSJfCj_4VkKpSkyvFGHVQhqKNofKQ6yU-EhDbKy3VKhqA3Oqb4fWPtejUGhSMS-a9cXuMmWuC1wycIkQz7q-gXg";
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<END TOOLS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//

}
