package com.mensaunibe.app.controller;

import java.util.HashSet;
import java.util.Set;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.mensaunibe.R;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.app.views.FragmentFriends;
import com.mensaunibe.app.views.FragmentMensaList;
import com.mensaunibe.app.views.FragmentMensaMap;
import com.mensaunibe.app.views.FragmentMenuList;
import com.mensaunibe.app.views.FragmentNotifications;
import com.mensaunibe.app.views.FragmentSplashScreen;
import com.mensaunibe.app.views.FragmentStart;
import com.mensaunibe.lib.dialogs.SimpleDialogListener;
import com.mensaunibe.util.gui.CustomNavigationDrawer;
import com.mensaunibe.util.tasks.TaskListener;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

public class ActivityMain extends FragmentActivity implements TaskListener, SimpleDialogListener, ConnectionCallbacks, OnConnectionFailedListener {

	// for logging and debugging purposes
	private static final String TAG = ActivityMain.class.getSimpleName();
	
	private FragmentManager fm;
	private LocationClient lc;
	private ConnectivityManager cm;
	private WifiManager wm;
	
	private DataHandler mDataHandler;
	private FragmentSplashScreen mSplashScreen;

	private CustomNavigationDrawer mDrawer;

	private boolean mModelReady;
	private boolean mWait;

	private Bundle si;

	private static Context sContext;

	/**
	 * Overrides
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		// handle action bar hiding on app start for the splash screen (prevents flickering)
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	    getActionBar().hide();
	    getActionBar().setCustomView(R.layout.actionbar_status);
	    getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
	    
	    // set the waiting variable to false initially
		mWait = false;
		// put the savedInstanceState in a variable for other methods to use
		si = savedInstanceState;
		
		// set a global reference to the app context, get it via getContext() method
		sContext = getApplicationContext();
		
		// set a global reference to the fragment manager as we use it a lot
		fm = getSupportFragmentManager();
    	
    	// initialize the ConnectivityManager
    	cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    	
    	// initialize the WifiManager
    	wm = (WifiManager) getSystemService(WIFI_SERVICE);
    	
		// initialize the LocationClient		
    	lc = new LocationClient(this, this, this);
		
		// until the data loader fragment has done it's job, show the splash screen with a progress bar
		attachSplashScreen();

		// initialize retaining data loader fragment for data persistence
		attachDataHandler();
		
		setContentView(R.layout.activity_main);
		
//		// set up the navigation drawer, is done later on as the model is not ready yet!
//		attachNavigationDrawer(savedInstanceState);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onPostCreate(Bundle)");
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		if (mDrawer != null ) {
			mDrawer.getDrawerToggle().syncState();
		} else {
			Log.e(TAG, "onPostCreate(): mDrawer is null");
		}
	}
	
	@Override
    protected void onStart() {
		Log.i(TAG, "onStart()");
        super.onStart();
        if (mDataHandler != null) {
            getLoadStatus();
        }
    }
	
	@Override
	protected void onPause() {
		Log.i(TAG, "onPause()");
		super.onPause();
		
		if (lc.isConnected()) {
			lc.disconnect();
		}
	}
	
	@Override
	protected void onResume() {
		Log.i(TAG, "onResume(), mDrawer = " + mDrawer + ", mModelReady = " + mModelReady);
		super.onResume();
		if (mDrawer == null && mModelReady) {
			attachNavigationDrawer(si);
		}
		
		if (!lc.isConnected()) {
			lc.connect();
		}
	}

    @Override
    protected void onStop() {
    	Log.i(TAG, "onStop()");
        super.onStop();
        if (mDataHandler != null) {
//            mDataHandler.removeProgressListener();
        }
    }

	// reqired for proper setup of ActionBarDrawerToggle
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.i(TAG, "onConfigurationChanged(Configuration)");
		super.onConfigurationChanged(newConfig);
		if (mDrawer != null ) {
			// Pass any configuration change to the drawer toggle
			mDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
		} else {
			Log.e(TAG, "onConfigurationChanged(): mDrawer is null");
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		if (mDrawer != null) {
			if (mDrawer.isDrawerOpen()) {
	//			menu.findItem(R.id.test).setVisible(false);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "onCreateOptionsMenu(Menu)");
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
    // location client callbacks
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.e(TAG, "onConnectionFailed(): " + result.getErrorCode());
//		Toast.makeText(mContext, "locationClient connection failed", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		Log.i(TAG, "onConnected()");
		// notify fragments listening for a successfull connection
		this.notifyListeners("onLocationClientConnected");
//		Toast.makeText(mContext, "locationClient connected", Toast.LENGTH_LONG).show();
//		mLocation = mLocationClient.getLastLocation();
	}

	@Override
	public void onDisconnected() {
		Log.i(TAG, "onDisconnected()");
//		Toast.makeText(mContext, "locationClient disconnected", Toast.LENGTH_LONG).show();
	}
    
    // task listener callbacks
    @Override
    public void onTaskComplete(Object result) {
    	Log.i(TAG, "onTaskComplete(" + result + ")");

    	if (result != null) {
			if (result instanceof MensaList) {
				// call method again to remove the splashscreen and check the model availability
		    	getLoadStatus();
		    	// set up the navigation drawer
		    	attachNavigationDrawer(si);
		    	// load the current location + closest mensa
				mDataHandler.loadLocation();
				// write the model to the db
				//mDataHandler.updateDB();
			}
    	} else {
    		Log.e(TAG, "onTaskComplete(Object): result was null! Trying to get the model again...");
    		if (mWait == false) {
    			mDataHandler.loadModel();
    		} else {
    			Log.i(TAG, "Waiting for user input on dialog");
    		}
    	}
    }

    @Override
    public void onProgressUpdate(int percent) {
    	Fragment mFragment = fm.findFragmentById(R.id.frame_content);
		if (mSplashScreen != null) {
    		mSplashScreen.setProgress(percent);
    	} else if (mFragment != null && mFragment instanceof FragmentStart) {
    		((FragmentStart) mFragment).getProgressBar().setProgress(percent);
    	}
    }
    
	@Override
	public void onRendered() {
		// unused		
	}
	
	/**
	 * Custom Methods
	 */
    // create the data loader fragment if not present
    private void attachDataHandler() {
    	Log.i(TAG, "attachDataHandler()");
		mDataHandler = (DataHandler) fm.findFragmentByTag("data");
		
		// if not instantiated yet, create a new FragmentData
		if (mDataHandler == null) {
			mDataHandler = DataHandler.newInstance(this);
            mDataHandler.loadModel();
			fm.beginTransaction().add(mDataHandler, "data").commit();
		} else {
            if (getLoadStatus()) {
                return;
            }
        }
    }
    
