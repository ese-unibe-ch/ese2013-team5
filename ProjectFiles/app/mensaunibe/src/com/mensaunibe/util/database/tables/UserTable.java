package com.mensaunibe.util.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserTable implements Table{

	public static final String TABLE_NAME = "user";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_MENSAID = "mensaId";
	public static final String COLUMN_DEVICEID = "deviceId";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LASTUPDATE = "lastUpdate";
	
	private final String CREATE_TABLE = 
		"create table " 	
		+ TABLE_NAME 
		+ "("
		+ COLUMN_ID + " integer primary key, "
		+ COLUMN_MENSAID + " integer not null, "
		+ COLUMN_DEVICEID + " integer not null, "
		+ COLUMN_NAME + " text not null, "
		+ COLUMN_LASTUPDATE + " integer not null"
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
