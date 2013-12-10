package com.mensaunibe.util.database;

import java.util.List;

import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.app.model.Menu;
import com.mensaunibe.util.database.tables.FavoriteTable;
import com.mensaunibe.util.database.tables.MensaTable;
import com.mensaunibe.util.database.tables.MenuTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * class to manage all functionalities of the database.
 * save objects with save(), load the Model with load()
 *
 */
public class DatabaseManager {
	
	// for logging and debugging purposes
	private static final String TAG = DatabaseManager.class.getSimpleName();
	
	private DatabaseHelper mDBHelper;
	private SQLiteDatabase mDB;
	private DatabaseService mDBService;
	
	public DatabaseManager(Context context){
		this.mDBHelper = new DatabaseHelper(context);
		this.mDB = mDBHelper.getWritableDatabase();
		this.mDBService = new DatabaseService();
	}
	
	public void save(MensaList mensalist) {
		List<Mensa> list = mensalist.getMensas();
		for(Mensa mensa : list) {
			save(mensa);
			if (mensa.isFavorite()) {
				saveFavorite(mensa);
			}
			
			for(Menu menu : mensa.getAllMenus()) {
				save(menu);
			}
		}
	}

	public void saveFavorite(Mensa mensa) {
		ContentValues values = new ContentValues();
		values.put(FavoriteTable.COLUMN_ID, mensa.getId());
		mDB.insertWithOnConflict(FavoriteTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void removeFavorite(Mensa mensa) {
		mDB.delete(FavoriteTable.TABLE_NAME, FavoriteTable.COLUMN_ID + " = ?", new String[] { "" + mensa.getId() });
	}
	
	public boolean isFavorite(Mensa mensa) {
		return mDBService.isFavorite(mensa, mDB);
	}

	public void save(Mensa m) {
		ContentValues values = mDBService.toValue(m);
		mDB.insertWithOnConflict(MensaTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void save(Menu menu) {
		ContentValues values = mDBService.toValue(menu);
		mDB.insertWithOnConflict(MenuTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public MensaList load(){
		Log.i(TAG, "load()");
		return mDBService.createMensalist(mDB);
	}

	
}
