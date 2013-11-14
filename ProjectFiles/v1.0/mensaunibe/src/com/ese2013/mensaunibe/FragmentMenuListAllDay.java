/***
  Copyright (c) 2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Android Development_
    http://commonsware.com/Android
 */

package com.ese2013.mensaunibe;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ese2013.mensaunibe.model.ModelMensa;
import com.ese2013.mensaunibe.model.ModelMenu;
import com.ese2013.mensaunibe.model.Model;
import com.ese2013.mensaunibe.util.gui.AdapterCustomMenulist;
import com.ese2013.mensaunibe.util.gui.CustomListViewPullToRefresh;
import com.ese2013.mensaunibe.util.gui.CustomListViewPullToRefresh.OnRefreshListener;

public class FragmentMenuListAllDay extends Fragment {
	
	private AdapterCustomMenulist adapter;
	private Context context;
	private ActivityMain main;
	private Model model;
	
    private static final String KEY_POSITION = "position";
	private String day;
//	private Mensa mensa;

    public static FragmentMenuListAllDay newInstance(int position) {
    	Bundle args = new Bundle();
    	FragmentMenuListAllDay fragment = null;

        args.putInt(KEY_POSITION, position);
        
	    switch (position) {
		    case 0:
		    	fragment = new FragmentMenuListAllDay();
		    	fragment.setArguments(args);
//		    	fragment.setMensa(mensa);
		    	fragment.setDay("Monday");
		        return fragment;
		    case 1:
		    	fragment = new FragmentMenuListAllDay();
		    	fragment.setArguments(args);
//		    	fragment.setMensa(mensa);
		    	fragment.setDay("Tuesday");
		        return fragment;
		    case 2:
		    	fragment = new FragmentMenuListAllDay();
		    	fragment.setArguments(args);
//		    	fragment.setMensa(mensa);
		    	fragment.setDay("Wednesday");
		        return fragment;
		    case 3:
		    	fragment = new FragmentMenuListAllDay();
		    	fragment.setArguments(args);
//		    	fragment.setMensa(mensa);
		    	fragment.setDay("Thursday");
		        return fragment;
		    case 4:
		    	fragment = new FragmentMenuListAllDay();
		    	fragment.setArguments(args);
//		    	fragment.setMensa(mensa);
		    	fragment.setDay("Friday");
		        return fragment;
		    default:
		        return null;
	    }
    }

	public void setDay(String day) {
		this.day = day;
	}
	
//	public void setMensa(Mensa mensa) {
//		this.mensa = mensa;
//	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  
        View rootView = inflater.inflate(R.layout.fragment_menulist_pager, container, false);
        this.context = inflater.getContext();
        this.main = (ActivityMain) this.getActivity();
        this.model = main.model;
    
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
		
		ArrayList<ModelMensa> mensas = model.getMensas();
        ArrayList<ModelMenu> menus = new ArrayList<ModelMenu>();
        
		// fill the arraylist with menus from all mensas
		for (ModelMensa mensa : mensas){
			menus.addAll(mensa.getMenus(day));
		}
//        menus.addAll(mensa.getMenus(day));
    
        adapter = new AdapterCustomMenulist(context, menus, R.layout.list_menulist_all_item);
    
        listview.setAdapter(adapter);

        return(rootView);
    }
}