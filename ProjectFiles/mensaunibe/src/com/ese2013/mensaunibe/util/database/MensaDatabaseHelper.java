package com.ese2013.mensaunibe.util.database;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ese2013.mensaunibe.ActivityMain;
import com.ese2013.mensaunibe.util.database.table.FavoriteTable;
import com.ese2013.mensaunibe.util.database.table.MensaTable;
import com.ese2013.mensaunibe.util.database.table.MenuTable;
import com.ese2013.mensaunibe.util.database.table.Table;

public class MensaDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "mensa.db";
	private static final int DATABASE_VERSION = 8;
	private List<Table> tables = new ArrayList<Table>();

	public MensaDatabaseHelper() {
		super(ActivityMain.getContextOfApp(), DATABASE_NAME, null, DATABASE_VERSION);
		tables.add(new MensaTable());
		tables.add(new FavoriteTable());
		tables.add(new MenuTable());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (Table t : tables) {
			t.onCreate(db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (Table t : tables) {
			t.onUpgrade(db, oldVersion, newVersion);
		}
	}

}
