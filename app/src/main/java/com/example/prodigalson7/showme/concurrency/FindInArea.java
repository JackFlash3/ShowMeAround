package com.example.prodigalson7.showme.concurrency;

import android.content.Context;
import android.os.AsyncTask;

import com.example.prodigalson7.showme.DataBase.DataBaseConnector;
import com.example.prodigalson7.showme.Model.MyLocation;
import com.example.prodigalson7.showme.Model.Target;
import com.example.prodigalson7.showme.Model.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by ProdigaLsON7 on 19/03/2018.
 */

public class FindInArea extends AsyncTask {
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&type=restaurant&keyword=cruise&key=YOUR_API_KEY
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=32.7940,34.9896&radius=1000&type=restaurant&key=AIzaSyAc63LwuQZ_6yjFJvpkHyelI8DQavkcT0E
//https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CmRaAAAAocuQTJi4-DEFCanT3TJ8TkdoshWW9S72dPZHWRxLDR1xz_jqplUgPPmrTlWNGi0txiWsg_8S03XOz2eHMNHxTNSNZVi0WbgiSmZ7amqYsAOFZ2tui2Ew1lWahDMfT7_hEhCZkMvCjMkM6rkvWgWhqsVOGhQWpWHP3TZS0bhejBnqEb_adPSQFA&key=AIzaSyAc63LwuQZ_6yjFJvpkHyelI8DQavkcT0E

    private  String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private final String KEY = "key=AIzaSyAc63LwuQZ_6yjFJvpkHyelI8DQavkcT0E";

    //local variables
    private DataBaseConnector contactsDB;
    Context context;
    private MyLocation currentLocation = Util.getInstance().getCurrentLocation();
    private String radius = "radius="+Util.getInstance().getSearchRadius();
    private Util.Subject subject;
    private String type;
    private int dbSize = 0;

    public FindInArea(Context context, Util.Subject subject) {
        this.context = context;
        this.type = "type=" + subject.toString().toLowerCase();
        this.subject = subject;
        URL = URL + "location="+this.currentLocation.getLat()+","+this.currentLocation.getLon()+"&"+radius+"&"+this.type+"&"+KEY;
         this.contactsDB = new DataBaseConnector(this.context);
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        //1. DownLoad places
        try {
            dbSize = getDBSize();                   //get the table size
            getPlaces();
        } catch (JSONException e) {
            String msg = e.getMessage();
        }
        //2. reset blocking flag
        Util.getInstance().setConcurrencyFlag(false);

        return null;
    }


//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<TOOLS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
     private int getDBSize() {
        int size = contactsDB.get_destination_table_size();
        return size;
     }


    private boolean getPlaces()  throws JSONException{
            JSONObject serverResponse = null;   //Result from the server
            InputStream mInputStream;

            try {
            mInputStream = getStream(URL);                                                  //create an inputStream pipe
            serverResponse = convertStreamToJSON(mInputStream);     //convert stream to a JSONObject
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            catch (Exception e){
                e.printStackTrace();
                return false;
            }

            //deciphering the URL
            return parseJsonObject(serverResponse);
}

    //Download the JSONObject using URLConnection
    private InputStream getStream(String mURL) {
        try {
            InputStream is = new URL(mURL).openStream();
            return is;
        } catch (Exception ex) {
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

 //fill a new target inside the DB
 private void fillTargetRecordInDB(Target target) {
        //int task_id, String address, String name, String subject, int rating,
        // double latitude, double longitude, String photo_ref
     this.contactsDB.insertDestination(target.getId(), target.getAddress(), target.getName(), target.getSubject().toString(), target.getRating(),
                                     target.getmLocation().getLat(),    target.getmLocation().getLon(), target.getPlaceID(), target.getPhoto_ref());
 }

 //Parsing the JSONObject returned by Google places
private boolean parseJsonObject(JSONObject serverResponse) {
        MyLocation mLocation = new MyLocation(0.0, 0.0);
        String name = "";
        boolean open_now = false;
        String photo_ref = "";
        double rating = 0.0;
        String vicinity = "";
        String place_id = "";
        Target target;

         try {
                if (serverResponse != null) {
                    JSONArray results =  serverResponse.getJSONArray("results");

                    // loop among all results
                    for (int i = 0; i < results.length(); i++) {
                        // loop among all addresses within this result
                        JSONObject result = results.getJSONObject(i);
                        if (result.has("geometry")) {                                                                                           //geometry
                            JSONObject jGeometry = result.getJSONObject("geometry");
                            if (jGeometry.has("location")) {
                                JSONObject jLocation = jGeometry.getJSONObject("location");
                                mLocation.setLat(jLocation.getDouble("lat"));
                                mLocation.setLon(jLocation.getDouble("lng"));
                            }
                            if ( result.has("name")) {                                                                                                //name
                                name = result.getString("name");
                            }
                            if ( result.has("opening_hours")) {                                                                              //opening hours
                                JSONObject jOpeningHours = result.getJSONObject("opening_hours");
                                if (jOpeningHours.has("open_now")) {
                                    open_now = jOpeningHours.getBoolean("open_now");
                                }
                            }
                            if ( result.has("photos")) {                                                                                               //Photos
                                JSONArray jPhotos = result.getJSONArray("photos");
                                JSONObject jPhoto = jPhotos.getJSONObject(0);
                                photo_ref = jPhoto.getString("photo_reference");
                            }
                            if ( result.has("rating")) {                                                                                                   //rating
                                rating = result.getDouble("rating");
                            }
                            if ( result.has("vicinity")) {                                                                                               //vicinity
                                vicinity = result.getString("vicinity");
                            }
                            if ( result.has("place_id")) {                                                                                               //vicinity
                                place_id = result.getString("place_id");
                            }

                                 //Add a new Target to the DB
                                target = new Target(i+1+dbSize,  name, rating, this.subject, vicinity, photo_ref, place_id,  mLocation);

                                //insert a contact to the DB
                                fillTargetRecordInDB(target);
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }


}
