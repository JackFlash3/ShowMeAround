package com.example.prodigalson7.showme.concurrency;

import android.database.Cursor;
import android.os.AsyncTask;

import com.example.prodigalson7.showme.DataBase.DataBaseConnector;
import com.example.prodigalson7.showme.Model.MyLocation;
import com.example.prodigalson7.showme.Model.Target;
import com.example.prodigalson7.showme.Model.Util;

import java.util.List;

/**
 * Created by ProdigaLsON7 on 21/03/2018.
 */

public class LoadPlaces extends AsyncTask{

    private DataBaseConnector contactsDB;
    private List<Target> data;

    public LoadPlaces( DataBaseConnector contactsDB, List<Target> data) {
        this.contactsDB = contactsDB;
        this.data = data;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        removeRecordsFromList();                                                //clear the data List
        Cursor c = contactsDB.getDestinations();                      //retrieve the Data from SQLite
        retrieveDataFromSqlite(c);                                               //extract the data from the cursor
        Util.getInstance().setConcurrencyFlag(false);              //end blocking
        return null;
    }

//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<TOOLS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
private void removeRecordsFromList() {
    Target target;

    int listSize = this.data.size();
    for (int i = 0; i < listSize; i++) {
        target = this.data.remove(0);
    }
}


private void retrieveDataFromSqlite(Cursor c) {
        if (c.getCount() > 0) {
            while (c.moveToNext() == true) {
                Target target = new Target(c.getInt(c.getColumnIndex("_id")),
                        c.getString(c.getColumnIndex("name")),
                        c.getDouble(c.getColumnIndex("rating")),
                        Util.Subject.valueOf(c.getString(c.getColumnIndex("subject"))),
                        c.getString(c.getColumnIndex("address")),
                        c.getString(c.getColumnIndex("photo_ref")),
                        c.getString(c.getColumnIndex("place_id")),
                        new MyLocation(c.getDouble(c.getColumnIndex("latitude")), c.getDouble(c.getColumnIndex("longitude"))));
                this.data.add(target);
            }
        }

}

/*
 Target(int id, String name, double rating, Util.Subject subject, String address, String photo_ref, String placeID,  MyLocation location)

private static final String CREATE_TABLE_DESTINATIONS = "CREATE TABLE "+ DESTINATIONS_TABLE
			+"("+ TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+  NAME+" TEXT, "+ ADDRESS +" TEXT, "+ SUBJECT +" TEXT, "+RATING+" DOUBLE, "
			+ LATITUDE +" DOUBLE, "+ LONGITUDE +" DOUBLE, "+PLACE_ID+" TEXT, "+PHOTO_REF+" TEXT );";



 */



//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<END TOOLS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
}
