package com.mensaunibe.app.model;

import java.util.UUID;

import com.google.android.gms.maps.model.LatLng;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.util.ServicePrefsManager;
import com.mensaunibe.util.ServiceWebRequest;
import com.mensaunibe.util.database.DatabaseManager;
import com.mensaunibe.util.tasks.TaskCreateModel;
import com.mensaunibe.util.tasks.TaskListener;
import com.mensaunibe.util.tasks.TaskLocation;
import com.mensaunibe.util.tasks.TaskUpdateAPI;
import com.mensaunibe.util.tasks.TaskUpdateDB;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;


/**
 * This fragment persists over the activity lifecycle and retains
 * the dynamic data for the application.
 * The reason for this is, that the Main Activity is not the place
 * where model data should be stored as it gets destroyed by the
 * system when another app needs the memory or some kind of config change happens
 */
public class DataHandler extends Fragment implements TaskListener {
	
	// for logging and debugging purposes
	private static final String TAG = DataHandler.class.getSimpleName();
	
	private static Controller mController;
	private static ServicePrefsManager mPersistenceManager;
	private static DatabaseManager mDBManager;
	private static ServiceWebRequest mWebService;

	private MensaList mModel;
	
	private Location mLocation;
	private LatLng mLocationTarget;
	private Mensa mClosestMensa;
	private Mensa mCurrentMensa;

	private static String mDeviceId;
	
	
/**
 * 
 * @param controller; takes the ActivityMain as parameter
 * @return instance of the DataHandler
 */
	public static DataHandler newInstance(Controller controller) {
		Log.i(TAG, "newInstance()");
		DataHandler instance = new DataHandler();

		mController = controller;
		mPersistenceManager = new ServicePrefsManager(mController);
		mDBManager = new DatabaseManager(mController);
		mWebService = new ServiceWebRequest(mController);
		
		mDeviceId = getDeviceId();
		
		// Keep this Fragment even during config changes
		instance.setRetainInstance(true);
		
		return instance;
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    Log.i(TAG, "onActivityCreated(Bundle)");
	    super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
	    Log.i(TAG, "onStart()");
	    super.onStart();
	}

	@Override
	public void onResume() {
	    Log.i(TAG, "onResume()");
	    super.onResume();
	}

	@Override
	public void onPause() {
	    Log.i(TAG, "onPause()");
	    super.onPause();
	}

	@Override
	public void onStop() {
	    Log.i(TAG, "onStop()");
	    super.onStop();
	}
	
    // implementations of the TaskListener methods
	@Override
	public void onTaskComplete(Object result) {
		Log.i(TAG, "onTaskComplete(" + result + ")");
		if (result != null) {
			if (result instanceof MensaList) {
				mModel = (MensaList) result;
			} else if (result instanceof Location) {
				mLocation = (Location) result;
			} else if (result instanceof String) {
				Log.i(TAG, (String) result);
			}
		} else {
			Log.e(TAG, "onTaskComplete(): result was null!");
		}
	}

	@Override
	public void onProgressUpdate(int percent) {
		// unused
	}

	@Override
	public void onRendered() {
		// unused
	}
	
	public void setClosestMensa(int mensaid) {
		Log.i(TAG, "setClosestMensa(" + mensaid + ")");
		mClosestMensa = mModel.getMensaById(mensaid);
	}
	
	public Mensa getClosestMensa() {
		Log.i(TAG, "getClosestMensa()");
		return mClosestMensa;
	}
    
    public void setCurrentMensa(Mensa mensa) {
    	Log.i(TAG, "setCurrentMensa()");
    	mCurrentMensa = mensa;
    }
    
    public Mensa getCurrentMensa() {
    	Log.i(TAG, "getCurrentMensa()");
    	return mCurrentMensa;
    }

    /**
     * Gets the JSON from REST API and builds the data model from it
     */
    public void loadModel() {
    	Log.i(TAG, "loadModel()");
    	TaskCreateModel mTask = new TaskCreateModel(mController, this);
        mTask.addListener(this);
        mTask.addListener(mController);
        mTask.execute();
    }
    
