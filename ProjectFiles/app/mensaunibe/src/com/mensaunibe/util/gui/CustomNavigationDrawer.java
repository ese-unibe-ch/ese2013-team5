package com.mensaunibe.util.gui;

import java.util.ArrayList;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CustomNavigationDrawer extends DrawerLayout {
	
	// for logging and debugging purposes
	private static final String TAG = CustomNavigationDrawer.class.getSimpleName();
	
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerListView;
	private ArrayAdapter<String> mDrawerAdapter;
	private ArrayList<FragmentNavItem> mDrawerNavItems;
	private int mDrawerContainerRes;

	public CustomNavigationDrawer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomNavigationDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomNavigationDrawer(Context context) {
		super(context);
	}
    
	// setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer), R.layout.drawer_list_item, R.id.flContent);
	@SuppressLint("NewApi")
	public void setupDrawer(ListView drawerListView, int drawerItemRes, int drawerContainerRes) {
		// Setup navigation items array
		mDrawerNavItems = new ArrayList<CustomNavigationDrawer.FragmentNavItem>();
		// Set the adapter for the list view
		mDrawerAdapter = new ArrayAdapter<String>(getActivity(), drawerItemRes, new ArrayList<String>());
		this.mDrawerContainerRes = drawerContainerRes; 
		// Setup drawer list view and related adapter
		this.mDrawerListView = drawerListView;
		drawerListView.setAdapter(mDrawerAdapter);
		// Setup item listener
		drawerListView.setOnItemClickListener(new FragmentDrawerItemListener());
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = setupDrawerToggle();
		setDrawerListener(mDrawerToggle);
		// set a custom shadow that overlays the main content when the drawer
		setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		// the method setHomeButtonEnabled is only available since API 14
		// the linter throws an error we have to supress, but we'll try to handle
		// possible errors anyway with the following if check
//		if ( Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 14 ) {
//			getActionBar().setHomeButtonEnabled(true);
//		}
	}

	// addNavItem("First", "First Fragment", FirstFragment.class)
	public void addNavItem(String navTitle, Class<? extends Fragment> fragmentClass) {
		mDrawerAdapter.add(navTitle);
		mDrawerNavItems.add(new FragmentNavItem(navTitle, fragmentClass));
	}
	
	/** 
	 * Swaps fragments in the main content view
	 */
	public void selectItem(int position) {
		FragmentNavItem navItem = mDrawerNavItems.get(position);
		Fragment fragment = null;
		try {
			fragment = navItem.getFragmentClass().newInstance();
			Bundle args = navItem.getFragmentArgs();
			if (args != null) { 
			  fragment.setArguments(args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(mDrawerContainerRes, fragment).commit();

		// Highlight the selected item, update the title, and close the drawer
		mDrawerListView.setItemChecked(position, true);
		setTitle(navItem.getTitle());
		closeDrawer(mDrawerListView);
	}
	

	public ActionBarDrawerToggle getDrawerToggle() {
		return mDrawerToggle;
	}

	
	private FragmentActivity getActivity() {
		return (FragmentActivity) getContext();
	}

	private ActionBar getActionBar() {
		ActionBar mActionBar = getActivity().getActionBar();
		return mActionBar;
	}

	private void setTitle(CharSequence title) {
		getActionBar().setTitle(title);
	}
	
	private class FragmentDrawerItemListener implements ListView.OnItemClickListener {		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}	
	}
	
	private class FragmentNavItem {
		private Class<? extends Fragment> fragmentClass;
		private String title;
		private Bundle fragmentArgs;
		
		public FragmentNavItem(String title, Class<? extends Fragment> fragmentClass) {
			this(title, fragmentClass, null);
		}
		
		public FragmentNavItem(String title, Class<? extends Fragment> fragmentClass, Bundle args) {
			this.fragmentClass = fragmentClass;
			this.fragmentArgs = args;
			this.title = title;
		}
		
		public Class<? extends Fragment> getFragmentClass() {
			return fragmentClass;
		}
		
		public String getTitle() {
			return title;
		}
		
		public Bundle getFragmentArgs() {
			return fragmentArgs;
		}
	}
	
	private ActionBarDrawerToggle setupDrawerToggle() {
		return new ActionBarDrawerToggle(getActivity(), /* host Activity */
		this, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.sidenav_open, /* "open drawer" description for accessibility */
		R.string.sidenav_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActivity().invalidateOptionsMenu(); // call onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				setTitle(Controller.getApplicationName());
				getActivity().invalidateOptionsMenu(); // call onPrepareOptionsMenu()
			}
		};
	}

	public boolean isDrawerOpen() {
		return isDrawerOpen(mDrawerListView);
	}
}