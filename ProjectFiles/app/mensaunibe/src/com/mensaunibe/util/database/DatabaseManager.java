package com.mensaunibe.util.database;

import java.util.ArrayList;
import java.util.List;

import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.app.model.Menu;
import com.mensaunibe.app.model.User;
import com.mensaunibe.app.model.UserFriend;
import com.mensaunibe.app.model.UserNotification;
import com.mensaunibe.util.database.tables.FavoriteTable;
import com.mensaunibe.util.database.tables.FriendTable;
import com.mensaunibe.util.database.tables.MensaTable;
import com.mensaunibe.util.database.tables.MenuTable;
import com.mensaunibe.util.database.tables.NotificationTable;
import com.mensaunibe.util.database.tables.UserTable;

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
	
	public void update(MensaList mensalist){
		mDB.delete(MensaTable.TABLE_NAME, null, null);
		mDB.delete(MenuTable.TABLE_NAME, null, null);
		mDB.delete(FavoriteTable.TABLE_NAME, null, null);
		save(mensalist);
	}
	
	public void save(MensaList mensalist) {
		Log.i(TAG, "save()");
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
		Log.i(TAG, "saveFavorite()");
		ContentValues values = new ContentValues();
		values.put(FavoriteTable.COLUMN_ID, mensa.getId());
		mDB.insertWithOnConflict(FavoriteTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void removeFavorite(Mensa mensa) {
		Log.i(TAG, "removeFavorite()");
		mDB.delete(FavoriteTable.TABLE_NAME, FavoriteTable.COLUMN_ID + " = ?", new String[] { "" + mensa.getId() });
	}
	
	public boolean isFavorite(Mensa mensa) {
		//Log.i(TAG, "isFavorite()");
		return mDBService.isFavorite(mensa, mDB);
	}

	public void save(Mensa m) {
		//Log.i(TAG, "save(Mensa)");
		ContentValues values = mDBService.toValue(m);
		mDB.insertWithOnConflict(MensaTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void save(Menu menu) {
		//Log.i(TAG, "save(Menu)");
		ContentValues values = mDBService.toValue(menu);
		mDB.insertWithOnConflict(MenuTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void save(User user){
		ContentValues values = mDBService.toValue(user);
		mDB.insertWithOnConflict(UserTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		
		List<UserFriend> friendlist = user.getFriendList().getFriends();
		for(UserFriend friend : friendlist){
			save(friend);
		}
		
		List<UserNotification> notifications = user.getNotificationList().getNotifications();
		for(UserNotification notif : notifications){
			save(notif);
		}
	}
	
	public void save(UserNotification notif) {
		ContentValues values = mDBService.toValue(notif);
		mDB.insertWithOnConflict(NotificationTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public void save(UserFriend friend){
		ContentValues values = mDBService.toValue(friend);
		mDB.insertWithOnConflict(FriendTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public MensaList load(){
		Log.i(TAG, "load()");
		return mDBService.createMensalist(mDB);
	}

	public User loadUser(){
		return mDBService.createUser(mDB);
	}
}
