package com.ese2013.mensaunibe;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ese2013.mensaunibe.model.ModelMensa;
import com.ese2013.mensaunibe.model.Model;
import com.ese2013.mensaunibe.util.gui.AdapterCustomMensalist;
import com.ese2013.mensaunibe.util.gui.CustomListViewPullToRefresh;
import com.ese2013.mensaunibe.util.gui.CustomListViewPullToRefresh.OnRefreshListener;

/**
 * Fragment that appears in the "content_frame", shows mensalist
 */
public class FragmentMensaList extends Fragment {
	private ActivityMain main;
	private AdapterCustomMensalist adapter;

	public FragmentMensaList() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mensalist, container, false);
		this.main = (ActivityMain) this.getActivity();
		Model model = main.model;
	
		ArrayList <ModelMensa> mensalist = model.getMensas();
		
//		Toast.makeText(main.getBaseContext(), "Hier werden alle Mensas im Überblick angezeigt", Toast.LENGTH_LONG).show();
		// get the list view from the layout into a variable, it's important
		// to fetch it from the rootView
		final CustomListViewPullToRefresh listview = (CustomListViewPullToRefresh) rootView.findViewById(R.id.mensalist);
		
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
						
						listview.onRefreshComplete("Mensas neu geladen");
					}
				}, 2000);
			}
		});

		adapter = new AdapterCustomMensalist(getActivity(), this, mensalist, R.layout.list_mensalist_item);

		listview.setAdapter(adapter);

		main.listView = listview;
		
		return rootView;
	}

	public void selectItem(int position) {
		((ActivityMain) main).currentMensa = ((ActivityMain) main).model.getMensas().get(position);
		Fragment fragment = new FragmentMensaDetails();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.replace(R.id.content_frame, fragment).commit();
	}


}

