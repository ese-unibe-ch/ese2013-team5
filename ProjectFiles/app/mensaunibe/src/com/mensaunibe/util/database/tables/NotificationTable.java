package com.mensaunibe.util.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NotificationTable implements Table{
	
	public static final String TABLE_NAME = "notification";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FROM = "from";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_MESSAGE = "message";
	public static final String COLUMN_READ = "read";
	
	public static final String CREATE_TABLE =
			"create table "
			+ TABLE_NAME
			+ "("
			+ COLUMN_ID + " integer primary key "
			+ COLUMN_FROM + " text not null "
			+ COLUMN_DATE + " text not null "
			+ COLUMN_MESSAGE + " text not null "
			+ COLUMN_READ + " integer not null"
			+ "(;";
			
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