	// returns an instance of the data fragment
	public DataHandler getDataHandler() {
		Log.i(TAG, "getDataHandler()");
		if (mDataHandler != null) {
			return mDataHandler;
		} else {
			Log.e(TAG, "getDataHandler(): mDataHandler was null, probably lost sync!");
			Toast.makeText(this, "DataHandler was null, probably lost sync!", Toast.LENGTH_SHORT).show();
			attachDataHandler();
			return mDataHandler;
		}
	}
	
	// check if the model is already instantiated
	public boolean isModelReady() {
		Log.i(TAG, "isModelReady()");
		return mModelReady;
	}
    
    // create loading fragment / splash screen if not present
    private void attachSplashScreen() {
    	Log.i(TAG, "attachSplashScreenFragment()");
        mSplashScreen = (FragmentSplashScreen) fm.findFragmentByTag("splashscreen");

		// if not instantiated yet, create a new FragmentData
        if (mSplashScreen == null) {
            mSplashScreen = new FragmentSplashScreen();
            fm.beginTransaction().add(android.R.id.content, mSplashScreen, "splashscreen").commit();
        }
        
        // show the initially hidden action bar
    	getActionBar().show();
    	// set the background back to default
    	getWindow().setBackgroundDrawableResource(R.drawable.unibe_window_bg);
    }
    
