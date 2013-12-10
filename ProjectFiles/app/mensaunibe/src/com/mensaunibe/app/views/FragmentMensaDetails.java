package com.mensaunibe.app.views;

import java.util.ArrayList;
import java.util.List;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.Menu;
import com.mensaunibe.lib.viewpagerindicator.TitlePageIndicator;
import com.mensaunibe.util.gui.AdapterCustomFragmentPager;
import com.mensaunibe.util.gui.AdapterCustomMenulist;
import com.mensaunibe.util.gui.CustomListViewPullToRefresh;
import com.mensaunibe.util.gui.CustomListViewPullToRefresh.OnRefreshListener;

import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * This class is responsible for displaying various mensa detail views
 * these are 1. "normal" mensa detail views with menu pager for the whole week
 * and 2. "simple" mensa detail view with no pager and only todays menus
 * 
 * in the second case this fragments doesn't act as the pager holder but
 * as the pager page (for favmensadetails view)!
 */
public class FragmentMensaDetails extends Fragment {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = FragmentMensaDetails.class.getSimpleName();
	
	private static Controller sController;
	private static DataHandler sDataHandler;
	
	private static ProgressBar sProgressBar;
	private boolean mHasPager;
	
	private Mensa mMensa; // needs to be static to avoid crashes on config change (rotation)!
	
	public static FragmentMensaDetails newInstance(Mensa mensa, boolean pager) {
		//Log.e(TAG, "newInstance(): mensa name = " + mensa.getName());
		FragmentMensaDetails fragment = new FragmentMensaDetails();
		fragment.setMensa(mensa);
		fragment.hasPager(pager);
		fragment.setRetainInstance(true);
		return fragment;
	}
	
	private void setMensa(Mensa mensa) {
		mMensa = mensa;
	}

	private void hasPager(boolean pager) {
		mHasPager = pager;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		sController = Controller.getController();
		sDataHandler = Controller.getDataHandler();
		
		if (mMensa == null) {
			//Log.e(TAG, "mMensa was null, getting it from savedInstanceState");
			mMensa = sDataHandler.getMensaList().getMensaById(savedInstanceState.getInt("mensaid"));
		} else if (savedInstanceState != null) {
			//Log.e(TAG, "saved mensaid in instance state");
			savedInstanceState.putInt("mensaid", mMensa.getId());
		}
		
		View rootView = inflater.inflate(R.layout.fragment_mensadetails, container, false);
		
		// set up the progress bar
        sProgressBar = (ProgressBar) rootView.findViewById (R.id.progressbar);
        // add nice color to the progress bar
        sProgressBar.getProgressDrawable().setColorFilter(0xffE3003D, Mode.SRC_IN);
        
        if (!mHasPager) {
        	// hide the progressbar because it's already there from the frame
        	sProgressBar.setVisibility(View.GONE);
        }
		
		inflateHeader(rootView);
		
		
		if (mHasPager) {
			// hide the normal menu list as it would fill up the whole visible space
			rootView.findViewById(R.id.menulist).setVisibility(View.GONE);
			
			AdapterCustomFragmentPager mAdapter = new AdapterCustomFragmentPager(mMensa, sController, getChildFragmentManager());
			
			ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
		
			pager.setAdapter(mAdapter);
			pager.setCurrentItem(mAdapter.getCurrentDay());
			
			TitlePageIndicator indicator = (TitlePageIndicator) rootView.findViewById(R.id.indicator);
	        indicator.setViewPager(pager);
		} else {
			// get the normal no pager listview
	        final CustomListViewPullToRefresh listview = (CustomListViewPullToRefresh) rootView.findViewById(R.id.menulist);
	        
			// disable scrolling when list is refreshing
			listview.setLockScrollWhileRefreshing(false);

			// override the default strings
			// TODO: put strings in xml
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
			
	        // build the menu list
	        List<Menu> menus = new ArrayList<Menu>();
	        menus.addAll(mMensa.getDailyMenus(sDataHandler.getCurrentDayName()));
		    
	        AdapterCustomMenulist mAdapter = new AdapterCustomMenulist(sController, menus, R.layout.list_menulist_item);
	    
	        listview.setAdapter(mAdapter);
		}
	
		return rootView;
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)  {
		//Log.e(TAG, "onSavedInstanceState()");
		if (mMensa != null) {
			savedInstanceState.putInt("mensaid", mMensa.getId());
		}
	}
  
	/**
	 * shows the mensa detail information on top of the fragment
	 * @param rootView
	 * @param container
	 */
	private void inflateHeader(View rootView) {
		//Log.e(TAG, "inflateHeader() mensa name = " + mMensa.getName());
		TextView mensaName = (TextView) rootView.findViewById(R.id.name);
		mensaName.setText(mMensa.getName());

		TextView mensaAddress = (TextView) rootView.findViewById(R.id.address);
		mensaAddress.setText(mMensa.getAddress());

		TextView mensaPlz = (TextView) rootView.findViewById(R.id.city);
		mensaPlz.setText(mMensa.getCity());
		
        TextView mensaDistance = (TextView) rootView.findViewById(R.id.distance);
		mensaDistance.setText(String.valueOf(Math.round(mMensa.getDistance())) + "m");
		
		TextView mensaDesc = (TextView) rootView.findViewById(R.id.desc);
		mensaDesc.setText(mMensa.getDesc());
		
		CheckBox starCheckbox = (CheckBox) rootView.findViewById(R.id.checkbox_star);
        starCheckbox.setChecked(mMensa.isFavorite());

        // set the click listener for the favorite checkbox
        final OnCheckedChangeListener starListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mMensa.setFavorite(isChecked);
				// we have to update the closest fav mensa here
				sDataHandler.loadLocation(true);
			}
        };
        starCheckbox.setOnCheckedChangeListener(starListener);
		
        ImageButton mensaMapButton = (ImageButton) rootView.findViewById(R.id.button_map);
        // set the click listener for the navigation button
        // redirect to map fragment and show route
        final OnClickListener mapListener = new OnClickListener() {
            @Override
            public void onClick(View mapbutton) {
            	sDataHandler.setLocationTarget(mMensa.getLocation());
            	Controller.getNavigationDrawer().selectItem(2);
            	// TODO: add status update
            	Toast.makeText(sController, "Navigating to Mensa", Toast.LENGTH_SHORT).show();
            }
        };
        mensaMapButton.setOnClickListener(mapListener);
        
        ImageButton mensaShareButton = (ImageButton) rootView.findViewById(R.id.button_share);
        // set the click listener for the share button
        // open share dialog
        final OnClickListener shareListener = new OnClickListener() {
            @Override
            public void onClick(View sharebutton) {
            	// TODO: add status update
    			Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, 
						sDataHandler.getSettingsManager().getData("string", "setting_sharetext") + "\n\n" +
						mMensa.getName() + 
						mMensa.getDailyMenus(sDataHandler.getCurrentDayName())
						.toString().replace("[", "").replace("]", "").replace("\n,", ""));
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
            	Toast.makeText(sController, "Share todays menus from this mensa", Toast.LENGTH_SHORT).show();
            }
        };
        mensaShareButton.setOnClickListener(shareListener);
	}
}