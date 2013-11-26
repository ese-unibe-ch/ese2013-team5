package com.mensaunibe.app.views;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.ActivityMain;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.lib.viewpagerindicator.TitlePageIndicator;
import com.mensaunibe.util.gui.AdapterCustomFragmentPager;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FragmentMensaDetails extends Fragment {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = FragmentMensaDetails.class.getSimpleName();
	
	private ActivityMain mController;
	private Context mContext;
	private DataHandler mData;
	private MensaList mModel;
	
	private AdapterCustomFragmentPager mAdapter;
	private ProgressBar mProgressBar;
	private Mensa mMensa;
	
	public static FragmentMensaDetails newInstance(Mensa mensa) {
		FragmentMensaDetails fragment = new FragmentMensaDetails();
		fragment.setMensa(mensa);
		return fragment;
	}
	
	private void setMensa(Mensa mensa) {
		this.mMensa = mensa;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		this.mController = (ActivityMain) this.getActivity();
		this.mContext = inflater.getContext();
		this.mData = mController.getDataHandler();
		this.mModel = mData.getModel();
		
		View rootView = inflater.inflate(R.layout.fragment_mensadetails, container, false);
		
		// set up the progress bar
        mProgressBar = (ProgressBar) rootView.findViewById (R.id.progressbar);
        // add nice color to the progress bar
        mProgressBar.getProgressDrawable().setColorFilter(0xffE3003D, Mode.SRC_IN);
		
		inflateHeader(rootView, container);
		
		mAdapter = new AdapterCustomFragmentPager(mController, getChildFragmentManager());
		
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
}