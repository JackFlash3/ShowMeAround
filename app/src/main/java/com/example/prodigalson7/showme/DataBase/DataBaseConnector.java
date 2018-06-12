package com.example.prodigalson7.showme.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseConnector 
{// DAL - Data Access Layer
	// database name
	private static final String DATABASE_NAME = "Show_me_places";

	///////////////////Destinations TABLE////////////////////////
	private static final String DESTINATIONS_TABLE = "destinations";
	private static final String TASK_ID = "_id";
	private static final String NAME = "name";
	private static final String ADDRESS = "address";
	private static final String SUBJECT="subject";
	private static final String RATING="rating";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";
	private static final String PLACE_ID="place_id";
	private static final String PHOTO_REF = "photo_ref";

	private MyDataBase databaseOpenHelper = null; // database helper
	private SQLiteDatabase database = null; // database object

	// public constructor for DatabaseConnector
	public DataBaseConnector(Context context) 
	{
		databaseOpenHelper = new MyDataBase(context, DATABASE_NAME, null, 1);
	}

	// open the database connection
	public void open() throws SQLException
	{
		// create or open a database for reading/writing
		database = databaseOpenHelper.getWritableDatabase();
	}

	// close the database connection
	public void close() 
	{
		if (database != null)
			database.close(); // close the database connection
	}

	//===========================DESTINATIONS TABLE queries=========================//
	// inserts a new destination into the destinations table
	public void insertDestination(int task_id, String address, String name, String subject, double rating,
								 						 double latitude, double longitude, String place_id, String photo_ref)
	{
		ContentValues newDestination = new ContentValues();
		newDestination.put(TASK_ID, task_id);
		newDestination.put(ADDRESS, address);
		newDestination.put(NAME, name);
		newDestination.put(SUBJECT, subject);
		newDestination.put(RATING, rating);
		newDestination.put(LATITUDE, latitude);
		newDestination.put(LONGITUDE, longitude);
		newDestination.put(PLACE_ID, place_id);
		newDestination.put(PHOTO_REF, photo_ref);

		open();
		// null to inform android that there is no col id to be null
		database.insert(DESTINATIONS_TABLE, null, newDestination);
		close();
	}

	// return a Cursor with all the destinations information in the database
	public Cursor getDestinations() {

		open();
		Cursor c = database.rawQuery("SELECT * FROM "+
						DESTINATIONS_TABLE +
						" ORDER BY "+ TASK_ID+", "+ SUBJECT
						, null);
		return c;
	}

	   

   // Delete a destination specified by the given String name
   public void deleteDestination(int task_id)
   {
      open(); // open the database
      database.delete(DESTINATIONS_TABLE,
    		          TASK_ID +"=" + task_id,//where
    		          null);//where argument for where placeholder's
      close(); 
   }

	//delete all records from the database
	public void delete_all_destinations()
	{
		open();
		database.delete(DESTINATIONS_TABLE,
				null,//where
				null);//where argument for where placeholder's
		close();
	}

   // get a Cursor containing all information about the tasks specified
   // by the given id
   public Cursor getSingleDestination(int task_id)
   {

      return database.query(DESTINATIONS_TABLE,
    		  null,//all colmuns
    		  TASK_ID +"=" + task_id, //where
    		  null,//the argument for where placeholder's
    		  null,//group by
    		  null,//having
    		  null);//ordered by

   }

	// Get the amount of destinations in the table
	public int get_destination_table_size()
	{
		open(); // open the database
		Cursor c = database.rawQuery("SELECT COUNT(*) AS Size FROM "+ DESTINATIONS_TABLE , null);
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("Size"));
		 // close the database
	}
	//===========================END DESTINATIONS TABLE queries=========================//

}
