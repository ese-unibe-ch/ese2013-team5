package com.ese2013.mensaunibe.util.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Menu;
import com.ese2013.mensaunibe.util.BuilderMensa;
import com.ese2013.mensaunibe.util.database.table.FavoriteTable;
import com.ese2013.mensaunibe.util.database.table.MensaTable;
import com.ese2013.mensaunibe.util.database.table.MenuTable;

/**
 * Database for caching mensas and their menus locally
 * 
 * @author Nicolas Kessler
 *
 */
public class MensaDatabase {

	private SQLiteDatabase database;
	private MensaDatabaseHelper helper;
	
	private static final String SELECT_MENSAS = "select * from " + MensaTable.TABLE_NAME + " order by _id asc";

	public MensaDatabase() {
		helper = new MensaDatabaseHelper();
	}

	/**
	 * Opens the database	
	 * @throws SQLiteException if the database was not opened correctly
	 */
	public void open() throws SQLiteException {
		database = helper.getWritableDatabase();
	}

	/**
	 * Closes the databse
	 */
	public void close() {
		helper.close();
	}

	/**
	 * Stores the mensas and their menu plans to the database
	 * @param mensas List of the mensas that should be stored
	 */
	public void storeMensas(List<Mensa> mensas) {
		for (Mensa m : mensas) {
			storeSingleMensa(m);
			storeMenusOfMensa(m);
		}
	}
	
	/**
	 * Stores the favorite mensas to the database
	 * @param mensas List of the mensas that should be stored
	 */
	public void storeFavorites(List<Mensa> mensas) {
		for (Mensa mensa : mensas)  {
			if (mensa.isFavorite()) {
				ContentValues values = new ContentValues();
				values.put(FavoriteTable.COLUMN_ID, mensa.getId());
				database.insertWithOnConflict(FavoriteTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
			}
			else {
				database.delete(FavoriteTable.TABLE_NAME, "FavoriteTable.COLUMN_ID = " + mensa.getId(), null);
			}
		}
	}

	/**
	 * Loads all mensas, their menu plans and their favorite status fromt he database
	 * @return ArrayList with all the mensas from the database
	 */
	public ArrayList<Mensa> loadAllMensas() {
		ArrayList<Mensa> result = new ArrayList<Mensa>();
		Cursor cursor = database.rawQuery(SELECT_MENSAS, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			result.add(getMensaFromCursor(cursor));
			cursor.moveToNext();
			// TODO load menus
		}
		return result;
	}

	private void storeSingleMensa(Mensa mensa) {
		ContentValues values = new ContentValues();
		values.put(MensaTable.COLUMN_ID, mensa.getId());
		values.put(MensaTable.COLUMN_NAME, mensa.getName());
		values.put(MensaTable.COLUMN_ADDRESS, mensa.getAddress());
		values.put(MensaTable.COLUMN_CITY, mensa.getCity());
		values.put(MensaTable.COLUMN_LAT, mensa.getLat());
		values.put(MensaTable.COLUMN_LON, mensa.getLon());
		database.insertWithOnConflict(MensaTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	private void storeMenusOfMensa(Mensa mensa) {
		ArrayList<Menu> menus = mensa.getWeeklyPlan().getAllMenus();
		for (Menu m : menus) {
			storeSingleMenu(m);
		}
	}
	
	private void storeSingleMenu(Menu menu) {
		ContentValues values = new ContentValues();
		values.put(MenuTable.COLUMN_ID, menu.getMenuID());
		values.put(MenuTable.COLUMN_TITLE, menu.getTitle());
		values.put(MenuTable.COLUMN_DAY, menu.getDay());
		values.put(MenuTable.COLUMN_DATE, menu.getDate());
		values.put(MenuTable.COLUMN_DESC, menu.getDesc());
		values.put(MenuTable.COLUMN_MENSA_ID, menu.getMensaID());
		values.put(MenuTable.COLUMN_WEEK, menu.getWeek());
		values.put(MenuTable.COLUMN_PRICE, menu.getPrice());
		values.put(MenuTable.COLUMN_RATING, menu.getRating());
		database.insertWithOnConflict(MenuTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

	/**
	 * Returns a mensa from the data on which the cursor points.
	 * The cursor itself must not be affected by this!
	 */
	private Mensa getMensaFromCursor(Cursor cursor) {
		BuilderMensa builder = new BuilderMensa();
		builder.setId(cursor.getInt(cursor.getColumnIndex(MensaTable.COLUMN_ID)));
		builder.setName(cursor.getString(cursor.getColumnIndex(MensaTable.COLUMN_NAME)));
		builder.setAddress(cursor.getString(cursor.getColumnIndex(MensaTable.COLUMN_ADDRESS)));
		builder.setCity(cursor.getString(cursor.getColumnIndex(MensaTable.COLUMN_CITY)));
		builder.setLat(cursor.getFloat(cursor.getColumnIndex(MensaTable.COLUMN_LAT)));
		builder.setLon(cursor.getFloat(cursor.getColumnIndex(MensaTable.COLUMN_LON)));
		builder.setIsFavorite(isMensaFavorite(builder.getId()));
		return builder.build();
	}
	
	private boolean isMensaFavorite(int id) {
		String statement = "select * from " + FavoriteTable.TABLE_NAME 
				+ " where " + FavoriteTable.COLUMN_ID + " = " + id + ";";
		Cursor cursor = database.rawQuery(statement, null);
		if (cursor.getCount() == 1) {
			Log.i("Mensa fav", "mensa " + id + " was set as fav");
			return true;
		}
		else {
			Log.i("Mensa not fav", "mensa " + id + " was not set as fav!");
			return false;
		}
	}
}
