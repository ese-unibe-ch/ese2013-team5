package com.mensaunibe.util.tasks;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.util.ServiceRequestManager;

public class TaskUpdateAPI extends AsyncTask<Void, Integer, String> {
	
	// for logging and debugging purposes
	private static final String TAG = TaskUpdateAPI.class.getSimpleName();
	
	private Controller mController;
	
	private ArrayList<TaskListener> mListeners;

	private String mUrl;
	
	public TaskUpdateAPI(Controller controller, String deviceid) {
		this.mController = controller;
		this.mListeners = new ArrayList<TaskListener>();
		this.mUrl = "http://api.031.be/mensaunibe/getdata/?deviceid=" + deviceid;
	}
	
	public TaskUpdateAPI(Controller controller, String deviceid, String name) {
		this.mController = controller;
		this.mListeners = new ArrayList<TaskListener>();

		try {
			this.mUrl = "http://api.031.be/mensaunibe/getdata/?deviceid=" + deviceid + "&name=" + URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			Log.e(TAG, "Exception: " + ex);
		}
	}
	
	public TaskUpdateAPI(Controller controller, String deviceid, int mensaid) {
		this.mController = controller;
		this.mListeners = new ArrayList<TaskListener>();
		this.mUrl = "http://api.031.be/mensaunibe/getdata/?deviceid=" + deviceid + "&mensaid=" + mensaid;
	}
	
	public TaskUpdateAPI(Controller controller, String deviceid, int menuid, int rating) {
		this.mController = controller;
		this.mListeners = new ArrayList<TaskListener>();
		this.mUrl = "http://api.031.be/mensaunibe/getdata/?deviceid=" + deviceid + "&menuid=" + menuid + "&rating=" + rating;
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
		Log.i(TAG, "Starting background task to save data to the remote API");
		ServiceRequestManager mWebService = new ServiceRequestManager(mController);
		JsonObject jsonObj = mWebService.getJSON(mUrl, 5000);
		return jsonObj.get("status").getAsString();
    }

	@Override
    protected void onPostExecute(String status) {
        this.notifyOnTaskComplete(status);
        this.removeListeners();
    }

    @Override
    protected void onProgressUpdate(Integer... percent) {
        this.notifyOnProgressUpdate(percent[0]);
    }
}