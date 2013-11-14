package com.ese2013.mensaunibe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ese2013.mensaunibe.lib.viewpagerindicator.TitlePageIndicator;
import com.ese2013.mensaunibe.util.gui.AdapterCustomFragmentPager;

public class FragmentMenuList extends Fragment {
	
	private ActivityMain main;
//	private Context context;
//	private Model model;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    
		View rootView = inflater.inflate(R.layout.fragment_menulist, container, false);
	    
		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
		
		this.main = (ActivityMain) this.getActivity();
//		this.context = inflater.getContext();
//		this.model = main.model;
	
		pager.setAdapter(buildAdapter());
		
		TitlePageIndicator indicator = (TitlePageIndicator) rootView.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
        pager.setCurrentItem((main.calcWeekDay()));
	
		return(rootView);
	}

    private PagerAdapter buildAdapter() {
        return(new AdapterCustomFragmentPager(main, getChildFragmentManager()));
    }
}