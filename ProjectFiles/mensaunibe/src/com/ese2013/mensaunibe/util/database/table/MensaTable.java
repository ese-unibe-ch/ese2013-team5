package com.ese2013.mensaunibe.util.database.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Outline of the mensa-table for the database
 * 
 * @author Nicolas Kessler
 *
 */
public class MensaTable {
	
	// Database table
	public static final String TABLE_MENSA = "mensa";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_CITY = "city";
	public static final String COLUMN_LAT = "lat";
	public static final String COLUMN_LON = "lon";
	public static final String COLUMN_FAVORITE = "favorite";
	// Table create statement
	public static final String TABLE_CREATE = 
			"create table "
			+ TABLE_MENSA 
			+ "("
			+ COLUMN_ID + " integer primary key, "
			+ COLUMN_NAME + " text not null, "
			+ COLUMN_ADDRESS + " text not null, "
			+ COLUMN_CITY + " text not null, "
			+ COLUMN_LAT + " real not null, "
			+ COLUMN_LON + " real not null, "
			+ COLUMN_FAVORITE + " integer not null"
			+ ");";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + TABLE_MENSA);
		onCreate(db);
	}

}
