package com.mensaunibe.util.tasks;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mensaunibe.app.controller.ActivityMain;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.util.ServiceWebRequest;
import com.mensaunibe.util.database.DatabaseManager;

public class TaskCreateModel extends AsyncTask<Void, Integer, MensaList> {
	
	// for logging and debugging purposes
	private static final String TAG = TaskCreateModel.class.getSimpleName();
	
	private ActivityMain mController;
	private Fragment mCallerFragment;
	
	private ArrayList<TaskListener> mListeners;
	private ServiceWebRequest mWebService;
	private DatabaseManager mDBManager;

	
	public TaskCreateModel(ActivityMain controller, Fragment fragment) {
		this.mController = controller;
		this.mDBManager = new DatabaseManager(controller);
		this.mListeners = new ArrayList<TaskListener>();
		this.mWebService = new ServiceWebRequest(mController);
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
		if(mWebService.hasConnection(2000)){
			final JsonObject jsonObj = mWebService.getJSON("http://api.031.be/mensaunibe/v1/?type=mensafull", 5000);
    		model = new Gson().fromJson(jsonObj, MensaList.class);

    		//show fake progress
    		for (int i = 0; i <= 100; i++) {
            	try {
                	Thread.sleep(10);
                	this.publishProgress(i);
            	} catch (InterruptedException e) {
                	return null;
            	}
        	}
		}else{
			model = mDBManager.load();
		}
    	return model;
    }

    @Override
    protected void onPostExecute(MensaList model) {
        this.notifyOnTaskComplete(model);
        this.removeListeners();
    }

    @Override
    protected void onProgressUpdate(Integer... percent) {
        this.notifyOnProgressUpdate(percent[0]);
    }
}