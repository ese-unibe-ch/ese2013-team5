package com.mensaunibe.app.views;

import java.util.ArrayList;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMenuListDayFull extends Fragment {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = FragmentMenuListDayFull.class.getSimpleName();
    private static final String KEY_POSITION = "position";
    
	private Controller mController;
	private DataHandler mDataHandler;
	private MensaList mModel;
	
	private AdapterCustomMenulist mAdapter;
	private String mDay;

    public static FragmentMenuListDayFull newInstance(int position) {
    	Bundle args = new Bundle();
    	FragmentMenuListDayFull fragment = null;

        args.putInt(KEY_POSITION, position);
        
        fragment = new FragmentMenuListDayFull();
    	fragment.setArguments(args);
        
	    switch (position) {
		    case 0:
		    	fragment.setDay("Monday");
		    case 1:;
		    	fragment.setDay("Tuesday");
		    case 2:
		    	fragment.setDay("Wednesday");
		    case 3:
		    	fragment.setDay("Thursday");
		    case 4:
		    	fragment.setDay("Friday");
	    }
	    
	    return fragment;
    }

	public void setDay(String day) {
		this.mDay = day;
	}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		this.mController = Controller.getController();
		this.mDataHandler = Controller.getDataHandler();
		this.mModel = mDataHandler.getModel();
	  
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
		
		List<Mensa> mensas = mModel.getMensas();
        List<Menu> menus = new ArrayList<Menu>();
        
		// fill the arraylist with menus from all mensas
		for (Mensa mensa : mensas) {
			menus.addAll(mensa.getDailyMenus(mDay));
		}
    
        mAdapter = new AdapterCustomMenulist(mController, mModel, menus, R.layout.list_menulist_all_item);
    
        listview.setAdapter(mAdapter);

        return(rootView);
    }
}