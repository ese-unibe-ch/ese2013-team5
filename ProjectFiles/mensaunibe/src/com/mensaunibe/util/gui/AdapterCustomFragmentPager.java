package com.mensaunibe.util.gui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.mensaunibe.R;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.views.FragmentMenuListDay;
import com.mensaunibe.app.views.FragmentMenuListDayFull;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class AdapterCustomFragmentPager extends FragmentStatePagerAdapter {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = AdapterCustomFragmentPager.class.getSimpleName();
	
	private Context mContext;

	private Mensa mMensa;


    public AdapterCustomFragmentPager(Mensa mensa, Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
        this.mMensa = mensa;
    }
    
    public AdapterCustomFragmentPager(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(int position) {
    	if ( mMensa != null ) {
    		return(FragmentMenuListDay.newInstance(position, mMensa));
    	} else {
    		return(FragmentMenuListDayFull.newInstance(position));
    	}
    }

	@Override
	public String getPageTitle(int position) {
	    switch (position) {
		    case 0:
		        return mContext.getString(R.string.monday);
		    case 1:
		        return mContext.getString(R.string.tuesday);
		    case 2:
		        return mContext.getString(R.string.wednesday);
		    case 3:
		        return mContext.getString(R.string.thursday);
		    case 4:
		        return mContext.getString(R.string.friday);
		    default:
		        return null;
	    }
	}
	
	@Override
	public Parcelable saveState() {
		// don't keep any states and lose sync!
		return null;
	}
	
	/**
	 * this method is JUST for the view pager
	 * It's needed because the Java week starts at sunday, eg sunday = 1
	 * the pageview array o the viewpager starts at 0 and ends at 4 for friday
	 * so we need to return the correct values for that
	 * @return
	 */
	public int getCurrentDay() {
		Calendar calendar = new GregorianCalendar();
		Date now = new Date();   
		calendar.setTime(now);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		switch(day) {
			// sunday
			case 1: 
				return 4;
			// monday
			case 2:
				return 0;
			// tuesday
			case 3:
				return 1;
			// wednesday
			case 4:
				return 2;
			// thursday
			case 5:
				return 3;
			// friday
			case 6:
				return 4;
			// saturday
			case 7:
				return 4;
			default:
				return 0;
		}
	}
}