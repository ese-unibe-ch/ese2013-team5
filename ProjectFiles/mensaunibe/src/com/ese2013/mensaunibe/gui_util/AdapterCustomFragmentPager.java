package com.ese2013.mensaunibe.gui_util;


import com.ese2013.mensaunibe.FragmentMenuListDay;
import com.ese2013.mensaunibe.model.Mensa;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdapterCustomFragmentPager extends FragmentPagerAdapter {
    private Context context = null;
	private Mensa mensa;

    public AdapterCustomFragmentPager(Mensa mensa, Context context, FragmentManager fragmgr) {
        super(fragmgr);
        this.context = context;
        this.mensa = mensa;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(int position) {
        return(FragmentMenuListDay.newInstance(position, mensa));
    }

	@Override
	public String getPageTitle(int position) {
	    switch (position) {
		    case 0:
		        return "Montag";
		    case 1:
		        return "Dienstag";
		    case 2:
		        return "Mittwoch";
		    case 3:
		        return "Donnerstag";
		    case 4:
		        return "Freitag";
		    default:
		        return null;
	    }
	}
}