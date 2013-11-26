package com.mensaunibe.app.views;

import java.util.ArrayList;
import java.util.List;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.ActivityMain;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.app.model.Menu;
import com.mensaunibe.util.gui.AdapterCustomMenulist;
import com.mensaunibe.util.gui.CustomListViewPullToRefresh;
import com.mensaunibe.util.gui.CustomListViewPullToRefresh.OnRefreshListener;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class FragmentMenuListDayFull extends Fragment {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = FragmentMenuListDayFull.class.getSimpleName();
    private static final String KEY_POSITION = "position";
    
	private ActivityMain mController;
	private Context mContext;
	private DataHandler mData;
	private MensaList mModel;
	
	private AdapterCustomMenulist mAdapter;
	private String mDay;

    public static FragmentMenuListDayFull newInstance(int position) {
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
	
//	public void setMensa(Mensa mensa) {
//		this.mensa = mensa;
//	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		this.mController = (ActivityMain) getActivity();
		this.mContext = inflater.getContext();
		this.mData = mController.getDataHandler();
		this.mModel = mData.getModel();
	  
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
		
		List<Mensa> mensas = mModel.getMensas();
        List<Menu> menus = new ArrayList<Menu>();
        
		// fill the arraylist with menus from all mensas
		for (Mensa mensa : mensas) {
			menus.addAll(mensa.getDailyMenus(mDay));
		}
    
        mAdapter = new AdapterCustomMenulist(mContext, mModel, menus, R.layout.list_menulist_all_item);
    
        listview.setAdapter(mAdapter);

        return(rootView);
    }
}