package com.ese2013.mensaunibe;

//import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ese2013.mensaunibe.lib.viewpagerindicator.TitlePageIndicator;
import com.ese2013.mensaunibe.model.Mensa;
//import com.ese2013.mensaunibe.model.Model;
import com.ese2013.mensaunibe.util.gui.AdapterCustomFragmentPager;

public class FragmentMensaDetails extends Fragment {
	
	private ActivityMain main;
//	private Context context;
	private Mensa mensa; // Mensa object, for which details are being showed
//	private Model model;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    
		View rootView = inflater.inflate(R.layout.fragment_mensadetails, container, false);
	    
		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
		
		this.main = (ActivityMain) this.getActivity();
//		this.context = inflater.getContext();
//		this.model = main.model;
		this.mensa = main.currentMensa;
		
		inflateHeader(rootView, container);
	
		pager.setAdapter(buildAdapter());
		
		TitlePageIndicator indicator = (TitlePageIndicator) rootView.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
        pager.setCurrentItem((main.calcWeekDay()));
	
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