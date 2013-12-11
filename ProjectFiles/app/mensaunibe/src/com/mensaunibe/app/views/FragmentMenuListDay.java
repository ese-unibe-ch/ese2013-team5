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
import com.mensaunibe.util.tasks.TaskListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragment, shows a list of all menus for a given mensa for a given day
 */
public class FragmentMenuListDay extends Fragment implements TaskListener {
	
	// for logging and debugging purposes
	private static final String TAG = FragmentMenuListDay.class.getSimpleName();
	private static final String KEY_POSITION = "position";
	
	private static Controller sController;
	private static DataHandler sDataHandler;
	private FragmentMenuListDay mFragment;
	
	private AdapterCustomMenulist mAdapter;
	private Mensa mMensa;
	private String mDay;
	private CustomListViewPullToRefresh mListView;

	public static FragmentMenuListDay newInstance(int position, Mensa mensa) {
		Log.i(TAG, "newInstance(" + position + ", " + mensa.getName() + ")");
		
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
		
		sController = Controller.getController();
		sDataHandler = Controller.getDataHandler();
		mFragment = this;
	  
		View rootView = inflater.inflate(R.layout.fragment_menulist_page, container, false);
	
		mListView = (CustomListViewPullToRefresh) rootView.findViewById(R.id.menulist);
		
		// disable scrolling when list is refreshing
		mListView.setLockScrollWhileRefreshing(false);

		// set the onRefreshListener for the pull down listview
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				sDataHandler.loadModel(mFragment);
			}
		});
		
		if (mMensa != null) {
			sDataHandler.setCurrentMensa(mMensa);
		} else {
			mMensa = sDataHandler.getCurrentMensa();
			Log.e(TAG, "onCreateView(): mMensa was null, got it from model");
		}
		
		// build the menu list
		List<Menu> menus = new ArrayList<Menu>();
		menus.addAll(mMensa.getDailyMenus(mDay));
		
		mAdapter = new AdapterCustomMenulist(sController, this, menus, R.layout.list_menulist_item);
	
		mListView.setAdapter(mAdapter);

		return rootView;
	}
	
	public void updateList() {
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onTaskComplete(Object result) {
		Log.i(TAG, "onTaskComplete("+ result + ")");
		mListView.onRefreshComplete(getString(R.string.ptr_updated));
	}

	@Override
	public void onProgressUpdate(int percent) {
		// unused
	}

	@Override
	public void onRendered() {
		// unused
	}
}