    private void detachSplashScreen() {
    	Log.i(TAG, "detachSplashScreenFragment()");
    	mSplashScreen = (FragmentSplashScreen) fm.findFragmentByTag("splashscreen");
        if (mSplashScreen != null) {
            fm.beginTransaction().remove(mSplashScreen).commit();
        }
    }
    
    private void attachNavigationDrawer(Bundle savedInstanceState) {
    	Log.i(TAG, "attachNavigationDrawer()");
    	// Find our drawer view
		mDrawer = (CustomNavigationDrawer) findViewById(R.id.drawer_layout);
		// Setup drawer view
		mDrawer.setupDrawer((ListView) findViewById(R.id.sidenav), R.layout.sidenav_item, R.id.frame_content);
		// Make 3 line icon show instaead of up arrow
		mDrawer.getDrawerToggle().syncState();
		// Add nav items
		mDrawer.addNavItem(getString(R.string.title_home), FragmentStart.class);
		mDrawer.addNavItem(getString(R.string.title_mensalist), FragmentMensaList.class);
		mDrawer.addNavItem(getString(R.string.title_mensamap), FragmentMensaMap.class);
		mDrawer.addNavItem(getString(R.string.title_menulist), FragmentMenuList.class);
		mDrawer.addNavItem(getString(R.string.title_friends), FragmentFriends.class);
		mDrawer.addNavItem(getString(R.string.title_notifications), FragmentNotifications.class);
		// Select default
		if (savedInstanceState == null) {
			mDrawer.selectItem(0);	
		}
    }

	// returns the SharedPreferences
	public static SharedPreferences getSettings() {
		// TODO Auto-generated method stub
		return null;
	}
	// returns a reference to the context
	public Context getContext() {
		return sContext;
	}
	
	// returns a reference to the location client
	public LocationClient getLocationClient() {
		return lc;
	}
	
	// returns a reference to the connectivity manager
	public ConnectivityManager getConnectivityManager() {
		return cm;
	}
	
	// returns a reference to the wifi manager
	public WifiManager getWifiManager() {
		return wm;
	}
	
    /**
     * Checks if data is done loading, if it is, the result is handled
     *
     * @return true if data is done loading
     */
    private boolean getLoadStatus() {
    	Log.i(TAG, "getLoadStatus()");
    	
    	mModelReady = false;
    	
        if (mDataHandler.hasModel()) {
        	Log.i(TAG, "DataFragment has result");
        	
        	mModelReady = true;
            detachSplashScreen();
            return true;
        }
        
        return false;
    }
	
	/**
	 * This is a the event interface to notify Fragments when the model has changed
	 * or some central things like location client connections have changed state
	 */
	public interface ControllerListener {
		public void onModelChanged();
		public void onLocationClientConnected();
	};

	// initialize the listener set
	private Set<ControllerListener> mListeners = new HashSet<ControllerListener>();

	// add a listerner
	public void addListener(ControllerListener listener) {
		mListeners.add(listener);
	}
	
	// remove a listener
	public void removeListener(ControllerListener listener) {
		mListeners.remove(listener);
	}

	// notify the listeners when the model has changed
	private void notifyListeners(String event) {
		for (ControllerListener listener : mListeners) {
			if (event.equals("onModelChanged")) {
				listener.onModelChanged();
			} else if (event.equals("onLocationClientConnected")) {
				listener.onLocationClientConnected();
			}
		}
	}

    // dialog listener callbacks
	@Override
	public void onDialogCreated() {
		Log.i(TAG, "onDialogCreated()");
		mWait = true;
	}
	
	@Override
	public void onPositiveButtonClicked(int requestCode) {
		Log.i(TAG, "onPositiveButtonClicked(" + requestCode+ ")");
		if (requestCode == 444) {
			// turn of the wifi
			wm.setWifiEnabled(false);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {

			}
		}
		
		mWait = false;
		if (!mDataHandler.hasModel()) {
			mDataHandler.loadModel();
		}
	}

	@Override
	public void onNegativeButtonClicked(int requestCode) {
		Log.i(TAG, "onNegativeButtonClicked(" + requestCode+ ")");
		mWait = false;
		if (!mDataHandler.hasModel()) {
			mDataHandler.loadModel();
		}
	}
}
