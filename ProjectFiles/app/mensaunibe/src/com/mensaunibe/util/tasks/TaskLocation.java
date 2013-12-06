package com.mensaunibe.util.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationClient;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;

public class TaskLocation extends AsyncTask<Void, Integer, Location> {
	
	// for logging and debugging purposes
	private static final String TAG = TaskLocation.class.getSimpleName();
	
	private Controller mController;
	private DataHandler mDataHandler;
	private MensaList mModel;

	private ArrayList<TaskListener> mListeners;
	private LocationClient mLocationClient;

	private Location mLocation;

	
	public TaskLocation(Controller controller) {
		this.mController = controller;
		this.mDataHandler = Controller.getDataHandler();
		this.mModel = mDataHandler.getModel();
		
		this.mListeners = new ArrayList<TaskListener>();
		this.mLocationClient = mController.getLocationClient();
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
    	Log.i(TAG, "notifyOnTaskComplete()");
        for (TaskListener mListener : mListeners) {
            mListener.onTaskComplete(result);
        }
    }

    protected void notifyOnProgressUpdate(int percent) {
        for (TaskListener mListener : mListeners) {
            mListener.onProgressUpdate(percent);
        }
    }
    
    protected void getLocation() {
    	Log.i(TAG, "getLocation()");
    	if (mLocationClient != null && mLocationClient.isConnected()) {
    		mLocation = mLocationClient.getLastLocation();
    	} else {
    		Log.e(TAG, "getLocation(): mLocationClient was null or not connected!");
    	}
    }
    
    protected void getClosestMensa() {
    	Log.i(TAG, "getClosestMensa()");
		// loop trough all the mansa coordinates and determine the closest mensa
		// fist get all the mensas in a list to loop over
		List<Mensa> mensas = mModel.getMensas();
		// check if there is a last location set, possible reason for NullPointer Exception in onConnected()
		if ( mLocation != null ) {
			// initialize the distances array to save all distances in
			Map<Integer, Float> distances = new HashMap<Integer, Float>();
			for (Mensa mensa : mensas) {
				distances.put(mensa.getId(), getDistance(mLocation.getLatitude(), mLocation.getLongitude(), mensa.getLat(), mensa.getLon()));
			}
			// make the distances globally available, just for convenience
//			this.distances = distances;
			// and now find the nearest mensa
			int closestMensaId = 0;
			float smallestDistance = Float.MAX_VALUE;
			for (Map.Entry<Integer, Float> entry : distances.entrySet()) {
			    Float value = entry.getValue();
			    if (value < smallestDistance) {
			        closestMensaId = entry.getKey();
			        smallestDistance = value;
			    }
			}
			
			// and save it in the data handler
			if (mDataHandler == null) {
				Log.e(TAG, "mDataHandler was null!");
				// seems to be null somtimes...dont know why
				mDataHandler = Controller.getDataHandler();
			}
			mDataHandler.setClosestMensa(closestMensaId);
			mDataHandler.setLocationTarget(mModel.getMensaById(closestMensaId).getLocation());
			// also make this globally available for convenience and fetch the mensa object
//			this.closestMensaId = closestMensaId;
//			this.nearestmensa = model.getMensaById(closestMensaId);
	//		Toast.makeText(this, "current location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
//			Toast.makeText(this, "closest mensa: " + closestMensa.getName(), Toast.LENGTH_SHORT).show();
			if ( smallestDistance <= 100 ) {
				// 100 is too big, should probably be smaller than 50 or less
				// here we would save the mensaid to the users profile on the server
				// then we can either show his friends that are in that mensa too
				// or the total number people in that mensa, or both...
//				updateUserMensa();
//				Toast.makeText(this, "mensa " + smallestdistance + "m away, are you in there?", Toast.LENGTH_SHORT).show();
			}
			// a very ugly and hacky way to show the closest mensa on the home screen
			// this should only be done when no favorite mensas are set, but the logic for that could be in the start fragment
//			selectItem(0);
		} else {
			Toast.makeText(mController, "last location was null, did the app crash here earlier?", Toast.LENGTH_SHORT).show();
		}
    }
    
	// this method stupidly calculates the air distance between two points, but should be enough to determine the closest mensa
	public static float getDistance(double startLat, double startLon, double endLat, double endLon){
	    float[] resultArray = new float[99];
	    Location.distanceBetween(startLat, startLon, endLat, endLon, resultArray);
	    return resultArray[0];
	}
	
	@Override
    protected void onPreExecute() {
		Log.i(TAG, "onPreExecute()");
	}
	
	@Override
    protected Location doInBackground(Void... params) {
		Log.i(TAG, "Starting background task to get the latest location");

		getLocation();
		
    	// show fake progress
//		while (mLocation == null) {
//			Log.i(TAG, "Entering progress loop");
//	    	for (int i = 0; i <= 100; i++) {
//	    		if (mLocation != null) {
//	    			Log.i(TAG, "Received location");
//	    			try {
//		                Thread.sleep(10/10);
//		                this.publishProgress(i);
//		            } catch (InterruptedException e) {
//		                return null;
//		            }
//	    		} else {
//		    		try {
//		                Thread.sleep(10);
//		                this.publishProgress(i);
//		            } catch (InterruptedException e) {
//		                return null;
//		            }
//	    		}
//	        }
//		}
		
		if (mLocation != null) {
			getClosestMensa();
			return mLocation;
		} else {
			Log.e(TAG, "mLocation was null!");
			return null;
		}
    }

	// async task callbacks
    @Override
    protected void onPostExecute(Location location) {
        this.notifyOnTaskComplete(location);
        this.removeListeners();
    }

    @Override
    protected void onProgressUpdate(Integer... percent) {
        this.notifyOnProgressUpdate(percent[0]);
    }
}