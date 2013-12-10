package com.mensaunibe.util.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.app.model.Menu;
import com.mensaunibe.app.model.MenuList;
import com.mensaunibe.util.database.tables.FavoriteTable;
import com.mensaunibe.util.database.tables.MensaTable;
import com.mensaunibe.util.database.tables.MenuTable;

/**
 * helper class for the DatabaseManager.
 * consists methods to convert objects into storable values and vice versa
 */
public class DatabaseService {
	
	private final String SELECT_MENSAS = "select * from " + MensaTable.TABLE_NAME + " order by _id asc";

	/**
	 * @param m: Mensa to convert into storable values
	 * @return ContentValues which can be stored in the Database
	 */
	public ContentValues toValue(Mensa m) {
		ContentValues values = new ContentValues();
		values.put(MensaTable.COLUMN_ID, m.getId());
		values.put(MensaTable.COLUMN_NAME, m.getName("de"));
		values.put(MensaTable.COLUMN_NAME_EN, m.getName("en"));
		values.put(MensaTable.COLUMN_DESC, m.getDesc("de"));
		values.put(MensaTable.COLUMN_DESC_EN, m.getDesc("en"));
		values.put(MensaTable.COLUMN_ADDRESS, m.getAddress());
		values.put(MensaTable.COLUMN_CITY, m.getCity());
		values.put(MensaTable.COLUMN_LAT, m.getLat());
		values.put(MensaTable.COLUMN_LON, m.getLon());
		return values;
	}
	
	/**
	 * @param m: Menu to convert into storable values
	 * @return ContentValues which can be stored in the Database
	 */
	public ContentValues toValue(Menu m){
		ContentValues values = new ContentValues();
		values.put(MenuTable.COLUMN_MENSA_ID, m.getMensaID());
		values.put(MenuTable.COLUMN_DATE, m.getDate("de"));
		values.put(MenuTable.COLUMN_DATE_EN, m.getDate("en"));
		values.put(MenuTable.COLUMN_DAY, m.getDay());
		values.put(MenuTable.COLUMN_DESC, m.getDesc("de"));
		values.put(MenuTable.COLUMN_DESC_EN, m.getDesc("en"));
		values.put(MenuTable.COLUMN_ID, m.getMenuID());
		values.put(MenuTable.COLUMN_PRICE, m.getPrice("de"));
		values.put(MenuTable.COLUMN_PRICE_EN, m.getPrice("en"));
		values.put(MenuTable.COLUMN_RATING, m.getRating());
		values.put(MenuTable.COLUMN_VOTES, m.getVotes());
		values.put(MenuTable.COLUMN_TITLE, m.getTitle("de"));
		values.put(MenuTable.COLUMN_TITLE_EN, m.getTitle("en"));
		values.put(MenuTable.COLUMN_TYPE, m.getType());
		values.put(MenuTable.COLUMN_WEEK, m.getWeek());
		return values;
	}
	
	/**
	 * creates the whole model as a MensaList from the database
	 * @param db
	 * database from which the model is created
	 * @return
	 * the model as a MensaList
	 */
	public MensaList createMensalist(SQLiteDatabase db) {
		List<Mensa> list = new ArrayList<Mensa>();
		Cursor cursor = db.rawQuery(SELECT_MENSAS, null);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()) {
			list.add(getMensaFromCursor(cursor, db));
			cursor.moveToNext();
		}
		addMenus(list, db);
		