    public boolean hasModel() {
    	Log.i(TAG, "hasModel()");
    	if (mModel != null) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public MensaList getModel() {
    	Log.i(TAG, "getModel()");
    	return mModel;
    }
    
    /**
     * Gets the JSON from REST API and builds the data model from it
     */
    public void loadLocation() {
    	Log.i(TAG, "loadLocation()");
    	TaskLocation mTask = new TaskLocation(mController);
        mTask.addListener(this);
        mTask.addListener(mController);
        mTask.execute();
    }
    
    public boolean hasLocation() {
    	Log.i(TAG, "hasLocation()");
    	if (mLocation != null) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public Location getLocation() {
    	Log.i(TAG, "getLocation()");
    	return mLocation;
    }
    
    public void setLocation(Location location) {
    	Log.i(TAG, "setLocation(" + location + ")");
    	mLocation = location;
    }
    
    public LatLng getLocationTarget() {
    	Log.i(TAG, "getLocationTarget()");
    	return mLocationTarget;
    }
    
    public void setLocationTarget(LatLng location) {
    	Log.i(TAG, "setLocationTarget(" + location + ")");
    	mLocationTarget = location;
    }
    
    public ServiceWebRequest getWebService() {
    	Log.i(TAG, "getWebService()");
    	return mWebService;
    }
    
    public ServicePrefsManager getPersistenceManager() {
    	Log.i(TAG, "getPersistenceManager()");
    	return mPersistenceManager;
    }
    
    public DatabaseManager getDatabaseManager() {
    	Log.i(TAG, "getDatabaseManager()");
    	return mDBManager;
    }

	public void DBUpdate() {
		Log.i(TAG, "DBUpdate()");
    	TaskUpdateDB mTask = new TaskUpdateDB(mController);
        mTask.addListener(this);
        mTask.execute();
		//mDBManager.save(mModel);
	}

	public void DBUpdateFavorite(Mensa mensa) {
		Log.i(TAG, "DBUpdateFavorite()");
		if(mensa.isFavorite()){
			mDBManager.saveFavorite(mensa);
		} else {
			mDBManager.removeFavorite(mensa);
		}
	}
    
	/**
	 * Returns a Pseudo Unique ID to identify the user, will be sent to the server and doesn't contain anything private
	 * @return deviceId 
	 */
	public static String getDeviceId() {
		Log.i(TAG, "getDeviceId()");
		String deviceid = (String) mPersistenceManager.getData("string", "deviceid");
		
		if (deviceid == null) {
			Log.i(TAG, "getDeviceId(): Creating and saving new device ID");
		    // IF all else fails or if the user has reset their phone or 'Secure.ANDROID_ID'
		    // returns 'null', then simply the ID returned will be soley based
		    // off their Android device information.
		    String devIdShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
	
		    // Only devices with API >= 9 have android.os.Build.SERIAL
		    // If a user upgrades software or roots their phone, there will be a duplicate entry
		    String serial = null; 
		    try {
		        serial = android.os.Build.class.getField("SERIAL").toString();
		        // go ahead and return the serial for api => 9
		        return new UUID(devIdShort.hashCode(), serial.hashCode()).toString();
		    } catch (Exception e) { 
		        // String needs to be initialized
		        serial = "serial"; // some value
		    }
	
		    // Finally, combine the values we have found by using the UUID class to create a unique identifier
		    deviceid = new UUID(devIdShort.hashCode(), serial.hashCode()).toString();
		    // save the id to shared prefs for later usage
		    mPersistenceManager.setData("string", "deviceid", deviceid);
		}
		
	    return deviceid;
	}
	
	public void APIRegisterUser() {
		Log.i(TAG, "APIRegisterUser(" + mDeviceId + ")");

    	TaskUpdateAPI mTask = new TaskUpdateAPI(mController, mDeviceId);
        mTask.addListener(this);
        mTask.execute();
	}
	
	public void APIRegisterRating(int menuid, int rating) {
		Log.i(TAG, "APIRegisterUser(" + menuid + ", " + rating + ")");

    	TaskUpdateAPI mTask = new TaskUpdateAPI(mController, mDeviceId, menuid, rating);
        mTask.addListener(this);
        mTask.execute();
	}
}
