package com.ese2013.mensaunibe;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Model;
import com.ese2013.mensaunibe.util.AdapterCustomFragmentPager;
import com.viewpagerindicator.TitlePageIndicator;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentStart extends Fragment {
	
	private ActivityMain main;
	private Context context;
	private Mensa mensa; // Mensa object, for which details are being showed
	private Model model;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    
		View rootView = inflater.inflate(R.layout.fragment_start, container, false);
	    
		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
		
		this.main = (ActivityMain) this.getActivity();
		this.context = inflater.getContext();
		this.model = main.model;
		if ( main.nearestmensa != null) {
			this.mensa = main.nearestmensa;
		} else {
//			Toast.makeText(context, "Nearest mensa null", Toast.LENGTH_LONG).show();
			// this should actually be the favourite mensa
			this.mensa = model.getMensaById(1);
		}
		
		inflateHeader(rootView, container);
	
		pager.setAdapter(buildAdapter());
		
		TitlePageIndicator indicator = (TitlePageIndicator) rootView.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
	
		return(rootView);
	}

    private PagerAdapter buildAdapter() {
        return(new AdapterCustomFragmentPager(mensa, main, getChildFragmentManager()));
    }
  
	/** private method to show the mensa-name and -address in the
	*  view on top of the fragment
	* 
	* @param inflater
	* @param container
	*/
	private void inflateHeader(View rootView, ViewGroup container) {
		TextView mensaName = (TextView) rootView.findViewById(R.id.name);
		mensaName.setText(mensa.getName());

		TextView mensaAddress = (TextView) rootView.findViewById(R.id.address);
		mensaAddress.setText(mensa.getAddress());

		TextView mensaPlz = (TextView) rootView.findViewById(R.id.city);
		mensaPlz.setText(mensa.getCity());
	}
}