package com.ese2013.mensaunibe;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ese2013.mensaunibe.model.ModelMensa;
import com.ese2013.mensaunibe.model.Model;
import com.ese2013.mensaunibe.util.WebServiceAsync;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class ActivityMain extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mNavItems;
	
	private static Context context;
	
	// prepare the location data, the variables must be public for use in fragments
	public LocationClient locationClient;
	public Location location;
	public Map<Integer, Float> distances;
	public int nearestmensaid;
	public ModelMensa nearestmensa;
	public static Map<String, Object> favMensaIds = new HashMap<String, Object>();

	// The model provides and manages the mensas objects for the app
	public Model model;
	// global UI Thread handler
	public Handler mHandler = new Handler();
	
	public ListView listView;
	public ModelMensa currentMensa;

	public static SharedPreferences settings;
	public static Editor settingseditor;
	public String deviceid;
	
	public Button notifCount;
	public int mNotifCount = (int) (Math.random() * 15 + 1);

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = getApplicationContext();
		
		
		// set up the shared prefs, just use the default ones
		ActivityMain.settings = PreferenceManager.getDefaultSharedPreferences(context);
		ActivityMain.settingseditor = settings.edit();
		
//		Toast.makeText(context, "settings " + settings.getAll(), Toast.LENGTH_LONG).show();
		
		// convert the stored favorite mensas to a hash map (from JSON string)
		favMensaIds = jsonStrToMap(settings.getString("favmensas", null));
		
		// check if a device id is stored in shared prefs
		this.deviceid = settings.getString("deviceid", null);
		
		if ( deviceid == null ) {
//			Toast.makeText(context, "no deviceid in shared prefs creating one, prefs: " + settings.getAll(), Toast.LENGTH_LONG).show();
		    settingseditor.putString("deviceid", getUniquePsuedoID());
		    settingseditor.commit();
		} else {
//			Toast.makeText(context, "deviceid in shared prefs: " + deviceid, Toast.LENGTH_LONG).show();
		}
		
		// check if the user is already registered in the api
		Boolean registered = settings.getBoolean("registered", false);
		
		if ( !registered ) {
//			Toast.makeText(context, "device is not registered in the API, trying to register", Toast.LENGTH_LONG).show();
			// register the device in the API only when necessary
			new WebServiceAsync(new AsyncCallback() { 
	            @Override
	            public void onTaskDone(JsonObject json) {
	            	String status = null;
					status = json.get("status").getAsString();
				    if ( status.equals("registered") ) {;
						Toast.makeText(context, "saving status to shared prefs", Toast.LENGTH_LONG).show();
				    	settingseditor.putBoolean("registered", true);
					    settingseditor.commit();
				    } else {
//					    	Toast.makeText(context, "status not registered", Toast.LENGTH_LONG).show();
				    }

//					Toast.makeText(context, status, Toast.LENGTH_LONG).show();
	            }
		    }).execute("http://api.031.be/mensaunibe/getdata/?deviceid=" + deviceid); // start the background processing
		} else {
//			Toast.makeText(context, "device is registered in the API", Toast.LENGTH_LONG).show();
		}
		
		// Model that is providing all the logic for the app is instantiated
		this.model = Model.getInstance();
		
