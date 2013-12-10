package com.mensaunibe.app.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
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

public class FragmentMenuListDayFull extends Fragment {
	
	// for logging and debugging purposes
	private static final String TAG = FragmentMenuListDayFull.class.getSimpleName();
    private static final String KEY_POSITION = "position";
    
	private static Controller sController;
	private static DataHandler sDataHandler;
	private static MensaList sMensaList;
	
	private AdapterCustomMenulist mAdapter;
	private String mDay;

    public static FragmentMenuListDayFull newInstance(int position) {
    	Log.i(TAG, "newInstance(" + position + ")");
    	
		sController = Controller.getController();
		sDataHandler = Controller.getDataHandler();
		sMensaList = sDataHandler.getMensaList();
    	
    	Bundle args = new Bundle();
    	FragmentMenuListDayFull fragment = null;

        args.putInt(KEY_POSITION, position);
        
	    switch (position) {
		    case 0:
		    	fragment = new FragmentMenuListDayFull();
		    	fragment.setArguments(args);
		    	fragment.setDay("Monday");
		        return fragment;
		    case 1:
		    	fragment = new FragmentMenuListDayFull();
		    	fragment.setArguments(args);
		    	fragment.setDay("Tuesday");
		        return fragment;
		    case 2:
		    	fragment = new FragmentMenuListDayFull();
		    	fragment.setArguments(args);
		    	fragment.setDay("Wednesday");
		        return fragment;
		    case 3:
		    	fragment = new FragmentMenuListDayFull();
		    	fragment.setArguments(args);
		    	fragment.setDay("Thursday");
		        return fragment;
		    case 4:
		    	fragment = new FragmentMenuListDayFull();
		    	fragment.setArguments(args);
		    	fragment.setDay("Friday");
		        return fragment;
		    default:
		        return null;
	    }
    }

	public void setDay(String day) {
		this.mDay = day;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  
        View rootView = inflater.inflate(R.layout.fragment_menulist_page, container, false);
    
        final CustomListViewPullToRefresh listview = (CustomListViewPullToRefresh) rootView.findViewById(R.id.menulist);
        
		// disable scrolling when list is refreshing
		listview.setLockScrollWhileRefreshing(false);

		// override the default strings
		// TODO: move strings to xml
		listview.setTextPullToRefresh("Ziehen für Update");
		listview.setTextReleaseToRefresh("Loslassen für Update");
		listview.setTextRefreshing("Lade Daten...");

		// set the onRefreshListener for the pull down listview
		listview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// code to refresh the list contents goes here

				// async webrequest
				//adapter.loadData();
				
				// call listView.onRefreshComplete() when the loading is done.

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
		
		List<Mensa> mensas = sMensaList.getMensas();
        List<Menu> menus = new ArrayList<Menu>();
        
		// fill the arraylist with menus from all mensas
		for (Mensa mensa : mensas) {
			menus.addAll(mensa.getDailyMenus(mDay));
		}
		
		sortList(menus);
    
        mAdapter = new AdapterCustomMenulist(sController, this, sMensaList, menus, R.layout.list_menulist_all_item);
    
        listview.setAdapter(mAdapter);

        return(rootView);
    }
    
    // sorts all menus from all mensas according to their rating, the model can only give back
    // a sorted list (by rating) of one mensas menus
	public void sortList(List<Menu> menus) {
		Collections.sort(menus, new Comparator<Menu>(){
		    public int compare(Menu m1, Menu m2) {
		    	// sorts the list that menu with the best ratings are displayed first
		    	int result = m2.getRating().compareTo(m1.getRating());
		        return result;
		    }
		});
	}
}