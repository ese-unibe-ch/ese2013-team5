package com.ese2013.mensaunibe.util.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.util.BuilderMensa;
import com.ese2013.mensaunibe.util.database.table.FavoriteTable;
import com.ese2013.mensaunibe.util.database.table.MensaTable;

/**
 * Database for caching mensas and their menus locally
 * 
 * @author Nicolas Kessler
 *
 */
public class MensaDatabase {

	private SQLiteDatabase database;
	private MensaDatabaseHelper helper;
	
	private static final String SELECT_MENSAS = "select * from " + MensaTable.TABLE_MENSA + " order by _id asc";

	public MensaDatabase() {
		helper = new MensaDatabaseHelper();
	}

	public void open() throws SQLiteException {
		database = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

	public void storeMensas(List<Mensa> mensas) {
		for (Mensa m : mensas) {
			storeSingleMensa(m);
			storeMenusOfMensa(m);
		}
	}

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
		database.insertWithOnConflict(MensaTable.TABLE_MENSA, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		values.clear();
		values.put(FavoriteTable.COLUMN_ID, mensa.getId());
		values.put(FavoriteTable.COLUMN_FAVORITE, mensa.isFavorite() ? 0 : 1);
		database.insertWithOnConflict(FavoriteTable.TABLE_FAVORITE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	private void storeMenusOfMensa(Mensa mensa) {
		// TODO implement method
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
		String statement = "select * from " + FavoriteTable.TABLE_FAVORITE 
				+ " where " + FavoriteTable.COLUMN_ID + " = " + id 
				+ " and " + FavoriteTable.COLUMN_FAVORITE + " > 0";
		Cursor cursor = database.rawQuery(statement, null);
		if (cursor.getCount() == 1)
			return true;
		else
			return false;
	}
}
