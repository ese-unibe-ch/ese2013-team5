package com.mensaunibe.util.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MensaTable implements Table{
	
	public static final String TABLE_NAME = "mensa";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_NAME_EN = "name_en";
	public static final String COLUMN_DESC = "desc";
	public static final String COLUMN_DESC_EN = "desc_en";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_CITY = "city";
	public static final String COLUMN_LAT = "lat";
	public static final String COLUMN_LON = "lon";
	
	public static final String CREATE_TABLE = 
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
		db.execSQL(CREATE_TABLE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MensaTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion 
				+ ", which will destroy all old data");
		db.execSQL("drop table if exists " + TABLE_NAME);
		onCreate(db);
	}

}
