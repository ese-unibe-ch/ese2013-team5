package com.mensaunibe.util.database;

import java.util.ArrayList;
import java.util.List;

import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.app.model.Menu;
import com.mensaunibe.util.database.tables.MensaTable;
import com.mensaunibe.util.database.tables.MenuTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * class to manage all functionalities of the database.
 * save objects with save(), load the Model with load()
 *
 */
public class DatabaseManager {
	
	private SQLiteDatabase mDB;
	private DatabaseHelper mHelper;
	private DatabaseService mDBService;
	
	public DatabaseManager(Context cont){
		this.mHelper = new DatabaseHelper(cont);
		this.mDB = mHelper.getWritableDatabase();
		this.mDBService = new DatabaseService();
	}
	
	public void save(MensaList mensalist){
		List<Mensa> list = mensalist.getMensas();
		for (Mensa mensa : list){
			save(mensa);
			for(Menu menu : mensa.getAllMenus()){
				save(menu);
			}
		}
	}

	public void save(Mensa m) {
		ContentValues values = mDBService.toValue(m);
		mDB.insertWithOnConflict(MensaTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void save(Menu menu){
		ContentValues values = mDBService.toValue(menu);
		mDB.insertWithOnConflict(MenuTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public MensaList load(){
		return mDBService.createMensalist(mDB);
	}
	
}
