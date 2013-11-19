package com.ese2013.mensaunibe.util.database.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Outline of the mensa-table for the database
 * 
 * @author ese2013-team5
 *
 */
public class MensaTable implements Table {
	
	// Database table
	public static final String TABLE_NAME = "mensa";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_CITY = "city";
	public static final String COLUMN_LAT = "lat";
	public static final String COLUMN_LON = "lon";
	// Table create statement
	public static final String TABLE_CREATE = 
			"create table "
			+ TABLE_NAME 
			+ "("
			+ COLUMN_ID + " integer primary key, "
			+ COLUMN_NAME + " text not null, "
			+ COLUMN_ADDRESS + " text not null, "
			+ COLUMN_CITY + " text not null, "
			+ COLUMN_LAT + " real not null, "
			+ COLUMN_LON + " real not null"
			+ ");";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}
	
	@Override
	public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + TABLE_NAME);
		onCreate(db);
	}

}
