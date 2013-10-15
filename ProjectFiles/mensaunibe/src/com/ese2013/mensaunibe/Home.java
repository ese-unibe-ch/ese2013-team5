package com.ese2013.mensaunibe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;

import com.ese2013.mensaunibe.model.Model;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Home extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mNavItems;
    
    private Model model;
    
    static Button notifCount;
    static int mNotifCount = 2;

    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        this.model = new Model();

        mTitle = mDrawerTitle = getTitle();
        mNavItems = getResources().getStringArray(R.array.sidenav_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_sidenav);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.sidenav_item, mNavItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.sidenav_open,  /* "open drawer" description for accessibility */
                R.string.sidenav_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
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
    
    private void setNotifCount(int count){
        int mNotifCount = count;
        invalidateOptionsMenu();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
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
        /*switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }*/
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
    	switch(position) {
			case 0:
				fragment = new StartFragment();
				break;
			case 1:
				fragment = new MensaFragment();
				break;
			case 2:
				fragment = new MenuFragment();
				break;
			case 3:
				fragment = new MensamapFragment();
				break;
			case 4:
				fragment = new NotificationsFragment();
				break;
			case 5:
				fragment = new FriendsFragment();
				break;
		}
        //Fragment fragment = new MensaFragment();
        //Bundle args = new Bundle();
        //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        //fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

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
    
    /**
     * Fragment that appears in the "content_frame", shows mensalist or favourite mensa menus
     */
    public static class StartFragment extends Fragment {
    	private SimpleAdapter adapter;

        public StartFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_start, container, false);
            //int i = getArguments().getInt(ARG_PLANET_NUMBER);
            //String mensas = getResources().getStringArray(R.array.mensa_list)[i];
            
            // get the list view from the layout into a variable, it's important to fetch it from the rootView
            final ListView listview = (ListView) rootView.findViewById(R.id.menulist);
            
            ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

            final String[][] menus = {
            	{"Menu natürlich vegi","Gemüse Schnitzel «Wiener Art», Grillgemüse, Bratkartoffeln"},
            	{"Menu einfach gut","Kalbsfleischkäse an Zwiebelsauce, Bratkartoffeln, Wirsing"},
            	{"Menu voll anders","Paniertes Schweinsschnitzel mit Zitronenschnitz, Pommes Frites, Tagesgemüse, Menüsalat"}
            };
            
            // Creating an array adapter to store the list of countries
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), R.layout.list_menulist_item, menus);
            HashMap<String,String> item;
            for(int i=0;i<menus.length;i++){
              item = new HashMap<String,String>();
              item.put("line1", menus[i][0]);
              item.put("line2", menus[i][1]);
              list.add(item);
            }
            
            adapter = new SimpleAdapter(inflater.getContext(), list,
              R.layout.list_menulist_item,
              new String[] { "line1","line2" },
              new int[] {R.id.line1, R.id.line2});
            
            // setting the adapter for the ListView
            listview.setAdapter(adapter);

            Toast toast = Toast.makeText(inflater.getContext(), "Hier werden entweder alle Mensas im Überblick angezeigt oder im Fall einer gewählten Lieblingsmensa deren Menuansicht", Toast.LENGTH_LONG);
            toast.show();
            return rootView;
        }
    }
    
    /**
     * Fragment that appears in the "content_frame", shows mensalist
     */
    public static class MensaFragment extends Fragment {
    	private SimpleAdapter adapter;

        public MensaFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_mensalist, container, false);
            
            // get the list view from the layout into a variable, it's important to fetch it from the rootView
            final ListView listview = (ListView) rootView.findViewById(R.id.mensalist);
            
            // Fetch the string array from resouce arrays.xml > mensalist
            //String[] mensas = getResources().getStringArray(R.array.mensalist);
            
            
            ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

            

            final String[][] mensas = {
            	{"Mensa Bühlplatz","Strassenname 23 | Öffnungszeiten: 08:00 - 19:00"},
            	{"Mensa Gesellschaftsstrasse","Strassenname 23 | Öffnungszeiten: 09:00 - 12:00"},
            	{"Mensa und Cafeteria von Roll","Strassenname 23 | Öffnungszeiten: 08:00 - 15:00"},
            	{"Mensa Unitobler","Strassenname 23 | Öffnungszeiten: 08:00 - 18:00"},
            	{"UNIESS - Bistro Bar Lounge","Strassenname 23 | Öffnungszeiten: 08:00 - 17:00"}
            };
            
            // Creating an array adapter to store the list of countries
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), R.layout.mensalist_item, mensas);
            
            // setting the adapter for the ListView
            //listview.setAdapter(adapter);
            
            HashMap<String,String> item;
            for(int i=0;i<mensas.length;i++){
              item = new HashMap<String,String>();
              item.put("line1", mensas[i][0]);
              item.put("line2", mensas[i][1]);
              list.add(item);
            }
            
            adapter = new SimpleAdapter(inflater.getContext(), list,
              R.layout.list_mensalist_item,
              new String[] { "line1","line2" },
              new int[] {R.id.line1, R.id.line2});
            
            listview.setAdapter(adapter);
            
            Toast toast = Toast.makeText(inflater.getContext(), "Hier werden alle Mensas im Überblick angezeigt", Toast.LENGTH_LONG);
            toast.show();

           // int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()), "drawable", getActivity().getPackageName());
            //((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            //getActivity().setTitle(planet);
            return rootView;
        }
    }
    
    /**
     * Fragment that appears in the "content_frame", shows mensalist
     */
    public static class MenuFragment extends Fragment {
        //public static final String ARG_PLANET_NUMBER = "planet_number";
    	private SimpleAdapter adapter;
    	private FragmentTabHost mTabHost;

        public MenuFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_menulist, container, false);
            
