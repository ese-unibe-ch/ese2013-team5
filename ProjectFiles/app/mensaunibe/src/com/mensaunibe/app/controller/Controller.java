package com.mensaunibe.app.controller;

import java.util.HashSet;
import java.util.Locale;
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
import com.mensaunibe.app.views.FragmentSettings;
import com.mensaunibe.app.views.FragmentSplashScreen;
import com.mensaunibe.app.views.FragmentStart;
import com.mensaunibe.lib.dialogs.SimpleDialogListener;
import com.mensaunibe.util.gui.CustomNavigationDrawer;
import com.mensaunibe.util.tasks.TaskListener;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

public class Controller extends FragmentActivity implements TaskListener, SimpleDialogListener, ConnectionCallbacks, OnConnectionFailedListener {

	// for logging and debugging purposes
	private static final String TAG = Controller.class.getSimpleName();
	
	private static FragmentManager fm;
	private static LocationClient lc;
	private static ConnectivityManager cm;
	private static WifiManager wm;
	private static Bundle si;
	
	private static DataHandler sDataHandler;
	private static FragmentSplashScreen sSplashScreen;
	private static CustomNavigationDrawer sDrawer;

	private static Controller sController;
	private static Context sContext;
	private static String sAppName;
	private static SharedPreferences sSettings;
	private static String sLanguage;
	private static boolean sModelReady;
	private static boolean sWait;

