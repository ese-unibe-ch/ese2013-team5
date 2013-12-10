package com.mensaunibe.app.views;

import java.util.ArrayList;
import java.util.List;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.Menu;
import com.mensaunibe.util.gui.AdapterCustomMenulist;
import com.mensaunibe.util.gui.CustomListViewPullToRefresh;
import com.mensaunibe.util.gui.CustomListViewPullToRefresh.OnRefreshListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragment, shows a list of all menus for a given mensa for a given day
 * 
 */
public class FragmentMenuListDay extends Fragment {
	
	// for logging and debugging purposes
	private static final String TAG = FragmentMenuListDay.class.getSimpleName();
	private static final String KEY_POSITION = "position";
	
	private Controller mController;
	private DataHandler mDataHandler;
	
	private AdapterCustomMenulist mAdapter;
	private Mensa mMensa;
	private String mDay;

    public static FragmentMenuListDay newInstance(int position, Mensa mensa) {
    	Log.i(TAG, "newInstance(" + position + ", " + mensa + ")");
    	
    	Bundle args = new Bundle();
    	FragmentMenuListDay fragment = null;

        args.putInt(KEY_POSITION, position);
        
	    switch (position) {
		    case 0:
		    	fragment = new FragmentMenuListDay();
		    	fragment.setArguments(args);
		    	fragment.setMensa(mensa);
		    	fragment.setDay("Monday");
		        return fragment;
		    case 1:
		    	fragment = new FragmentMenuListDay();
		    	fragment.setArguments(args);
		    	fragment.setMensa(mensa);
		    	fragment.setDay("Tuesday");
		        return fragment;
		    case 2:
		    	fragment = new FragmentMenuListDay();
		    	fragment.setArguments(args);
		    	fragment.setMensa(mensa);
		    	fragment.setDay("Wednesday");
		        return fragment;
		    case 3:
		    	fragment = new FragmentMenuListDay();
		    	fragment.setArguments(args);
		    	fragment.setMensa(mensa);
		    	fragment.setDay("Thursday");
		        return fragment;
		    case 4:
		    	fragment = new FragmentMenuListDay();
		    	fragment.setArguments(args);
		    	fragment.setMensa(mensa);
		    	fragment.setDay("Friday");
		        return fragment;
		    default:
		        return null;
	    }
    }

	private void setDay(String day) {
		this.mDay = day;
	}
	
	private void setMensa(Mensa mensa) {
		this.mMensa = mensa;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
		this.mController = Controller.getController();
		this.mDataHandler = Controller.getDataHandler();
	  
        View rootView = inflater.inflate(R.layout.fragment_menulist_page, container, false);
    
        final CustomListViewPullToRefresh listview = (CustomListViewPullToRefresh) rootView.findViewById(R.id.menulist);
        
		// disable scrolling when list is refreshing
		listview.setLockScrollWhileRefreshing(false);

		// override the default strings
		listview.setTextPullToRefresh("Ziehen für Update");
		listview.setTextReleaseToRefresh("Loslassen für Update");
		listview.setTextRefreshing("Lade Daten...");

		// set the onRefreshListener for the pull down listview
		listview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// For demo purposes, the code will pause here to
				// force a delay when invoking the refresh
				listview.postDelayed(new Runnable() {
					@Override
					public void run() {
						
						listview.onRefreshComplete("Daten neu geladen");
					}
				}, 2000);
			}
		});
		
        if (mMensa != null) {
        	mDataHandler.setCurrentMensa(mMensa);
        } else {
        	mMensa = mDataHandler.getCurrentMensa();
        	Log.e(TAG, "mMensa was null, get from model, mMensa now = " + mMensa);
        }
        
        // build the menu list
        List<Menu> menus = new ArrayList<Menu>();
        menus.addAll(mMensa.getDailyMenus(mDay));
	    
        mAdapter = new AdapterCustomMenulist(mController, menus, R.layout.list_menulist_item);
    
        listview.setAdapter(mAdapter);

        return rootView;
    }
}