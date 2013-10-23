package com.ese2013.mensaunibe;

import java.util.ArrayList;
import java.util.HashMap;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Model;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ActivityMain extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mNavItems;
	
	private static Context contextOfApp;

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
		
		contextOfApp = getApplicationContext();

		// Model that is providing all the logic for the app is instantiated
		this.model = new Model();
		
		mTitle = mDrawerTitle = getTitle();
		mNavItems = getResources().getStringArray(R.array.sidenav_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_sidenav);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.sidenav_item, mNavItems));
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
		return contextOfApp;
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

	/* The click listner for ListView in the navigation drawer */
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
			fragment = new FragmentMenu();
			break;
		case 3:
			fragment = new FragmentMensaMap();
			break;
		case 4:
			fragment = new NotificationsFragment();
			break;
		case 5:
			fragment = new FragmentFriends();
			break;
		}
	
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

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


	public static class NotificationsFragment extends Fragment {
		private SimpleAdapter adapter;

		public NotificationsFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_notifications,
					container, false);

			// get the list view from the layout into a variable, it's important
			// to fetch it from the rootView
			final ListView listview = (ListView) rootView
					.findViewById(R.id.notifications);

			// Fetch the string array from resouce arrays.xml > mensalist
			// String[] notifications =
			// getResources().getStringArray(R.array.notificationlist);
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

			final String[][] notifications = {
					{ "Message Subject", "Short ellipsis from the content" },
					{ "Super Message Subject",
							"Short ellipsis from the content" },
					{ "Nice Message Subject", "Short ellipsis from the content" },
					{ "Shitty Message Subject",
							"Short ellipsis from the content" },
					{ "Bla Message Subject", "Short ellipsis from the content" } };

			// Creating an array adapter to store the list of countries
			// ArrayAdapter<String> adapter = new
			// ArrayAdapter<String>(inflater.getContext(),
			// R.layout.list_item_1line, notifications);
			HashMap<String, String> item;
			for (int i = 0; i < notifications.length; i++) {
				item = new HashMap<String, String>();
				item.put("line1", notifications[i][0]);
				item.put("line2", notifications[i][1]);
				list.add(item);
			}

			adapter = new SimpleAdapter(inflater.getContext(), list,
					R.layout.list_notificationlist_item, new String[] {
							"line1", "line2" }, new int[] { R.id.line1,
							R.id.line2 });

			// setting the adapter for the ListView
			listview.setAdapter(adapter);

			Toast toast = Toast.makeText(inflater.getContext(),
					"Hier werden alle Notifications angezeigt",
					Toast.LENGTH_LONG);
			toast.show();
			return rootView;
		}
	}

	
}