//            mTabHost = new FragmentTabHost(inflater.getContext());
//            mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.fragment1);
//
//            mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("Simple"),
//                    FragmentStackSupport.CountingFragment.class, null);
//            mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator("Contacts"),
//                    LoaderCursorSupport.CursorLoaderListFragment.class, null);
//            mTabHost.addTab(mTabHost.newTabSpec("custom").setIndicator("Custom"),
//                    LoaderCustomSupport.AppListFragment.class, null);
//            mTabHost.addTab(mTabHost.newTabSpec("throttle").setIndicator("Throttle"),
//                    LoaderThrottleSupport.ThrottledLoaderListFragment.class, null);
           
            // get the list view from the layout into a variable, it's important to fetch it from the rootView
            final ListView listview = (ListView) rootView.findViewById(R.id.menulist);
            
            // Fetch the string array from resouce arrays.xml > mensalist
            //String[] menus = getResources().getStringArray(R.array.menulist);
            ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

            final String[][] menus = {
            	{"Menu natürlich vegi","Gemüse Schnitzel «Wiener Art», Grillgemüse, Bratkartoffeln"},
            	{"Menu einfach gut","Kalbsfleischkäse an Zwiebelsauce, Bratkartoffeln, Wirsing"},
            	{"Menu voll anders","Paniertes Schweinsschnitzel mit Zitronenschnitz, Pommes Frites, Tagesgemüse, Menüsalat"}
            };
            
            // Creating an array adapter to store the list of countries
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), R.layout.list_menulist_item, menus);
            HashMap<String,String> item;
            for(int i=0;i<menus.length;i++){
              item = new HashMap<String,String>();
              item.put("line1", menus[i][0]);
              item.put("line2", menus[i][1]);
              list.add(item);
            }
            
            adapter = new SimpleAdapter(inflater.getContext(), list,
              R.layout.list_menulist_item,
              new String[] { "line1","line2" },
              new int[] {R.id.line1, R.id.line2});
            
            // setting the adapter for the ListView
            listview.setAdapter(adapter);

            Toast toast = Toast.makeText(inflater.getContext(), "Hier werden alle Menus im Überblick angezeigt", Toast.LENGTH_LONG);
            toast.show();
            return rootView;
        }
    }
    
    
    /**
     * Fragment that appears in the "content_frame", shows mensamap
     */
    public static class MensamapFragment extends Fragment {
    	
    	private GoogleMap map;

        public MensamapFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_mensamap, container, false);

            // Gets the MapView from the XML layout and creates it
            //MapFragment map = rootView.findViewById(R.id.mensamap);
            //FragmentManager fragmentManager = getFragmentManager();
            //MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.mensamap);
            //MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mensamap);
            //map = mapFragment.getMap();
            //mapView.onCreate(savedInstanceState);
            
			// Do a null check to confirm that we haven't already instantiated the map.
            if (map == null) {
                // Try to obtain the map from the MapFragment.
            	map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mensamap)).getMap();
                // Check if we were successful in obtaining the map.
                if (map != null) {
                	map.setMapType(GoogleMap.MAP_TYPE_NORMAL); //added line
                    map.setMyLocationEnabled(true); 
                    map.getUiSettings().setMyLocationButtonEnabled(true); //my code did not have these and still be able to display the zoom in, zoom out, and my location button
                    map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
                	Toast toast = Toast.makeText(inflater.getContext(), "Map NOT null", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                	Toast toast = Toast.makeText(inflater.getContext(), "Map null", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

//            if (map !=null){
//            	Toast toast = Toast.makeText(inflater.getContext(), "Map not null", Toast.LENGTH_LONG);
//                toast.show();
//            	
//            } else {
//            	Toast toast = Toast.makeText(inflater.getContext(), "Map null", Toast.LENGTH_LONG);
//                toast.show();
//	        }
            // Gets to GoogleMap from the MapView and does initialization stuff
            //GoogleMap map = mapView.getMap();
            //map.getUiSettings().setMyLocationButtonEnabled(false);
            //map.setMyLocationEnabled(true);
            //map.addMarker(new MarkerOptions().position(new LatLng(50.167003,19.383262)));
            
            //myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            //myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            //myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            
            //myMap.setOnMapClickListener(this);


            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
//            try {
//                MapsInitializer.initialize(inflater.getContext());
//            } catch (GooglePlayServicesNotAvailableException e) {
//                e.printStackTrace();
//            }

            // Updates the location and zoom of the MapView
            //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
            //map.animateCamera(cameraUpdate);
            //GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mensamap)).getMap();
            //int i = getArguments().getInt(ARG_PLANET_NUMBER);
            //String mensas = getResources().getStringArray(R.array.mensa_list)[i];

            Toast toast = Toast.makeText(inflater.getContext(), "Hier werden alle Mensas auf einer Karte angezeigt", Toast.LENGTH_LONG);
            toast.show();
            return rootView;
        }
        
        @Override
        public void onDestroyView() {
            super.onDestroyView();
            map = null; // tried to prevent the app from crashing when map fragment is called twice, didn't work...
        }
    }
    
    /**
     * Fragment that appears in the "content_frame", shows mensalist
     */
    public static class NotificationsFragment extends Fragment {
    	private SimpleAdapter adapter;

        public NotificationsFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
            
            // get the list view from the layout into a variable, it's important to fetch it from the rootView
            final ListView listview = (ListView) rootView.findViewById(R.id.notifications);
            
            // Fetch the string array from resouce arrays.xml > mensalist
            //String[] notifications = getResources().getStringArray(R.array.notificationlist);
            ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
            
            final String[][] notifications = {
            	{"Message Subject","Short ellipsis from the content"},
            	{"Super Message Subject","Short ellipsis from the content"},
            	{"Nice Message Subject","Short ellipsis from the content"},
            	{"Shitty Message Subject","Short ellipsis from the content"},
            	{"Bla Message Subject","Short ellipsis from the content"}
            };
            
            // Creating an array adapter to store the list of countries
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), R.layout.list_item_1line, notifications);
            HashMap<String,String> item;
            for(int i=0;i<notifications.length;i++){
              item = new HashMap<String,String>();
              item.put("line1", notifications[i][0]);
              item.put("line2", notifications[i][1]);
              list.add(item);
            }
            
            adapter = new SimpleAdapter(inflater.getContext(), list,
              R.layout.list_notificationlist_item,
              new String[] { "line1","line2" },
              new int[] {R.id.line1, R.id.line2});
            
            // setting the adapter for the ListView
            listview.setAdapter(adapter);

            Toast toast = Toast.makeText(inflater.getContext(), "Hier werden alle Notifications angezeigt", Toast.LENGTH_LONG);
            toast.show();
            return rootView;
        }
    }
    
    /**
     * Fragment that appears in the "content_frame", shows friends
     */
    public static class FriendsFragment extends Fragment {
    	private SimpleAdapter adapter;

        public FriendsFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
            
            // get the list view from the layout into a variable, it's important to fetch it from the rootView
            final ListView listview = (ListView) rootView.findViewById(R.id.friendslist);
            
            // Fetch the string array from resouce arrays.xml > mensalist
            //String[] friendslist = getResources().getStringArray(R.array.friendlist);
            ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

            final String[][] friends = {
            	{"Friend Name","Some information, maybe location?"},
            	{"Friend Name Blub","Some information, maybe location?"},
            	{"Friend Name Bli","Some information, maybe location?"},
            	{"Friend Name Ga","Some information, maybe location?"},
            	{"Friend Name Da","Some information, maybe location?"}
            };
            
            // Creating an array adapter to store the list of countries
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), R.layout.list_item_1line, friendslist);
            HashMap<String,String> item;
            for(int i=0;i<friends.length;i++){
              item = new HashMap<String,String>();
              item.put("line1", friends[i][0]);
              item.put("line2", friends[i][1]);
              list.add(item);
            }
            
            adapter = new SimpleAdapter(inflater.getContext(), list,
              R.layout.list_friendlist_item,
              new String[] { "line1","line2" },
              new int[] {R.id.line1, R.id.line2});
            
            // setting the adapter for the ListView
            listview.setAdapter(adapter);
            
            Toast toast = Toast.makeText(inflater.getContext(), "Hier werden alle Freunde angezeigt", Toast.LENGTH_LONG);
            toast.show();
            return rootView;
        }
    }
}
