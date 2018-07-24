package com.example.prodigalson7.showme.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBase extends SQLiteOpenHelper 
{
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

	private static final String CREATE_TABLE_DESTINATIONS = "CREATE TABLE "+ DESTINATIONS_TABLE
			+"("+ TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+  NAME+" TEXT, "+ ADDRESS +" TEXT, "+ SUBJECT +" TEXT, "+RATING+" DOUBLE, "
			+ LATITUDE +" DOUBLE, "+ LONGITUDE +" DOUBLE, "+PLACE_ID+" TEXT, "+PHOTO_REF+" TEXT );";

	public MyDataBase(Context context, String name, CursorFactory factory,
			int version) 
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(CREATE_TABLE_DESTINATIONS); 	// create DESTINATIONS_TABLE
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
	}

}
