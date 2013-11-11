package com.ese2013.mensaunibe.util.database.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Outline for the favorite-table for the mensa database
 * 
 * @author ese2013-team5
 * 
 */
public class FavoriteTable implements Table {

	// Database table
	public static final String TABLE_NAME = "favorite";
	public static final String COLUMN_ID = "_id";
	// Table create statement
	public static final String TABLE_CREATE = 
			"create table "
			+ TABLE_NAME
			+ "("
			+ COLUMN_ID + " integer primary key"
			+ ");";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + TABLE_NAME);
		onCreate(db);
	}
	
}
