package com.mensaunibe.app.views;

import java.util.List;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.lib.viewpagerindicator.TitlePageIndicator;
import com.mensaunibe.util.gui.AdapterCustomFragmentPager;

import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class FragmentMensaListPager extends Fragment {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = FragmentMensaListPager.class.getSimpleName();
	
	private Controller mController;
	private DataHandler mDataHandler;
	private MensaList mMensaList;
	private List<Mensa> mMensas;
	
	private AdapterCustomFragmentPager mAdapter;
	private ProgressBar mProgressBar;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		this.mController = Controller.getController();
		this.mDataHandler = Controller.getDataHandler();
		this.mMensaList = mDataHandler.getMensaList();
		this.mMensas = mMensaList.getFavMensas();
		
	    
		View rootView = inflater.inflate(R.layout.fragment_mensalist_pager, container, false);
		
		// set up the progress bar
        mProgressBar = (ProgressBar) rootView.findViewById (R.id.progressbar);
        // add nice color to the progress bar
        mProgressBar.getProgressDrawable().setColorFilter(0xffE3003D, Mode.SRC_IN);
		
		mAdapter = new AdapterCustomFragmentPager(mMensas, mController, getChildFragmentManager());
	    
		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
	
		pager.setAdapter(mAdapter);
		pager.setCurrentItem(0);
		
		TitlePageIndicator indicator = (TitlePageIndicator) rootView.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
	
		return rootView;
	}
}