//		ActivityMain.toast("lastupdate: " + settings.getInt("lastupdate", 0));
		
		// try to fix phones with settings key and force them so show and use the actionbar menu
		getOverflowMenu();
		
		// initialize the locationClient
		locationClient = new LocationClient(this, this, this);

		
		mTitle = mDrawerTitle = getTitle();
		mNavItems = getResources().getStringArray(R.array.sidenav_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_sidenav);

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.sidenav_item, mNavItems));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if ( Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 14 ) {
			getActionBar().setHomeButtonEnabled(true);
		}
		

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.sidenav_open, /* "open drawer" description for accessibility */
		R.string.sidenav_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}
	
	public static Context getContext() {
		return context;
	}
	
	// just a helper method to make toasting easier
	public static void toast(String msg) {
		new Toast(context);
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	private void getOverflowMenu() {

	    try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	         Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	         if (menuKeyField != null) {
	             menuKeyField.setAccessible(true);
	             menuKeyField.setBoolean(config, false);
	         }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * this method is JUST for the view pager
	 * It's needed because the Java week starts at sunday, eg sunday = 1
	 * the pageview array o the viewpager starts at 0 and ends at 4 for friday
	 * so we need to return the correct values for that
	 * @return
	 */
	public int calcWeekDay() {
		Calendar calendar = new GregorianCalendar();
		Date now = new Date();   
		calendar.setTime(now);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		switch(day) {
			// sunday
			case 1: 
				return 4;
			// monday
			case 2:
				return 0;
			// tuesday
			case 3:
				return 1;
			// wednesday
			case 4:
				return 2;
			// thursday
			case 5:
				return 3;
			// friday
			case 6:
				return 4;
			// saturday
			case 7:
				return 4;
			default:
				return 0;
		}
	}

	@Override
	protected void onStop() {
		model.updateLocalData();
		super.onStop();
	}
                  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_action, menu);
		final MenuItem menuitem = menu.findItem(R.id.action_notifications);
		View count = menu.findItem(R.id.action_notifications).getActionView();
		this.notifCount = (Button) count.findViewById(R.id.notification_count);
		notifCount.setText(String.valueOf(mNotifCount));
	    notifCount.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	            onOptionsItemSelected(menuitem);
	        }
	    });
		return super.onCreateOptionsMenu(menu);
	}

	public void setNotifCount(int count) {
//		String countstring = String.valueOf(count);
//		notifCount.setText(String.valueOf(mNotifCount));
//		invalidateOptionsMenu();
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		switch (item.getItemId()) {
	        case R.id.action_settings:
	        	// this is very hacky as the support library doesn't support PreferenceFragment
	        	// so we have to use the normal fragment manager here (which we can't with the viewPager as it seems)
	        	// the problem is, that the both FragmentManagers don't know from each other
	        	// which results in the system settings overlapping the content of the currently showed fragment
	        	// so basically we just have to different FrameLayouts in the main activity, one is used for the normal
	        	// content, the other for the settings and depending on what should be displayed they are hidden or not
	        	View view = findViewById(R.id.content_frame);
	        	view.setVisibility(View.GONE);
	        	view = findViewById(R.id.settings_frame);
	        	view.setVisibility(View.VISIBLE);
	        	getFragmentManager().beginTransaction().replace(R.id.settings_frame, new FragmentSettings()).commit();
		        break;
	        case R.id.action_notifications:
	        	selectItem(4);
	        	break;
	        default:
	        	break;
	    }
		
		return super.onOptionsItemSelected(item);
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// make the content frame visible again (could be hidden when visiting settings fragment)
		View view = findViewById(R.id.content_frame);
    	view.setVisibility(View.VISIBLE);
    	view = findViewById(R.id.settings_frame);
    	view.setVisibility(View.GONE);
		Fragment fragment = null;
		// update the main content by replacing fragments
		switch (position) {
		case 0:
			fragment = new FragmentStart();
			break;
		case 1:
			fragment = new FragmentMensaList();
			break;
		case 2:
			fragment = new FragmentMenuList();
			break;
		case 3:
			fragment = new FragmentMensaMap();
			break;
		case 4:
			fragment = new FragmentNotifications();
			break;
		case 5:
			fragment = new FragmentFriends();
			break;
		}
	
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mNavItems[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}
	
	// this overrides the behavior of the back button, but we need standard behavior inside the detail fragments
	// how to do this?
//	@Override
//	public void onBackPressed() {
//		mDrawerLayout.openDrawer(mDrawerList);
//	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	// the following methods handle the locationService for the map, the current position is determined here and then
	// fetched from the FragmentMensaMap
	@Override
	protected void onResume() {
		super.onResume();
		// getting some null pointer here, dunno why...
		//setNotifCount((int) (Math.random() * 15 + 1));

		locationClient.connect();    
	}

	@Override
	protected void onPause() {
		super.onPause();

		locationClient.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Toast.makeText(this, "locationClient connection failed", Toast.LENGTH_LONG).show();
	}

	@SuppressLint("UseSparseArrays")
	@Override
	public void onConnected(Bundle connectionHint) {
		Toast.makeText(this, "locationClient connected", Toast.LENGTH_LONG).show();
		this.location = locationClient.getLastLocation();
		// loop trough all the mansa coordinates and determine the closest mensa
		// fist get all the mensas in a list to loop over
		ArrayList <ModelMensa> mensas = model.getMensas();
		// check if there is a last location set, possible reason for NullPointer Exception in onConnected()
		if ( location != null ) {
			// initialize the distances array to save all distances in
			Map<Integer, Float> distances = new HashMap<Integer, Float>();
			for (ModelMensa mensa : mensas) {
				distances.put(mensa.getId(), getDistance(location.getLatitude(), location.getLongitude(), mensa.getLat(), mensa.getLon()));
			}
			// make the distances globally available, just for convenience
			this.distances = distances;
			// and now find the nearest mensa
			int nearestmensaid = 0;
			float smallestdistance = Float.MAX_VALUE;
			for (Map.Entry<Integer, Float> entry : distances.entrySet()) {
			    Float value = entry.getValue();
			    if (value < smallestdistance) {
			        nearestmensaid = entry.getKey();
			        smallestdistance = value;
			    }
			}
			// also make this globally available for convenience and fetch the mensa object
			this.nearestmensaid = nearestmensaid;
			this.nearestmensa = model.getMensaById(nearestmensaid);
	//		Toast.makeText(this, "current location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
			Toast.makeText(this, "closest mensa: " + nearestmensa.getName(), Toast.LENGTH_SHORT).show();
			if ( smallestdistance <= 100 ) {
				// 100 is too big, should probably be smaller than 50 or less
				// here we would save the mensaid to the users profile on the server
				// then we can either show his friends that are in that mensa too
				// or the total number people in that mensa, or both...
				updateUserMensa();
//				Toast.makeText(this, "mensa " + smallestdistance + "m away, are you in there?", Toast.LENGTH_SHORT).show();
			}
			// a very ugly and hacky way to show the closest mensa on the home screen
			// this should only be done when no favorite mensas are set, but the logic for that could be in the start fragment
			selectItem(0);
		} else {
			Toast.makeText(this, "last location was null, did the app crash here earlier?", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "locationClient disconnected", Toast.LENGTH_LONG).show();
	}
    
	public static Map<String, Object> jsonStrToMap(String json) {
		Type type = new TypeToken<Map<String, Object>>(){}.getType();
		Map<String, Object> map = new Gson().fromJson(json, type);

	    return map;
	}
	
	public Location getLocation() {
		return location;
	}
	
	// this method stupidly calculates the air distance between two points, but should be enough to determine the closest mensa
	public static float getDistance(double startLat, double startLon, double endLat, double endLon){
	    float[] resultArray = new float[99];
	    Location.distanceBetween(startLat, startLon, endLat, endLon, resultArray);
	    return resultArray[0];
	}
	
	// UNUSED
	// this method asks the remote maps api for the path distance, e.g the distance over streets
	// currently not used, would be more accurate, but also make many remote requests each time
//	public String getRouteDistance(double startLat, double startLon, double endLat, double endLon) {
//	    WebService webService = new WebService();
//	    String Distance = "error";
//	    String Status = "error";
//	    try {
//	        Log.e("Distance Link : ", "http://maps.googleapis.com/maps/api/directions/json?origin="+ startLat +","+ startLon +"&destination="+ endLat +","+ endLon +"&sensor=false");
//	        JsonObject jsonObj = webService.requestFromURL("http://maps.googleapis.com/maps/api/directions/json?origin="+ startLat +","+ startLon +"&destination="+ endLat +","+ endLon +"&sensor=false"); 
//	        Status = jsonObj.get("status").getAsString();
//	        if(Status.equalsIgnoreCase("OK")) {
//	            JsonArray routes = jsonObj.get("routes").getAsJsonArray(); 
//	            JsonArray legs = routes.get(0).getAsJsonObject().get("legs").getAsJsonArray();
//	            JsonObject dist = legs.get(0).getAsJsonObject().get("distance").getAsJsonObject();
//	            Distance = dist.get("text").getAsString();
//	        } else {
//	            Distance = "Too Far";
//	        }
//	    } catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    
//	    return Distance;
//	}
	
	/**
	 * Return Pseudo Unique ID to identify the user, will be sent to the server and doesn't contain anything private
	 * @return ID 
	 */
	public static String getUniquePsuedoID() {
		Toast.makeText(context, "getUniquePseudoID method running", Toast.LENGTH_LONG).show();
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
	    String deviceid = new UUID(devIdShort.hashCode(), serial.hashCode()).toString();
	    
	    return deviceid;
	}
	
//	public void sendUserID() {
//		String deviceid = getUniquePsuedoID();
//		try {
//			JSONObject statusJSON = webService.requestFromURL("http://api.031.be/mensaunibe/getdata/?deviceid=" + deviceid);
//			String status = statusJSON.getString("status");
//			Toast.makeText(this, status, Toast.LENGTH_LONG).show();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//        // actually we should save this id somewhere to get it over sessions...should always be the same...
//        // if the status is "registered" then this should be saved persistent somewhere to not always make this request!
//	}
	
	public void updateUserMensa() {
		String url = "http://api.031.be/mensaunibe/getdata/?deviceid=" + deviceid + "&mensaid=" + nearestmensaid;
		new WebServiceAsync(new AsyncCallback() { 
            @Override
            public void onTaskDone(JsonObject json) {
            	String status = null;
				status = json.get("status").getAsString();

//				Toast.makeText(ActivityMain.this, status, Toast.LENGTH_LONG).show();
            }
	    }).execute(url);
	}
	
	public void updateUser(String username) throws UnsupportedEncodingException {
		String url = "http://api.031.be/mensaunibe/getdata/?deviceid=" + deviceid + "&name=" + URLEncoder.encode(username, "UTF-8");
		new WebServiceAsync(new AsyncCallback() { 
            @Override
            public void onTaskDone(JsonObject json) {
            	String status = null;
				status = json.get("status").getAsString();

//				Toast.makeText(ActivityMain.this, status, Toast.LENGTH_LONG).show();
            }
	    }).execute(url); // start the background processing
	}
	
    public interface AsyncCallback {
        public void onTaskDone(JsonObject json);
    }
}
