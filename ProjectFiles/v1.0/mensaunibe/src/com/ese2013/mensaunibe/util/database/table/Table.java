package com.ese2013.mensaunibe.util.database.table;

import android.database.sqlite.SQLiteDatabase;

public interface Table {

	public void onCreate(SQLiteDatabase db);

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
