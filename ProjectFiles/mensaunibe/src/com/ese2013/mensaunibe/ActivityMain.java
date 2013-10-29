package com.ese2013.mensaunibe;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Model;
import com.ese2013.mensaunibe.util.WebService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ActivityMain extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mNavItems;
	
	private static Context appContext;
	
	// prepare the location data, the variables must be public for use in fragments
	public LocationClient locationClient;
	public Location location;
	public Map<Integer, Float> distances;
	public int nearestmensaid;
	public Mensa nearestmensa;

	// The model provides and manages the mensas objects for the app
	public Model model;
	
	public ListView listView;
	public Mensa currentMensa;
	

	static Button notifCount;
	static int mNotifCount = 2;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		appContext = getApplicationContext();
		

		// Model that is providing all the logic for the app is instantiated
		this.model = new Model();
		
		// initialize the locationClient
		locationClient = new LocationClient(this, this, this);
		
		// send userid to server, this should only be done one or two times, basically first register
		// the user, then check the next time and save the userid somewhere locally (api returns "registered" in status
		// when a user/device is in the database
		// we also need an EditText field in the system settings to add a username, which then would also be sent to the
		// server for the whole notification and friends stuff, the api is ready for that...
		sendUserID();
		
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
		getActionBar().setHomeButtonEnabled(true);

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
	
	public static Context getContextOfApp() {
		return appContext;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_action, menu);
		final MenuItem menuitem = menu.findItem(R.id.action_notifications);
		View count = menu.findItem(R.id.action_notifications).getActionView();
		notifCount = (Button) count.findViewById(R.id.notification_count);
		notifCount.setText(String.valueOf(mNotifCount));
	    notifCount.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	            onOptionsItemSelected(menuitem);
	        }
	    });
		return super.onCreateOptionsMenu(menu);
	}

	@SuppressWarnings("unused")
	private void setNotifCount(int count) {
		int mNotifCount = count;
		invalidateOptionsMenu();
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		@SuppressWarnings("unused")
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
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
//		        Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
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
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	// the following methods handle the locationService for the map, the current position is determined here and then
	// fetched from the FragmentMensaMap
	@Override
	protected void onResume() {
		super.onResume();

		locationClient.connect();
	}

	@Override
	protected void onPause() {
		super.onPause();

		locationClient.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "locationClient connection failed", Toast.LENGTH_LONG).show();
	}

	@SuppressLint("UseSparseArrays")
	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "locationClient connected", Toast.LENGTH_LONG).show();
		this.location = locationClient.getLastLocation();
		// loop trough all the mansa coordinates and determine the closest mensa
		// fist get all the mensas in a list to loop over
		ArrayList <Mensa> mensas = model.getMensas();
		// initialize the distances array to save all distances in
		Map<Integer, Float> distances = new HashMap<Integer, Float>();
		for (Mensa mensa : mensas) {
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
			Toast.makeText(this, "mensa " + smallestdistance + "m away, are you in there?", Toast.LENGTH_SHORT).show();
		}
		// a very ugly and hacky way to show the closest mensa on the home screen
		// this should only be done when no favorite mensas are set, but the logic for that could be in the start fragment
		selectItem(0);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "locationClient disconnected", Toast.LENGTH_LONG).show();
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
	
	// this method asks the remote maps api for the path distance, e.g the distance over streets
	public String getRouteDistance(double startLat, double startLon, double endLat, double endLon) {
	    WebService webService = new WebService();
	    String Distance = "error";
	    String Status = "error";
	    try {
	        Log.e("Distance Link : ", "http://maps.googleapis.com/maps/api/directions/json?origin="+ startLat +","+ startLon +"&destination="+ endLat +","+ endLon +"&sensor=false");
	        JSONObject jsonObj = webService.requestFromURL("http://maps.googleapis.com/maps/api/directions/json?origin="+ startLat +","+ startLon +"&destination="+ endLat +","+ endLon +"&sensor=false"); 
	        Status = jsonObj.getString("status");
	        if(Status.equalsIgnoreCase("OK")) {
	            JSONArray routes = jsonObj.getJSONArray("routes"); 
	            JSONObject zero = routes.getJSONObject(0);
	            JSONArray legs = zero.getJSONArray("legs");
	            JSONObject zero2 = legs.getJSONObject(0);
	            JSONObject dist = zero2.getJSONObject("distance");
	            Distance = dist.getString("text");
	        } else {
	            Distance = "Too Far";
	        }
	    } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    
	    return Distance;
	}
	
	/**
	 * Return Pseudo Unique ID to identify the user, will be sent to the server and doesn't contain anything private
	 * @return ID 
	 */
	public static String getUniquePsuedoID() {
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
	    return new UUID(devIdShort.hashCode(), serial.hashCode()).toString();
	}
	
	public void sendUserID() {
		String deviceid = getUniquePsuedoID();
		WebService webService = new WebService();
		JSONObject jsonObj = webService.requestFromURL("http://api.031.be/mensaunibe/getdata/?deviceid=" + deviceid); 
        try {
			String status = jsonObj.getString("status");
			Toast.makeText(this, status, Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // actually we should save this id somewhere to get it over sessions...should always be the same...
        // if the status is "registered" then this should be saved persistent somewhere to not always make this request!
	}
	
	public void updateUserMensa() {
		String deviceid = getUniquePsuedoID();
		WebService webService = new WebService();
		JSONObject jsonObj = webService.requestFromURL("http://api.031.be/mensaunibe/getdata/?deviceid=" + deviceid + "&mensaid=" + nearestmensaid); 
        try {
			String status = jsonObj.getString("status");
			Toast.makeText(this, status, Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // actually we should save this id somewhere to get it over sessions...should always be the same...
        // if the status is "registered" then this should be saved persistent somewhere to not always make this request!
	}
	
	public void updateUserName(String username) throws UnsupportedEncodingException {
		String deviceid = getUniquePsuedoID();
		WebService webService = new WebService();
		JSONObject jsonObj = webService.requestFromURL("http://api.031.be/mensaunibe/getdata/?deviceid=" + deviceid + "&name=" + URLEncoder.encode(username, "UTF-8")); 
        try {
			String status = jsonObj.getString("status");
			Toast.makeText(this, status, Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // actually we should save this id somewhere to get it over sessions...should always be the same...
        // if the status is "registered" then this should be saved persistent somewhere to not always make this request!
	}
}
