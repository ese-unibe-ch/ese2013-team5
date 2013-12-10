package com.mensaunibe.util.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.app.model.Menu;
import com.mensaunibe.app.model.MenuList;
import com.mensaunibe.app.model.User;
import com.mensaunibe.app.model.UserFriend;
import com.mensaunibe.app.model.UserFriendList;
import com.mensaunibe.app.model.UserNotification;
import com.mensaunibe.app.model.UserNotificationList;
import com.mensaunibe.util.database.tables.FavoriteTable;
import com.mensaunibe.util.database.tables.FriendTable;
import com.mensaunibe.util.database.tables.MensaTable;
import com.mensaunibe.util.database.tables.MenuTable;
import com.mensaunibe.util.database.tables.NotificationTable;
import com.mensaunibe.util.database.tables.UserTable;

/**
 * helper class for the DatabaseManager.
 * consists methods to convert objects into storable values and vice versa
 */
public class DatabaseService {
	
	private final String SELECT_MENSAS = "select * from " + MensaTable.TABLE_NAME + " order by _id asc";
	private final String SELECT_FRIENDS = "select * from " + FriendTable.TABLE_NAME + " order by _id asc";
	private final String SELECT_NOTIFICATIONS = "select * from " + NotificationTable.TABLE_NAME + " order by _id asc";
	private final String SELECT_USER = "select * from " + UserTable.TABLE_NAME;
	
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
		values.put(MenuTable.COLUMN_VOTES, m.getVotes());
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
	

	public User createUser(SQLiteDatabase mDB) {
		Cursor cursor = mDB.rawQuery(SELECT_USER, null);
		cursor.moveToFirst();
		if(cursor.getCount() > 1) {
			Log.e(DatabaseService.class.getName(), "more than one user is stored in the database");
		}
		if(cursor.getCount() == 0){
			Log.e(DatabaseService.class.getName(), "cursor count is 0");
		}
		int id = cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_ID));
		int mensaId = cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_MENSAID));
		String deviceId = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_DEVICEID));
		String name = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_NAME));
		int lastUpdate = cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_LASTUPDATE));
		UserFriendList friendlist = new UserFriendList(createFriendList(mDB));
		UserNotificationList notifications = new UserNotificationList(createNotifications(mDB));
		return new User(id, mensaId, deviceId, name, lastUpdate, friendlist, notifications);
	}

	private List<UserNotification> createNotifications(SQLiteDatabase mDB) {
		List<UserNotification> notifications = new ArrayList<UserNotification>();
		Cursor cursor = mDB.rawQuery(SELECT_NOTIFICATIONS, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			notifications.add(getNotificationFromCursor(cursor));
			cursor.moveToNext();
		}
		return notifications;
	}

	private List<UserFriend> createFriendList(SQLiteDatabase mDB) {
		List<UserFriend> friendlist = new ArrayList<UserFriend>();
		Cursor cursor = mDB.rawQuery(SELECT_FRIENDS, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			friendlist.add(getFriendFromCursor(cursor));
			cursor.moveToNext();
		}
		return friendlist;
	}

	private UserNotification getNotificationFromCursor(Cursor cursor) {
		int id = cursor.getInt(cursor.getColumnIndex(NotificationTable.COLUMN_ID));
		int fromid = cursor.getInt(cursor.getColumnIndex(NotificationTable.COLUMN_FROMID));
		String from = cursor.getString(cursor.getColumnIndex(NotificationTable.COLUMN_FROM));
		String date = cursor.getString(cursor.getColumnIndex(NotificationTable.COLUMN_DATE));
		String message = cursor.getString(cursor.getColumnIndex(NotificationTable.COLUMN_MESSAGE));
		int read = cursor.getInt(cursor.getColumnIndex(NotificationTable.COLUMN_READ));
		return new UserNotification(id, from, fromid, date, message, read);
	}

	private UserFriend getFriendFromCursor(Cursor cursor) {
		int id = cursor.getInt(cursor.getColumnIndex(FriendTable.COLUMN_ID));
		int mensaId = cursor.getInt(cursor.getColumnIndex(FriendTable.COLUMN_MENSAID));
		String name = cursor.getString(cursor.getColumnIndex(FriendTable.COLUMN_NAME));
		return new UserFriend(id, mensaId, name);
	}

	public ContentValues toValue(User user) {
		ContentValues values = new ContentValues();
		values.put(UserTable.COLUMN_ID, user.getId());
		values.put(UserTable.COLUMN_MENSAID, user.getMensaId());
		values.put(UserTable.COLUMN_DEVICEID, user.getDeviceId());
		values.put(UserTable.COLUMN_NAME, user.getName());
		values.put(UserTable.COLUMN_LASTUPDATE, user.getLastUpdate());
		return values;
	}

	public ContentValues toValue(UserNotification notif) {
		ContentValues values = new ContentValues();
		values.put(NotificationTable.COLUMN_ID, notif.getId());
		values.put(NotificationTable.COLUMN_FROM, notif.getFrom());
		values.put(NotificationTable.COLUMN_FROMID, notif.getFromID());
		values.put(NotificationTable.COLUMN_DATE, notif.getDate());
		values.put(NotificationTable.COLUMN_MESSAGE, notif.getMessage());
		values.put(NotificationTable.COLUMN_READ, notif.getRead());
		return values;
	}

	public ContentValues toValue(UserFriend friend) {
		ContentValues values = new ContentValues();
		values.put(FriendTable.COLUMN_ID, friend.getFriendID());
		values.put(FriendTable.COLUMN_MENSAID, friend.getMensaID());
		values.put(FriendTable.COLUMN_NAME, friend.getName());
		return values;
	}

}