		return new MensaList(list);
	}

	/**
	 * fetches all menus from the database and stores them in the mensalist's mensa objects
	 * @param mensalist list of mensas to be inflated
	 * @param db database where the menus are stored
	 */
	private void addMenus(List<Mensa> mensalist, SQLiteDatabase db) {
		for(Mensa mensa : mensalist) {
			List<Menu> menulist = new ArrayList<Menu>();
			final String SELECT_MENUS = "select * from " 
					+ MenuTable.TABLE_NAME 
					+ " where " + MenuTable.COLUMN_MENSA_ID 
					+ " = " + mensa.getId();
			Cursor cursor = db.rawQuery(SELECT_MENUS, null);
			cursor.moveToFirst();
			
			while(!cursor.isAfterLast()) {
				menulist.add(getMenuFromCursor(cursor));
				cursor.moveToNext();
			}
			mensa.setMenuList(new MenuList(menulist));
		}
	}

	/**
	 * Returns a menu from the data on which the cursor points.
	 * The cursor itself must not be affected by this!
	 */
	private Menu getMenuFromCursor(Cursor cursor) {
		int id = cursor.getInt(cursor.getColumnIndex(MenuTable.COLUMN_ID));
		int mensaid = cursor.getInt(cursor.getColumnIndex(MenuTable.COLUMN_MENSA_ID));
		String title = cursor.getString(cursor.getColumnIndex(MenuTable.COLUMN_TITLE));
		String title_en	= cursor.getString(cursor.getColumnIndex(MenuTable.COLUMN_TITLE_EN));
		String type	= cursor.getString(cursor.getColumnIndex(MenuTable.COLUMN_TYPE));
		String desc	= cursor.getString(cursor.getColumnIndex(MenuTable.COLUMN_DESC));
		String desc_en = cursor.getString(cursor.getColumnIndex(MenuTable.COLUMN_DESC_EN));
		String price = cursor.getString(cursor.getColumnIndex(MenuTable.COLUMN_PRICE));
		String price_en = cursor.getString(cursor.getColumnIndex(MenuTable.COLUMN_PRICE_EN));	
		String date = cursor.getString(cursor.getColumnIndex(MenuTable.COLUMN_DATE));
		String date_en = cursor.getString(cursor.getColumnIndex(MenuTable.COLUMN_DATE_EN));
		String day = cursor.getString(cursor.getColumnIndex(MenuTable.COLUMN_DAY));
		int week = cursor.getInt(cursor.getColumnIndex(MenuTable.COLUMN_WEEK));
		Double rating = cursor.getDouble(cursor.getColumnIndex(MenuTable.COLUMN_RATING));
		int votes = cursor.getInt(cursor.getColumnIndex(MenuTable.COLUMN_VOTES));
		
		return new Menu(id, mensaid, title, title_en, type, desc, desc_en, price, price_en, date, date_en, day, week, rating, votes);
	}

	/**
	 * Returns a mensa from the data on which the cursor points.
	 * The cursor itself must not be affected by this!
	 */
	private Mensa getMensaFromCursor(Cursor cursor, SQLiteDatabase db) {
		int id = cursor.getInt(cursor.getColumnIndex(MensaTable.COLUMN_ID));
		String name = cursor.getString(cursor.getColumnIndex(MensaTable.COLUMN_NAME));
		String name_en = cursor.getString(cursor.getColumnIndex(MensaTable.COLUMN_NAME_EN));
		String desc = cursor.getString(cursor.getColumnIndex(MensaTable.COLUMN_DESC));
		String desc_en = cursor.getString(cursor.getColumnIndex(MensaTable.COLUMN_DESC_EN));
		String address = cursor.getString(cursor.getColumnIndex(MensaTable.COLUMN_CITY));
		String city = cursor.getString(cursor.getColumnIndex(MensaTable.COLUMN_CITY));
		float lat = cursor.getFloat(cursor.getColumnIndex(MensaTable.COLUMN_LAT));
		float lon = cursor.getFloat(cursor.getColumnIndex(MensaTable.COLUMN_LON));
		Mensa mensa = new Mensa(id, name, name_en, desc, desc_en, address, city, lat, lon, null);
		mensa.setFavorite(isFavorite(mensa, db));
		
		return mensa;
	}

	public boolean isFavorite(Mensa mensa, SQLiteDatabase mDB) {
		final String SELECT_FAVORITES = "select * from " 
				+ FavoriteTable.TABLE_NAME 
				+ " where " + FavoriteTable.COLUMN_ID 
				+ " = " + mensa.getId();
		Cursor cursor = mDB.rawQuery(SELECT_FAVORITES, null);
		cursor.moveToFirst();
		
		if (cursor.getCount() == 0) {
			return false;
		} else {
			return true;
		}
	}
}
