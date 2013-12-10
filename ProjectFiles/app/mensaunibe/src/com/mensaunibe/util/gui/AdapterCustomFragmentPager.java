package com.mensaunibe.util.gui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.views.FragmentMensaDetails;
import com.mensaunibe.app.views.FragmentMenuListDay;
import com.mensaunibe.app.views.FragmentMenuListDayFull;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class AdapterCustomFragmentPager extends FragmentStatePagerAdapter {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = AdapterCustomFragmentPager.class.getSimpleName();
	
	private Controller mController;

	private Mensa mMensa;
	private List<Mensa> mMensas;
	
    public AdapterCustomFragmentPager(List<Mensa> mensas, Controller controller, FragmentManager fm) {
        super(fm);
        this.mController = controller;
        this.mMensas = mensas;
    }

    public AdapterCustomFragmentPager(Mensa mensa, Controller controller, FragmentManager fm) {
        super(fm);
        this.mController = controller;
        this.mMensa = mensa;
    }
    
    public AdapterCustomFragmentPager(Controller controller, FragmentManager fm) {
        super(fm);
        this.mController = controller;
    }

    @Override
    public int getCount() {
    	if (mMensas != null) {
    		return mMensas.size();
    	} else {
    		return 5;
    	}
    }

    @Override
    public Fragment getItem(int position) {
    	if (mMensas != null) {
    		return(FragmentMensaDetails.newInstance(mMensas.get(position), false));
    	} else if (mMensa != null) {
    		return(FragmentMenuListDay.newInstance(position, mMensa));
    	} else {
    		return(FragmentMenuListDayFull.newInstance(position));
    	}
    }

	@Override
	public String getPageTitle(int position) {
		if (mMensas != null) {
			return mMensas.get(position).getName();
		} else {
		    switch (position) {
			    case 0:
			        return mController.getString(R.string.monday);
			    case 1:
			        return mController.getString(R.string.tuesday);
			    case 2:
			        return mController.getString(R.string.wednesday);
			    case 3:
			        return mController.getString(R.string.thursday);
			    case 4:
			        return mController.getString(R.string.friday);
			    default:
			        return null;
		    }
		}
	}
	
	@Override
	public Parcelable saveState() {
		// TODO: maybe remove this override as it doesn't solve any issue as far as I see
		// don't keep any states and lose sync, doesn't actually help...
		//Log.e(TAG, "saveState()");
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