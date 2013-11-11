package com.ese2013.mensaunibe;

import java.util.ArrayList;
import java.util.HashMap;

//import com.ese2013.mensaunibe.model.Model;
import com.ese2013.mensaunibe.util.gui.CustomListViewPullToRefresh;
import com.ese2013.mensaunibe.util.gui.CustomListViewPullToRefresh.OnRefreshListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * Fragment that appears in the "content_frame", shows friends
 */
public class FragmentFriends extends Fragment {
	private SimpleAdapter adapter;
//	private ActivityMain main;

	public FragmentFriends() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
		
//		this.main = (ActivityMain) this.getActivity();
//		Model model = main.model;

		// get the list view from the layout into a variable, it's important
		// to fetch it from the rootView
		final CustomListViewPullToRefresh listview = (CustomListViewPullToRefresh) rootView.findViewById(R.id.friendslist);
		
		// disable scrolling when list is refreshing
		listview.setLockScrollWhileRefreshing(false);

		// override the default strings
		listview.setTextPullToRefresh("Ziehen fŸr Update");
		listview.setTextReleaseToRefresh("Loslassen fŸr Update");
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
						
						listview.onRefreshComplete("Freunde neu geladen");
					}
				}, 2000);
			}
		});

		// Fetch the string array from resouce arrays.xml > mensalist
		// String[] friendslist =
		// getResources().getStringArray(R.array.friendlist);
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		final String[][] friends = {
				{ "Friend Name", "Some information, maybe location?" },
				{ "Friend Name Blub", "Some information, maybe location?" },
				{ "Friend Name Bli", "Some information, maybe location?" },
				{ "Friend Name Ga", "Some information, maybe location?" },
				{ "Friend Name Da", "Some information, maybe location?" } };

		// Creating an array adapter to store the list of countries
		// ArrayAdapter<String> adapter = new
		// ArrayAdapter<String>(inflater.getContext(),
		// R.layout.list_item_1line, friendslist);
		HashMap<String, String> item;
		for (int i = 0; i < friends.length; i++) {
			item = new HashMap<String, String>();
			item.put("line1", friends[i][0]);
			item.put("line2", friends[i][1]);
			list.add(item);
		}

		adapter = new SimpleAdapter(inflater.getContext(), list,
				R.layout.list_friendlist_item, new String[] { "line1",
						"line2" }, new int[] { R.id.line1, R.id.line2 });

		// setting the adapter for the ListView
		listview.setAdapter(adapter);

		Toast toast = Toast.makeText(inflater.getContext(),
				"Hier werden alle Freunde angezeigt", Toast.LENGTH_LONG);
		toast.show();
		return rootView;
	}
}
