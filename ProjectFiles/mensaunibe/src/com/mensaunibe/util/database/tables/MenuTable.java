package com.mensaunibe.util.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MenuTable implements Table {

	// Database table
	public static final String TABLE_NAME = "menu";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_TITLE_EN = "title_en";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_DAY = "day";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_DATE_EN = "date_en";
	public static final String COLUMN_DESC = "desc";
	public static final String COLUMN_DESC_EN = "desc_en";
	public static final String COLUMN_MENSA_ID	= "mensaId";
	public static final String COLUMN_WEEK = "week";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_PRICE_EN = "price_en";
	public static final String COLUMN_RATING = "rating";
	
	// Table create statement
	public static final String TABLE_CREATE = 
			"create table "
			+ TABLE_NAME
			+ "("
			+ COLUMN_ID + " integer primary key, "
			+ COLUMN_TITLE + " text not null, "
			+ COLUMN_TITLE_EN + " text not null, "
			+ COLUMN_TYPE + " text not null, "
			+ COLUMN_DAY + " text not null, "
			+ COLUMN_DATE + " text not null, "
			+ COLUMN_DATE_EN + " text not null, "
			+ COLUMN_DESC + " text not null, "
			+ COLUMN_DESC_EN + " text not null, "
			+ COLUMN_MENSA_ID	+ " integer not null, "
			+ COLUMN_WEEK + " integer not null, "
			+ COLUMN_PRICE + " text not null, "
			+ COLUMN_PRICE_EN + " text not null, "
			+ COLUMN_RATING + " real not null"
			+ ");";

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MenuTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion 
				+ ", which will destroy all old data");
		db.execSQL("drop table if exists " + TABLE_NAME);
		onCreate(db);
	}
}
