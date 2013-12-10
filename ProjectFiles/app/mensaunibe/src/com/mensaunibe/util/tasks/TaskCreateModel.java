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
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.util.ServiceSettingsManager;
import com.mensaunibe.util.ServiceRequestManager;
import com.mensaunibe.util.database.DatabaseManager;

public class TaskCreateModel extends AsyncTask<Void, Integer, MensaList> {
	
	// for logging and debugging purposes
	private static final String TAG = TaskCreateModel.class.getSimpleName();
	
	@SuppressWarnings("unused")
	private Controller mController;
	private DataHandler mDataHandler;
	
	private ArrayList<TaskListener> mListeners;
	private ServiceRequestManager mRequestManager;
	private ServiceSettingsManager mSettingsManager;
	private DatabaseManager mDBManager;

	
	public TaskCreateModel(Controller controller, Fragment fragment) {
		this.mController = controller;
		this.mDataHandler = Controller.getDataHandler();
		this.mRequestManager = mDataHandler.getRequestManager();
		this.mSettingsManager = mDataHandler.getSettingsManager();
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
    protected MensaList doInBackground(Void... params) {
		Log.i(TAG, "Starting background task to create the model");
		MensaList model = null;
		JsonObject jsonObj = null;
		final Gson gson = new Gson();
		
		if (mRequestManager.getLastUpdate() != (Integer) mSettingsManager.getData("integer", "lastupdate")) {
			Log.i(TAG, "Update necessary, trying to load JSON from API");
			jsonObj = mRequestManager.getJSON("http://api.031.be/mensaunibe/v1/?type=mensafull", 5000);
		} else {
			Log.e(TAG, "No update necessary, trying to load from shared prefs");
		}
		// TODO: test the model creation from shared prefs
		//jsonObj = null;
		
		if (jsonObj != null) {
			Log.i(TAG, "JSONObject from webservice successfuly loaded");
    		model = gson.fromJson(jsonObj, MensaList.class);
    		setFavorites(model);
    		
	    	//persist the json in shared prefs
	    	mSettingsManager.setData("string", "model", jsonObj.toString());
	    	
	    	// get the lastupate timestamp and persist it too
	    	mSettingsManager.setData("integer", "lastupdate", jsonObj.get("lastupdatetimestamp").getAsInt());
	    	
	    	// inform the controller that a new model with updated data was fetched (to re-save the db)
	    	Controller.setModelUpdated(true);
		} else {
			Log.e(TAG, "JSONObject is null until now, either because of no connection or because no update is necessary, trying to load from shared prefs");
			// try to get the model from the persisted shared prefs json
			String jsonStr = (String) mSettingsManager.getData("string", "model");
			if (jsonStr != null) {
				jsonObj = new JsonParser().parse(jsonStr).getAsJsonObject();
				model = gson.fromJson(jsonObj, MensaList.class);
	    		setFavorites(model);
			}
		}
		
		// TODO: test the model creation from db
		//model = null;
		
		if (model == null) {
			Log.e(TAG, "Model still null after loading from prefs, trying the database!");
			// if the model still is null, a lot went wrong, now we try to load the data from the slower
			// (than shared prefs, which sit in the device RAM) database
			model = mDBManager.load();
			
    		//show fake progress
    		for (int i = 0; i <= 100; i++) {
            	try {
                	Thread.sleep(10);
                	publishProgress(i);
            	} catch (InterruptedException e) {
                	return null;
            	}
        	}
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
		
    	return model;
    }

    private void setFavorites(MensaList model) {
    	List<Mensa> mensalist = model.getMensas();
    	for (Mensa mensa : mensalist) {
    		mensa.setFavorite(mDBManager.isFavorite(mensa));
    	}
	}

	@Override
    protected void onPostExecute(MensaList model) {
        notifyOnTaskComplete(model);
        removeListeners();
    }

    @Override
    protected void onProgressUpdate(Integer... percent) {
        notifyOnProgressUpdate(percent[0]);
    }
}