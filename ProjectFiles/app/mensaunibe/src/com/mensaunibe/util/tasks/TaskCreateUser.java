package com.mensaunibe.util.tasks;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.app.model.User;
import com.mensaunibe.app.model.UserFriend;
import com.mensaunibe.app.model.UserFriendList;
import com.mensaunibe.app.model.UserNotification;
import com.mensaunibe.app.model.UserNotificationList;
import com.mensaunibe.util.ServicePrefsManager;
import com.mensaunibe.util.ServiceWebRequest;
import com.mensaunibe.util.database.DatabaseManager;

public class TaskCreateUser extends AsyncTask<Void, Integer, User>{

	// for logging and debugging purposes
	private static final String TAG = TaskCreateUser.class.getSimpleName();
	
	@SuppressWarnings("unused")
	private Controller mController;
	private DataHandler mDataHandler;
		
	private ArrayList<TaskListener> mListeners;
	private ServiceWebRequest mWebService;
	private ServicePrefsManager mPersistenceService;
	private DatabaseManager mDBManager;
	
	public TaskCreateUser(Controller controller, Fragment fragment){
		this.mController = controller;
		this.mDataHandler = Controller.getDataHandler();
		this.mWebService = mDataHandler.getWebService();
		this.mPersistenceService = mDataHandler.getPersistenceManager();
		this.mDBManager = mDataHandler.getDatabaseManager();
		this.mListeners = new ArrayList<TaskListener>();
	}
	
	public void addListener(TaskListener listener) {
		Log.i(TAG, "addListener(" + listener + ")");
		if (listener != null) {
			mListeners.add(listener);
	    } else {
	    	Log.e(TAG, "Listener was null!");
	    }
	}
	
	public void removeListener(TaskListener listener) {
		Log.i(TAG, "removeListener(" + listener + ")");
		mListeners.remove(listener);
	}
	
	public void removeListeners() {
    	Log.i(TAG, "removeListeners()");
    	mListeners.removeAll(mListeners);
    }
	
	protected void notifyOnTaskComplete(Object result) {
    	Log.i(TAG, "notifyOnTaskComplete(" + result + ")");
    	for (TaskListener mListener : mListeners) {
    		mListener.onTaskComplete(result);
    	}
    }
	
	protected void notifyOnProgressUpdate(int percent) {
        for (TaskListener mListener : mListeners) {
            mListener.onProgressUpdate(percent);
        }
    }
	
	@Override
	protected User doInBackground(Void... arg0) {
		Log.i(TAG, "Starting background task to create the (user)model");
		User userModel = null;
		UserFriendList friendList = null;
		UserNotificationList notifications = null;
		final Gson gson = new Gson();
		JsonObject jsonObj = null;
		
		
		Log.i(TAG, "trying to load JSON from API");
		jsonObj = mWebService.getJSON("http://api.031.be/mensaunibe/v1/?type=user&deviceid=ffffffff-c97a-166c-ffff-ffffeabf4809&prettyprint=1", 5000);
		
		
		if(jsonObj != null){
			userModel = createUserFromJson(gson, jsonObj);
		} else {
			userModel = createUserFromSharedPrefs(gson, userModel);
		}
		
		// if the model still is null, a lot went wrong, now we try to load the data from the slower
		// (than shared prefs, which sit in the device RAM) database
		if (userModel == null) {
			Log.e(TAG, "User still null after loading from prefs, trying the database!");	
			userModel = mDBManager.loadUser();
		}
		
		//show fake progress
		for (int i = 0; i <= 100; i++) {
        	try {
            	Thread.sleep(10);
            	publishProgress(i);
        	} catch (InterruptedException e) {
            	return null;
        	}
    	}
		
		return userModel;
	}

	private User createUserFromSharedPrefs(Gson gson, User user) {
		Log.e(TAG, "JSONObject from webservice was null, trying to load from shared prefs");
		JsonObject jsonObj;
		String jsonStr = (String) mPersistenceService.getData("string", "user");
		if (jsonStr != null) {
			jsonObj = new JsonParser().parse(jsonStr).getAsJsonObject();
			user = gson.fromJson(jsonObj, User.class);
		}
		return user;
	}

	private User createUserFromJson(Gson gson, JsonObject jsonObj) {
		Log.i(TAG, "JSONObject from webservice successfully loaded");
		User user;
		user = gson.fromJson(jsonObj, User.class);
		
		//persist the json in shared prefs
		mPersistenceService.setData("string", "user", jsonObj.toString());
		//mPersistenceService.setData("integer", "lastupdate", jsonObj.get("lastupdate").getAsInt());
		return user;
	}

	private UserNotificationList createNotificationsFromJson(Gson gson, JsonObject jsonObj) {
		UserNotificationList notifications;
		notifications = gson.fromJson(jsonObj, UserNotificationList.class);
		
		return notifications;
	}

	private UserFriendList createFriendListFromJson(Gson gson, JsonObject jsonObj) {
		UserFriendList friendlist;
		friendlist = gson.fromJson(jsonObj, UserFriendList.class);
		return friendlist;
	}
	
	@Override
    protected void onPostExecute(User user) {
        notifyOnTaskComplete(user);
        removeListeners();
    }

    @Override
    protected void onProgressUpdate(Integer... percent) {
        notifyOnProgressUpdate(percent[0]);
    }

}
