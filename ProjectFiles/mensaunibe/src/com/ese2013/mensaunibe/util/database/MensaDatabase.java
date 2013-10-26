package com.ese2013.mensaunibe.util.database;

import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.ese2013.mensaunibe.model.Mensa;
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
		// TODO implement method
		for (Mensa m : mensas) {
			storeSingleMensa(m);
		}
	}

	public List<Mensa> loadAllMensas() {
		// TODO implement method
		return null;
	}

	private void storeSingleMensa(Mensa mensa) {
		ContentValues values = new ContentValues();
		values.put(MensaTable.COLUMN_ID, mensa.getId());
		values.put(MensaTable.COLUMN_NAME, mensa.getName());
		values.put(MensaTable.COLUMN_ADDRESS, mensa.getAddress());
		values.put(MensaTable.COLUMN_CITY, mensa.getCity());
		values.put(MensaTable.COLUMN_LAT, mensa.getLat());
		values.put(MensaTable.COLUMN_LON, mensa.getLon());
		values.put(MensaTable.COLUMN_FAVORITE, mensa.isFavorite() ? 0 : 1);
		database.insertWithOnConflict(MensaTable.TABLE_MENSA, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	private void storeMenusOfMensa(Mensa mensa) {
		// TODO implement method
	}
}
