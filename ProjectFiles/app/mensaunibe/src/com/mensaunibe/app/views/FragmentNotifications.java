package com.mensaunibe.app.views;

import java.util.ArrayList;
import java.util.HashMap;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.util.gui.CustomListViewPullToRefresh;
import com.mensaunibe.util.gui.CustomListViewPullToRefresh.OnRefreshListener;

import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * Fragment that appears in the "content_frame", shows notifications
 * </br>
 * !!The User model is not completed and thus it is not used in this version of
 * the application!! 
 */
public class FragmentNotifications extends Fragment {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = FragmentNotifications.class.getSimpleName();
	
	private Controller mController;
	@SuppressWarnings("unused")
	private DataHandler mDataHandler;
	
	private SimpleAdapter mAdapter;
	private ProgressBar mProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		this.mController = Controller.getController();
		this.mDataHandler = Controller.getDataHandler();
		
		View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
		
		// set up the progress bar
        mProgressBar = (ProgressBar) rootView.findViewById (R.id.progressbar);
        // add nice color to the progress bar
        mProgressBar.getProgressDrawable().setColorFilter(0xffE3003D, Mode.SRC_IN);

		// get the list view from the layout into a variable, it's important
		// to fetch it from the rootView
		final CustomListViewPullToRefresh listview = (CustomListViewPullToRefresh) rootView.findViewById(R.id.notifications);
		
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
						
						listview.onRefreshComplete("Notifications neu geladen");
					}
				}, 2000);
			}
		});


		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		//TODO: remove dummy data and get data from the user model, as soon as it is implemented.
		final String[][] notifications = {
				{ "Message Subject", "Short ellipsis from the content" },
				{ "Super Message Subject",
						"Short ellipsis from the content" },
				{ "Nice Message Subject", "Short ellipsis from the content" },
				{ "Shitty Message Subject",
						"Short ellipsis from the content" },
				{ "Bla Message Subject", "Short ellipsis from the content" } };

		HashMap<String, String> item;
		for (int i = 0; i < notifications.length; i++) {
			item = new HashMap<String, String>();
			item.put("line1", notifications[i][0]);
			item.put("line2", notifications[i][1]);
			list.add(item);
		}

		mAdapter = new SimpleAdapter(inflater.getContext(), list,
				R.layout.list_notificationlist_item, new String[] {
						"line1", "line2" }, new int[] { R.id.line1,
						R.id.line2 });

		// setting the adapter for the ListView
		listview.setAdapter(mAdapter);

		Toast.makeText(mController, "Hier werden alle Notifications angezeigt", Toast.LENGTH_SHORT).show();
		
		return rootView;
	}
}
