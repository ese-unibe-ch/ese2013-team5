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
import com.mensaunibe.util.tasks.TaskListener;

import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Fragment that appears in the "content_frame", shows a list of all mensas, sorted
 * by the distance from the devices current position. Each item shows only a few details, has
 * a button to open the map and show the way to the mensa, has a checkbox to choose as favorite
 * and is clickable to get to the mensadetails.
 */
public class FragmentMensaList extends Fragment implements TaskListener {
	
	// for logging and debugging purposes
	private static final String TAG = FragmentMensaList.class.getSimpleName();
	
	private static Controller sController;
	private static DataHandler sDataHandler;
	private static MensaList sMensaList;
	private FragmentMensaList mFragment;
	private List<Mensa> mMensas;
	
	private AdapterCustomMensalist mAdapter;
	private CustomListViewPullToRefresh mListView;
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
		
		sController = Controller.getController();
		sDataHandler = Controller.getDataHandler();
		sMensaList = sDataHandler.getMensaList();
		mFragment = this;
		
		if (mMensas == null) {
			this.mMensas = sMensaList.getMensas();
		}
		
		View rootView = inflater.inflate(R.layout.fragment_mensalist, container, false);
		
		// set up the progress bar
		mProgressBar = (ProgressBar) rootView.findViewById (R.id.progressbar);
		// add nice color to the progress bar
		mProgressBar.getProgressDrawable().setColorFilter(0xffE3003D, Mode.SRC_IN);
		
		// get the list view from the layout into a variable, 
		// it's important to fetch it from the rootView
		mListView = (CustomListViewPullToRefresh) rootView.findViewById(R.id.mensalist);
		
		// disable scrolling when list is refreshing
		mListView.setLockScrollWhileRefreshing(false);

		// set the onRefreshListener for the pull down listview
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				sDataHandler.loadModel(mFragment);
			}
		});

		mAdapter = new AdapterCustomMensalist(sController, this, mMensas, R.layout.list_mensalist_item);

		mListView.setAdapter(mAdapter);
		
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
		sDataHandler.DBUpdateFavorite(mensa);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onTaskComplete(Object result) {
		Log.i(TAG, "onTaskComplete("+ result + ")");
		mListView.onRefreshComplete(getString(R.string.ptr_updated));
	}

	@Override
	public void onProgressUpdate(int percent) {
		//unused
	}

	@Override
	public void onRendered() {
		// unused
	}
}

