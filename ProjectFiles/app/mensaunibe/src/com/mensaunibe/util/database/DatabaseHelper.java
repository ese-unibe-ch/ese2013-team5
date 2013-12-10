package com.mensaunibe.util.database;

import java.util.ArrayList;

import com.mensaunibe.util.database.tables.FavoriteTable;
import com.mensaunibe.util.database.tables.MensaTable;
import com.mensaunibe.util.database.tables.MenuTable;
import com.mensaunibe.util.database.tables.Table;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * simple helper class extending SQLiteOpenHelper
 * to create a SQLiteDatabase.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

	public static DatabaseHelper instance;
	private ArrayList<Table> tables = new ArrayList<Table>();
	private static final String DATABASE_NAME = "mensa.db";
	private static final int DATABASE_VERSION = 8;
	
	
	public DatabaseHelper(Context cont) {
		super(cont, DATABASE_NAME, null, DATABASE_VERSION);
		instance = this;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		tables.add(new MensaTable());
		tables.add(new FavoriteTable());
		tables.add(new MenuTable());
		for(Table t : tables){
			t.onCreate(db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for(Table t : tables){
			t.onUpgrade(getWritableDatabase(), oldVersion, newVersion);
		}
	}

}
