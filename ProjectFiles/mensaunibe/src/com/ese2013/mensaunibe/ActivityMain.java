package com.ese2013.mensaunibe;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Model;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
		
		// initialize the locationClient
		locationClient = new LocationClient(this, this, this);
	}
	
	public static Context getContextOfApp() {
		return appContext;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home, menu);

		View count = menu.findItem(R.id.notifications).getActionView();
		notifCount = (Button) count.findViewById(R.id.notification_count);
		notifCount.setText(String.valueOf(mNotifCount));
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
		// Handle action buttons
		/*
		 * switch(item.getItemId()) { case R.id.action_websearch: // create
		 * intent to perform web search for this planet Intent intent = new
		 * Intent(Intent.ACTION_WEB_SEARCH);
		 * intent.putExtra(SearchManager.QUERY, getActionBar().getTitle()); //
		 * catch event that there's no activity to handle intent if
		 * (intent.resolveActivity(getPackageManager()) != null) {
		 * startActivity(intent); } else { Toast.makeText(this,
		 * R.string.app_not_available, Toast.LENGTH_LONG).show(); } return true;
		 * default: return super.onOptionsItemSelected(item); }
		 */
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

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "locationClient connected", Toast.LENGTH_LONG).show();
		this.location = locationClient.getLastLocation();
		Toast.makeText(this, "current location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "locationClient disconnected", Toast.LENGTH_LONG).show();
	}
	
	public Location getLocation() {
		return location;
	}
}
