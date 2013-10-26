package com.ese2013.mensaunibe.util.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ese2013.mensaunibe.ActivityMain;
import com.ese2013.mensaunibe.util.database.table.FavoriteTable;
import com.ese2013.mensaunibe.util.database.table.MensaTable;
import com.ese2013.mensaunibe.util.database.table.MenuTable;

public class MensaDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "mensa.db";
	private static final int DATABASE_VERSION = 4;

	public MensaDatabaseHelper() {
		super(ActivityMain.getContextOfApp(), DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		MensaTable.onCreate(db);
		FavoriteTable.onCreate(db);
		MenuTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		MensaTable.onUpgrade(db, oldVersion, newVersion);
		FavoriteTable.onUpgrade(db, oldVersion, newVersion);
		MenuTable.onUpgrade(db, oldVersion, newVersion);
	}

}
