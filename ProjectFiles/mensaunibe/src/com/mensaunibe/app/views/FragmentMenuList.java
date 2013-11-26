package com.mensaunibe.app.views;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.ActivityMain;
import com.mensaunibe.app.model.DataHandler;
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

public class FragmentMenuList extends Fragment {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = FragmentMenuList.class.getSimpleName();
	
	private ActivityMain mController;
	private Context mContext;
	private DataHandler mData;
	private MensaList mModel;
	
	private AdapterCustomFragmentPager mAdapter;
	private ProgressBar mProgressBar;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		this.mController = (ActivityMain) getActivity();
		this.mContext = inflater.getContext();
		this.mData = mController.getDataHandler();
		this.mModel = mData.getModel();
	    
		View rootView = inflater.inflate(R.layout.fragment_menulist, container, false);
		
		// set up the progress bar
        mProgressBar = (ProgressBar) rootView.findViewById (R.id.progressbar);
        // add nice color to the progress bar
        mProgressBar.getProgressDrawable().setColorFilter(0xffE3003D, Mode.SRC_IN);
		
		mAdapter = new AdapterCustomFragmentPager(mController, getChildFragmentManager());
	    
		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
	
		pager.setAdapter(mAdapter);
		pager.setCurrentItem(mAdapter.getCurrentDay());
		
		TitlePageIndicator indicator = (TitlePageIndicator) rootView.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
	
		return(rootView);
	}
}