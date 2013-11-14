package com.ese2013.mensaunibe.util.gui;


import com.ese2013.mensaunibe.FragmentMenuListAllDay;
import com.ese2013.mensaunibe.FragmentMenuListDay;
import com.ese2013.mensaunibe.model.ModelMensa;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdapterCustomFragmentPager extends FragmentPagerAdapter {
//    private Context context = null;
	private ModelMensa mensa;

    public AdapterCustomFragmentPager(ModelMensa mensa, Context context, FragmentManager fragmgr) {
        super(fragmgr);
//        this.context = context;
        this.mensa = mensa;
    }
    
    public AdapterCustomFragmentPager(Context context, FragmentManager fragmgr) {
        super(fragmgr);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(int position) {
    	if ( mensa != null ) {
    		return(FragmentMenuListDay.newInstance(position, mensa));
    	} else {
    		return(FragmentMenuListAllDay.newInstance(position));
    	}
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