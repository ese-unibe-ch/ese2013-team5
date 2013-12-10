package com.mensaunibe.util.tasks;

import java.util.ArrayList;
import android.os.AsyncTask;
import android.util.Log;

import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.util.database.DatabaseManager;

public class TaskUpdateDB extends AsyncTask<Void, Integer, String> {
	
	// for logging and debugging purposes
	private static final String TAG = TaskUpdateDB.class.getSimpleName();
	
	@SuppressWarnings("unused")
	private Controller mController;
	private DataHandler mDataHandler;
	
	private DatabaseManager mDBManager;
	private ArrayList<TaskListener> mListeners;

	
	public TaskUpdateDB(Controller controller) {
		this.mController = controller;
		this.mDataHandler = Controller.getDataHandler();
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
	protected String doInBackground(Void... params) {
		Log.i(TAG, "Starting background task to save the model to the database");
		
		if (mDataHandler.hasModel()) {
			mDBManager.save(mDataHandler.getMensaList());
			
			//show fake progress
			for (int i = 0; i <= 100; i++) {
				try {
					Thread.sleep(10);
					this.publishProgress(i);
				} catch (InterruptedException e) {
					return null;
				}
			}
			
			return "Successfully wrote model to the database!";
		} else {
			Log.e(TAG, "DataHandler didn't have model while trying to save one to the database");
		}
		
		return "Something went wrong when trying to save the model to the database";
	}

	@Override
	protected void onPostExecute(String result) {
		this.notifyOnTaskComplete(result);
		this.removeListeners();
	}

	@Override
	protected void onProgressUpdate(Integer... percent) {
		this.notifyOnProgressUpdate(percent[0]);
	}
}