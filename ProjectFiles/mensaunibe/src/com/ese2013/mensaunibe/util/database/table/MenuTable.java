package com.ese2013.mensaunibe.util.database.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Outline for the menu-table for the mensa database
 * 
 * @author Nicolas Kessler
 * 
 */
public class MenuTable implements Table {

	// Database table
	public static final String TABLE_NAME = "menu";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DAY = "day";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_DESC = "desc";
	public static final String COLUMN_MENSA_ID	= "mensa id";
	public static final String COLUMN_WEEK = "week";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_RATING = "rating";
	// Table create statement
	public static final String TABLE_CREATE = 
			"create table "
			+ TABLE_NAME
			+ "("
			+ COLUMN_ID + " integer primary key, "
			+ COLUMN_TITLE + " text not null, "
			+ COLUMN_DAY + " text not null, "
			+ COLUMN_DATE + " text not null, "
			+ COLUMN_DESC + " text not null, "
			+ COLUMN_MENSA_ID	+ " integer not null, "
			+ COLUMN_WEEK + " integer not null, "
			+ COLUMN_PRICE + " text not null, "
			+ COLUMN_RATING + "real not null"
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
