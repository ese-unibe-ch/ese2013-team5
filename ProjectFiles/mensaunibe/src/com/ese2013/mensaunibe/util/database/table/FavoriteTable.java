package com.ese2013.mensaunibe.util.database.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Outline for the favorite-table for the mensa database
 * 
 * @author Nicolas Kessler
 * 
 */
public class FavoriteTable {

	// Database table
	public static final String TABLE_FAVORITE = "favorite";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FAVORITE = "favorite";
	// Table create statement
	public static final String TABLE_CREATE = 
			"create table "
			+ TABLE_FAVORITE
			+ "("
			+ COLUMN_ID + " integer primary key, "
			+ COLUMN_FAVORITE + " integer not null"
			+ ");";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + TABLE_FAVORITE);
		onCreate(db);
	}
	
}
