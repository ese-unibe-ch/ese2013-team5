package com.mensaunibe.app.views;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.lib.viewpagerindicator.TitlePageIndicator;
import com.mensaunibe.util.gui.AdapterCustomFragmentPager;

import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMensaDetails extends Fragment {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = FragmentMensaDetails.class.getSimpleName();
	
	private Controller mController;
	private DataHandler mDataHandler;
	
	private AdapterCustomFragmentPager mAdapter;
	private ProgressBar mProgressBar;
	private static Mensa mMensa; // needs to be static to avoid crashes on config change (rotation)!
	
	public static FragmentMensaDetails newInstance(Mensa mensa) {
		FragmentMensaDetails fragment = new FragmentMensaDetails();
		fragment.setMensa(mensa);
		return fragment;
	}
	
	private void setMensa(Mensa mensa) {
		mMensa = mensa;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		this.mController = Controller.getController();
		this.mDataHandler = Controller.getDataHandler();
		
		View rootView = inflater.inflate(R.layout.fragment_mensadetails, container, false);
		
		// set up the progress bar
        mProgressBar = (ProgressBar) rootView.findViewById (R.id.progressbar);
        // add nice color to the progress bar
        mProgressBar.getProgressDrawable().setColorFilter(0xffE3003D, Mode.SRC_IN);
		
		inflateHeader(rootView, container);
		
		mAdapter = new AdapterCustomFragmentPager(mMensa, mController, getChildFragmentManager());
		
		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
	
		pager.setAdapter(mAdapter);
		pager.setCurrentItem(mAdapter.getCurrentDay());
		
		TitlePageIndicator indicator = (TitlePageIndicator) rootView.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
	
		return rootView;
	}
  
	/** private method to show the mensa-name and -address in the
	*  view on top of the fragment
	* 
	* @param inflater
	* @param container
	*/
	private void inflateHeader(View rootView, ViewGroup container) {
		TextView mensaName = (TextView) rootView.findViewById(R.id.name);
		mensaName.setText(mMensa.getName());

		TextView mensaAddress = (TextView) rootView.findViewById(R.id.address);
		mensaAddress.setText(mMensa.getAddress());

		TextView mensaPlz = (TextView) rootView.findViewById(R.id.city);
		mensaPlz.setText(mMensa.getCity());
		
        ImageButton mensaMapButton = (ImageButton) rootView.findViewById(R.id.button_map);
        // set the click listener for the navigation button
        // redirect to map fragment and show route
        final OnClickListener mapListener = new OnClickListener() {
            @Override
            public void onClick(View mapbutton) {
            	mDataHandler.setLocationTarget(mMensa.getLocation());
            	Controller.getNavigationDrawer().selectItem(2);
            	// TODO: add status update
            	Toast.makeText(mController, "Navigating to Mensa", Toast.LENGTH_SHORT).show();
            }
        };
        mensaMapButton.setOnClickListener(mapListener);
		
		TextView mensaDesc = (TextView) rootView.findViewById(R.id.desc);
		mensaDesc.setText(mMensa.getDesc());
	}
}