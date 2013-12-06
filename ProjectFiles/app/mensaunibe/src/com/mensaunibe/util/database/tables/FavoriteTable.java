package com.mensaunibe.util.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FavoriteTable implements Table{
	
	public static final String TABLE_NAME = "favorite";
	public static final String COLUMN_ID = "_id";
	
	//Table create statement
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
		Log.w(FavoriteTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion 
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	
	

}
