package com.mensaunibe.util.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.location.LocationClient;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;

public class TaskLocation extends AsyncTask<Void, Integer, Location> {
	
	// for logging and debugging purposes
	private static final String TAG = TaskLocation.class.getSimpleName();
	
	@SuppressWarnings("unused")
	private Controller mController;
	private DataHandler mDataHandler;
	private MensaList mMensaList;

	private ArrayList<TaskListener> mListeners;
	private LocationClient mLocationClient;

	private Location mLocation;

	
	public TaskLocation(Controller controller) {
		this.mController = controller;
		this.mDataHandler = Controller.getDataHandler();
		this.mMensaList = mDataHandler.getMensaList();
		
		this.mListeners = new ArrayList<TaskListener>();
		this.mLocationClient = Controller.getLocationClient();
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
    		Log.e(TAG, "getLocation(): mLocationClient was null or not connected! Setting default Location");
    		mLocation = new Location("default");
    		mLocation.setLatitude(46.9510);
    		mLocation.setLongitude(7.43820);
    	}
    }
    
    /**
     * loops through all the mensa coordinates to determine the closest mensa
     */
    protected void getClosestMensa() {
    	Log.i(TAG, "getClosestMensa()");
		List<Mensa> mensas = mMensaList.getMensas();
		// check if there is a last location set, possible reason for NullPointer Exception in onConnected()
		if ( mLocation != null ) {
			// initialize the distances array to save all distances in
			Map<Integer, Float> distances = new HashMap<Integer, Float>();
			Map<Integer, Float> favdistances = new HashMap<Integer, Float>();
			for (Mensa mensa : mensas) {
				float distance = getDistance(mLocation.getLatitude(), mLocation.getLongitude(), mensa.getLat(), mensa.getLon());
				// put all distances into the HashMap to calculate the closest mensa later
				distances.put(mensa.getId(), distance);
				// put distances of favorite mensas in a separate hashmap to determine the closest favorite mensa
				if (mensa.isFavorite()) {
					favdistances.put(mensa.getId(), distance);
				}
				// set the distance to that mensa in the model for displaying purposes
				mensa.setDistance(distance);
			}
			// and now find the nearest mensa
			int closestMensaId = 0;
			int closestFavMensaId = 0;
			float smallestDistance = Float.MAX_VALUE;
			float smallestFavDistance = Float.MAX_VALUE;
			
			for (Map.Entry<Integer, Float> entry : distances.entrySet()) {
			    Float value = entry.getValue();
			    if (value < smallestDistance) {
			        closestMensaId = entry.getKey();
			        smallestDistance = value;
			    }
			}
			
			for (Map.Entry<Integer, Float> entry : favdistances.entrySet()) {
			    Float value = entry.getValue();
			    if (value < smallestFavDistance) {
			        closestFavMensaId = entry.getKey();
			        smallestFavDistance = value;
			    }
			}
			
			// and save it in the data handler
			if (mDataHandler == null) {
				Log.e(TAG, "getClosestMensa(): mDataHandler was null!");
				// seems to be null somtimes...dont know why
				mDataHandler = Controller.getDataHandler();
			}
			
			mDataHandler.setClosestMensa(closestMensaId);
			mDataHandler.setClosestFavMensa(closestFavMensaId);
			mDataHandler.setLocationTarget(mMensaList.getMensaById(closestMensaId).getLocation());
		}
    }
 
    /**
     * method to calculated the distance (over air)
     */
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
		
		if (mLocation != null) {
			getClosestMensa();
			return mLocation;
		} else {
    		Log.e(TAG, "getLocation(): mLocation was null! Setting default Location");
    		mLocation = new Location("default");
    		mLocation.setLatitude(46.9510);
    		mLocation.setLongitude(7.43820);
    		getClosestMensa();
			return mLocation;
		}
    }

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