	/**
	 * Overrides
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		
		// handle action bar hiding on app start for the splash screen (prevents flickering)
		attachActionBar(true);
		
		// set a global reference to the shared prefs, needs to be the first thing to do because of the language
		sSettings = PreferenceManager.getDefaultSharedPreferences(this);
		
		// set up the user specified language
		setDefaultLocale();
	    
	    // set the waiting variable to false initially
		sWait = false;
		
		// put the savedInstanceState in a variable for other methods to use
		si = savedInstanceState;
		
		// a reference to the controller itself for static access from other classes
		sController = this;
		
		// set a global reference to the app context, get it via getContext() method
		sContext = getApplicationContext();
		
		// set a global reference to the app name
		sAppName = (String) getApplicationInfo().loadLabel(getPackageManager());
		
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
		
		setContentView(R.layout.controller);
		
		// setting up the navigation drawer is done later onTaskComplete() as the model is not ready yet here!
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onPostCreate(Bundle)");
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		if (sDrawer != null ) {
			sDrawer.getDrawerToggle().syncState();
		} else {
			Log.e(TAG, "onPostCreate(): mDrawer is null");
		}
	}
	
	@Override
    protected void onStart() {
		Log.i(TAG, "onStart()");
        super.onStart();
        if (sDataHandler != null) {
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
		Log.i(TAG, "onResume(), sDrawer = " + sDrawer + ", sModelReady = " + sModelReady);
		super.onResume();
		
//		setDefaultLocale();
		
		if (sDrawer == null && sModelReady) {
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
        if (sDataHandler != null) {
//            mDataHandler.removeProgressListener();
        }
    }

	// reqired for proper setup of ActionBarDrawerToggle
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.i(TAG, "onConfigurationChanged(" + newConfig + ")");
		super.onConfigurationChanged(newConfig);
		if (sDrawer != null && sModelReady) {
			// re-attach the navigation drawer when a config change happens (eg. rotation)
			attachNavigationDrawer(si);
			// Pass any configuration change to the drawer toggle, this actually doesn't work but crashes
			//sDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
		} else {
			//Log.e(TAG, "onConfigurationChanged(): sDrawer = " + sDrawer + ", sModelReady = " + sModelReady);
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		if (sDrawer != null) {
			if (sDrawer.isDrawerOpen()) {
				// menu.findItem(R.id.test).setVisible(false);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(TAG, "onCreateOptionsMenu(" + menu + ")");
		// this would set up the top right dropdown menu, but we don't need it anymore at this time
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (sDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
    // location client callbacks
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.e(TAG, "onConnectionFailed(): " + result.getErrorCode());
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		Log.i(TAG, "onConnected()");
		// notify fragments listening for a successfull connection
		this.notifyListeners("onLocationClientConnected");
	}

	@Override
	public void onDisconnected() {
		Log.i(TAG, "onDisconnected()");
	}
    
    // task listener callbacks
    @Override
    public void onTaskComplete(Object result) {
    	Log.i(TAG, "onTaskComplete(" + result + ")");

    	if (result != null) {
			if (result instanceof MensaList) {
				// call method again to remove the splashscreen and check the model availability
		    	getLoadStatus();
		    	// set up and attach the navigation drawer
		    	attachNavigationDrawer(si);
		    	// load the current location + closest mensa
				sDataHandler.loadLocation();
				// write the model to the db
				sDataHandler.DBUpdate();
			}
    	} else {
    		Log.e(TAG, "onTaskComplete(Object): result was null! Trying to get the model again...");
    		if (sWait == false) {
    			sDataHandler.loadModel();
    		} else {
    			Log.i(TAG, "Waiting for user input on dialog");
    		}
    	}
    }

    @Override
    public void onProgressUpdate(int percent) {
    	Fragment mFragment = fm.findFragmentById(R.id.frame_content);
		if (sSplashScreen != null) {
    		sSplashScreen.setProgress(percent);
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
    private static void attachDataHandler() {
    	Log.i(TAG, "attachDataHandler()");
		sDataHandler = (DataHandler) fm.findFragmentByTag("data");
		
		// if not instantiated yet, create a new FragmentData
		if (sDataHandler == null) {
			sDataHandler = DataHandler.newInstance(sController);
            sDataHandler.loadModel();
			fm.beginTransaction().add(sDataHandler, "data").commit();
		} else {
            if (getLoadStatus()) {
                return;
            }
        }
    }
    
	// returns an instance of the data fragment
	public static DataHandler getDataHandler() {
		Log.i(TAG, "getDataHandler()");
		if (sDataHandler != null) {
			return sDataHandler;
		} else {
			Log.e(TAG, "getDataHandler(): sDataHandler was null, probably lost sync!");
			Toast.makeText(sController, "sDataHandler was null, probably lost sync!", Toast.LENGTH_LONG).show();
			attachDataHandler();
			return sDataHandler;
		}
	}
	
	// check if the model is already instantiated
	public static boolean isModelReady() {
		Log.i(TAG, "isModelReady()");
		return sModelReady;
	}
    
    // create loading fragment / splash screen if not present
    private void attachSplashScreen() {
    	Log.i(TAG, "attachSplashScreenFragment()");
        sSplashScreen = (FragmentSplashScreen) fm.findFragmentByTag("splashscreen");

		// if not instantiated yet, create a new FragmentData
        if (sSplashScreen == null) {
            sSplashScreen = new FragmentSplashScreen();
            fm.beginTransaction().add(android.R.id.content, sSplashScreen, "splashscreen").commit();
        }
        
        // show the initially hidden action bar
    	getActionBar().show();
    	// set the background back to default
    	getWindow().setBackgroundDrawableResource(R.drawable.unibe_window_bg);
    }
    
    private static void detachSplashScreen() {
    	Log.i(TAG, "detachSplashScreenFragment()");
    	sSplashScreen = (FragmentSplashScreen) fm.findFragmentByTag("splashscreen");
        if (sSplashScreen != null) {
            fm.beginTransaction().remove(sSplashScreen).commit();
        }
    }
    
    private void attachNavigationDrawer(Bundle savedInstanceState) {
    	Log.i(TAG, "attachNavigationDrawer()");
    	// Find the drawer view
		sDrawer = (CustomNavigationDrawer) findViewById(R.id.drawer_layout);
		if (sDrawer != null) {
			// Setup drawer view
			sDrawer.setupDrawer((ListView) findViewById(R.id.sidenav), R.layout.sidenav_item, R.id.frame_content);
			// Make 3 line icon show instaead of up arrow
			sDrawer.getDrawerToggle().syncState();
			// Add nav items
			sDrawer.addNavItem(getString(R.string.title_home), FragmentStart.class);
			sDrawer.addNavItem(getString(R.string.title_mensalist), FragmentMensaList.class);
			sDrawer.addNavItem(getString(R.string.title_mensamap), FragmentMensaMap.class);
			sDrawer.addNavItem(getString(R.string.title_menulist), FragmentMenuList.class);
			sDrawer.addNavItem(getString(R.string.title_friends), FragmentFriends.class);
			sDrawer.addNavItem(getString(R.string.title_notifications), FragmentNotifications.class);
			sDrawer.addNavItem(getString(R.string.title_settings), FragmentSettings.class);
			// Select default
			if (savedInstanceState == null) {
				sDrawer.selectItem(0);	
			}
		} else {
			Log.e(TAG, "attachNavigationDrawer(): sDrawer was null!");
		}
    }
    
    public static CustomNavigationDrawer getNavigationDrawer() {
    	return sDrawer;
    }
    
    public void attachActionBar(boolean hide) {
    	getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
    	ActionBar actionBar = getActionBar();
	    if (hide) { actionBar.hide(); }
	    actionBar.setCustomView(R.layout.actionbar_status);
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
    }
	
	// returns a reference to this class
	public static Controller getController() {
		return sController;
	}
	// returns a reference to the context
	public static Context getContext() {
		return sContext;
	}
	
	public static String getApplicationName() {
	    return sAppName;
	}
	
	public static SharedPreferences getSettings() {
		return sSettings;
	}
	
	public static String getLanguage() {
		return sLanguage;
	}
	
	// returns a reference to the location client
	public static LocationClient getLocationClient() {
		return lc;
	}
	
	// returns a reference to the connectivity manager
	public static ConnectivityManager getConnectivityManager() {
		return cm;
	}
	
	// returns a reference to the wifi manager
	public static WifiManager getWifiManager() {
		return wm;
	}
	
	public void setDefaultLocale() {
		Log.i(TAG, "getDefaultLocale()");
		// we cannot use the DataHandler when this is called first, so we go the direct way over the system
		sLanguage = sSettings.getString("setting_language", null);
		
		if (sLanguage != null && !sLanguage.equals("")) {
			//Log.i(TAG, "getDefaultLocale(): sLanguage already saved in shared prefs = " + sLanguage);
			// a user language just for the app is set
			Locale locale = new Locale(sLanguage);
			Locale.setDefault(locale);
			Configuration appConfig = getBaseContext().getResources().getConfiguration();
			
			// only take action when the language that is currently used and the user language are not the same
			if (!appConfig.locale.equals(locale)) { 
				appConfig.locale = locale;
				getBaseContext().getResources().updateConfiguration(appConfig, null);
				onConfigurationChanged(appConfig);
			}
		} else {
			// if no language is set yet, set it to the default system language
			//Log.i(TAG, "getDefaultLocale(): setting default lang from system " + Locale.getDefault().getLanguage());
			sLanguage = Locale.getDefault().getLanguage();
			Editor editor = sSettings.edit();
			editor.putString("setting_language", sLanguage);
			editor.commit();
		}
	}
	
    /**
     * Checks if data is done loading, if it is, the result is handled
     *
     * @return true if data is done loading
     */
    private static boolean getLoadStatus() {
    	Log.i(TAG, "getLoadStatus()");
    	
    	sModelReady = false;
    	
        if (sDataHandler.hasModel()) {
        	Log.i(TAG, "sDataHandler has the model ready");
        	
        	sModelReady = true;
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
		sWait = true;
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
		
		sWait = false;
		if (!sDataHandler.hasModel()) {
			sDataHandler.loadModel();
		}
	}

	@Override
	public void onNegativeButtonClicked(int requestCode) {
		Log.i(TAG, "onNegativeButtonClicked(" + requestCode+ ")");
		sWait = false;
		if (!sDataHandler.hasModel()) {
			sDataHandler.loadModel();
		}
	}
}
