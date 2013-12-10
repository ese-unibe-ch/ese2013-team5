package com.mensaunibe.app.views;

import java.util.List;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.util.gui.AdapterCustomMensalist;
import com.mensaunibe.util.gui.CustomListViewPullToRefresh;
import com.mensaunibe.util.gui.CustomListViewPullToRefresh.OnRefreshListener;

import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Fragment that appears in the "content_frame", shows mensalist
 */
public class FragmentMensaList extends Fragment {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = FragmentMensaList.class.getSimpleName();
	
	private Controller mController;
	private DataHandler mDataHandler;
	private MensaList mMensaList;
	private List<Mensa> mMensas;
	
	private AdapterCustomMensalist mAdapter;
	private ProgressBar mProgressBar;
	
	public static FragmentMensaList newInstance(List<Mensa> mensalist) {
		FragmentMensaList fragment = new FragmentMensaList();
		fragment.setMensas(mensalist);
		return fragment;
	}
	
	private void setMensas(List<Mensa> mensalist) {
		mMensas = mensalist;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		this.mController = Controller.getController();
		this.mDataHandler = Controller.getDataHandler();
		this.mMensaList = mDataHandler.getMensaList();
		
		if (mMensas == null) {
			this.mMensas = mMensaList.getMensas();
		}
		
		View rootView = inflater.inflate(R.layout.fragment_mensalist, container, false);
		
		// set up the progress bar
		mProgressBar = (ProgressBar) rootView.findViewById (R.id.progressbar);
        // add nice color to the progress bar
        mProgressBar.getProgressDrawable().setColorFilter(0xffE3003D, Mode.SRC_IN);
		
		// get the list view from the layout into a variable, 
		// it's important to fetch it from the rootView
		final CustomListViewPullToRefresh listview = (CustomListViewPullToRefresh) rootView.findViewById(R.id.mensalist);
		
		// disable scrolling when list is refreshing
		listview.setLockScrollWhileRefreshing(false);

		// override the default strings
		listview.setTextPullToRefresh(getString(R.string.ptr_pull_to_refresh));
		listview.setTextReleaseToRefresh(getString(R.string.ptr_release_to_refresh));
		listview.setTextRefreshing(getString(R.string.ptr_refreshing));

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

		mAdapter = new AdapterCustomMensalist(mController, this, mMensas, R.layout.list_mensalist_item);

		listview.setAdapter(mAdapter);
		
		return rootView;
	}

	public void selectItem(int position) {
		Mensa mensa = mMensas.get(position);
		Fragment fragment = FragmentMensaDetails.newInstance(mensa, true);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.replace(R.id.frame_content, fragment).commit();
	}

	public void updateFavorite(Mensa mensa) {
		mDataHandler.DBUpdateFavorite(mensa);
		mAdapter.notifyDataSetChanged();
	}


}

