package com.mensaunibe.app.views;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.ActivityMain;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.lib.viewpagerindicator.TitlePageIndicator;
import com.mensaunibe.util.gui.AdapterCustomFragmentPager;

public class FragmentStart extends Fragment {
	
	// for logging and debugging purposes
	private static final String TAG = FragmentStart.class.getSimpleName();
	
	private ActivityMain mController;
	private Context mContext;
	private DataHandler mData;
	private MensaList mModel;

	private AdapterCustomFragmentPager mAdapter;
    private ProgressBar mProgressBar;
	private Mensa mMensa;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		this.mController = (ActivityMain) getActivity();
		this.mContext = inflater.getContext();
		this.mData = mController.getDataHandler();
		this.mModel = mData.getModel();
		
//		if ( main.nearestmensa != null) {
//		this.mensa = main.nearestmensa;
//	} else {
////		Toast.makeText(context, "Nearest mensa null", Toast.LENGTH_LONG).show();
//		// this should actually be the favourite mensa
//		this.mensa = model.getMensaById(1);
//	}
		if (mModel != null) {
			this.mMensa = mModel.getMensaById(2);
		} else {
			Log.e(TAG, "mModel was null");
		}
	    
		View rootView = inflater.inflate(R.layout.fragment_start, container, false);
		
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
		
		TextView mensaDesc = (TextView) rootView.findViewById(R.id.desc);
		mensaDesc.setText(mMensa.getDesc());
	}
	
	public ProgressBar getProgressBar() {
		return mProgressBar;
	}
}