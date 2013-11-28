package com.mensaunibe.app.model;

import com.mensaunibe.app.controller.ActivityMain;
import com.mensaunibe.util.database.DatabaseManager;
import com.mensaunibe.util.tasks.TaskCreateModel;
import com.mensaunibe.util.tasks.TaskListener;
import com.mensaunibe.util.tasks.TaskLocation;

import android.location.Location;
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
	
	private static ActivityMain mController;
	private static DatabaseManager mDBManager;
	private MensaList mModel;
	
	private Location mLocation;
	private Mensa mClosestMensa;
	private Mensa mCurrentMensa;
	
	
/**
 * 
 * @param controller; takes the ActivityMain as parameter
 * @return instance of the DataHandler
 */
	public static DataHandler newInstance(ActivityMain controller) {
		Log.i(TAG, "newInstance()");
		DataHandler instance = new DataHandler();
		// Keep this Fragment even during config changes
		instance.setRetainInstance(true);
		mController = controller;
		mDBManager = new DatabaseManager(mController.getContext());
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
				mDBManager.save((MensaList) result);
			} else if (result instanceof Location) {
				mLocation = (Location) result;
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
	
//    /**
//     * @return the result
//     */
//    public Object getResult() {
//    	Log.i(TAG, "getResult()");
//    	if (!Double.isNaN(mResult)) {
//    		return mResult;
//    	} else if (mModel != null) {
//    		return mModel;
//    	} else {
//    		return null;
//    	}
//    }

//    /**
//     * Returns true if a result has already been calculated
//     *
//     * @return true if a result has already been calculated
//     * @see #getResult()
//     */
//    public boolean hasResult() {
//    	Log.i(TAG, "hasResult()");
//    	if (mModel != null || !Double.isNaN(mResult)) {
//    		return true;
//    	} else {
//    		return false;
//    	}
//    }

//    /**
//     * Removes the ProgressListener
//     *
//     * @see #setProgressListener(ProgressListener)
//     */
//    public void removeProgressListener() {
//    	Log.i(TAG, "removeProgressListener()");
////        mFragmentListener = null;
//    }

//    /**
//     * Sets the ProgressListener to be notified of updates
//     *
//     * @param listener ProgressListener to notify
//     * @see #removeProgressListener()
//     */
//    public void setProgressListener(TaskListener listener) {
//    	Log.i(TAG, "setProgressListener()");
////        mFragmentListener = listener;
//    }
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
    	Log.i(TAG, "getCurrentMensa(), mCurrentMensa = " + mCurrentMensa);
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
    	Log.i(TAG, "getModel(), currentMensa = " + mCurrentMensa);
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
    	Log.i(TAG, "getModel(), currentMensa = " + mCurrentMensa);
    	return mLocation;
    }
    
    public void setLocation(Location location) {
    	mLocation = location;
    }


	public void updateDB() {
		mDBManager.save(mModel);
	}


	public void updateFavorite(Mensa mensa) {
		if(mensa.isFavorite()){
			mDBManager.saveFavorite(mensa);
		} else {
			mDBManager.removeFavorite(mensa);
		}
	}
}
