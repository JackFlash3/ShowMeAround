package com.example.prodigalson7.showme.ShowMeAround;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

/**
 * Created by ProdigaLsON7 on 14/06/2018.
 */

public class TestGson {
    private final String URL = "http://api.androidhive.info/json/movies.json";
  //  private final String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=32.7940,34.9896&radius=1000&type=restaurant&key=AIzaSyAc63LwuQZ_6yjFJvpkHyelI8DQavkcT0E";
    public void downloadMoviesList()  {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getPlaces();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void getPlaces()  throws JSONException{
        String serverResponse = null;   //Result from the server
        InputStream mInputStream;

        try {
           // mInputStream = getStream(URL);                                                  //create an inputStream pipe
           // serverResponse = convertStreamToJSON(mInputStream);     //convert stream to a JSONObject
            serverResponse = "[{\n" +
                    "        \"title\": \"Dawn of the Planet of the Apes\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/1.jpg\",\n" +
                    "        \"rating\": 8.3,\n" +
                    "        \"releaseYear\": 2014,\n" +
                    "        \"genre\": [\"Action\", \"Drama\", \"Sci-Fi\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"District 9\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/2.jpg\",\n" +
                    "        \"rating\": 8,\n" +
                    "        \"releaseYear\": 2009,\n" +
                    "        \"genre\": [\"Action\", \"Sci-Fi\", \"Thriller\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"Transformers: Age of Extinction\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/3.jpg\",\n" +
                    "        \"rating\": 6.3,\n" +
                    "        \"releaseYear\": 2014,\n" +
                    "        \"genre\": [\"Action\", \"Adventure\", \"Sci-Fi\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"X-Men: Days of Future Past\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/4.jpg\",\n" +
                    "        \"rating\": 8.4,\n" +
                    "        \"releaseYear\": 2014,\n" +
                    "        \"genre\": [\"Action\", \"Sci-Fi\", \"Thriller\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"The Machinist\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/5.jpg\",\n" +
                    "        \"rating\": 7.8,\n" +
                    "        \"releaseYear\": 2004,\n" +
                    "        \"genre\": [\"Drama\", \"Thriller\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"The Last Samurai\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/6.jpg\",\n" +
                    "        \"rating\": 7.7,\n" +
                    "        \"releaseYear\": 2003,\n" +
                    "        \"genre\": [\"Action\", \"Drama\", \"History\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"The Amazing Spider-Man 2\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/7.jpg\",\n" +
                    "        \"rating\": 7.3,\n" +
                    "        \"releaseYear\": 2014,\n" +
                    "        \"genre\": [\"Action\", \"Adventure\", \"Fantasy\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"Tangled\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/8.jpg\",\n" +
                    "        \"rating\": 7.9,\n" +
                    "        \"releaseYear\": 2010,\n" +
                    "        \"genre\": [\"Action\", \"Drama\", \"Sci-Fi\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"Rush\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/9.jpg\",\n" +
                    "        \"rating\": 8.3,\n" +
                    "        \"releaseYear\": 2013,\n" +
                    "        \"genre\": [\"Animation\", \"Comedy\", \"Family\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"Drag Me to Hell\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/10.jpg\",\n" +
                    "        \"rating\": 6.7,\n" +
                    "        \"releaseYear\": 2009,\n" +
                    "        \"genre\": [\"Horror\", \"Thriller\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"Despicable Me 2\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/11.jpg\",\n" +
                    "        \"rating\": 7.6,\n" +
                    "        \"releaseYear\": 2013,\n" +
                    "        \"genre\": [\"Animation\", \"Comedy\", \"Family\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"Kill Bill: Vol. 1\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/12.jpg\",\n" +
                    "        \"rating\": 8.2,\n" +
                    "        \"releaseYear\": 2003,\n" +
                    "        \"genre\": [\"Action\", \"Crime\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"A Bug's Life\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/13.jpg\",\n" +
                    "        \"rating\": 7.2,\n" +
                    "        \"releaseYear\": 1998,\n" +
                    "        \"genre\": [\"Animation\", \"Adventure\", \"Comedy\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"Life of Brian\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/14.jpg\",\n" +
                    "        \"rating\": 8.9,\n" +
                    "        \"releaseYear\": 1972,\n" +
                    "        \"genre\": [\"Comedy\"]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"title\": \"How to Train Your Dragon\",\n" +
                    "        \"image\": \"https://api.androidhive.info/json/movies/15.jpg\",\n" +
                    "        \"rating\": 8.2,\n" +
                    "        \"releaseYear\": 2010,\n" +
                    "        \"genre\": [\"Animation\", \"Adventure\", \"Family\"]\n" +
                    "    }]";
            Gson gson = new Gson();
            String jsonOutput = serverResponse.toString();
            Type listType = new TypeToken<List<Movie>>(){}.getType();
            List<Movie> movies = gson.fromJson(jsonOutput, listType);
        }  catch (Exception e){
            String msg = e.getMessage();

        }

    }

    //Download the JSONObject using URLConnection
    private InputStream getStream(String mURL) {
        try {
            InputStream is = new URL(mURL).openStream();
            return is;
        } catch (Exception ex) {
            String msg = ex.getMessage();
            return null;
        }
    }

    //Convert InputStream to JSONObject
    private JSONObject convertStreamToJSON(InputStream inputStreamObject) throws IOException, JSONException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);

        JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
        return jsonObject;
    }
}
