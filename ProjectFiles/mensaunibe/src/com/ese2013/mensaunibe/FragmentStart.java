package com.ese2013.mensaunibe;

import java.util.ArrayList;
import java.util.Calendar;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Menu;
import com.ese2013.mensaunibe.model.Model;
import com.ese2013.mensaunibe.util.AdapterCustomFragmentPager;
import com.ese2013.mensaunibe.util.AdapterCustomMenulist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment that appears in the "content_frame", shows mensalist or
 * favourite mensa menus
 */
public class FragmentStart extends Fragment {
	
	ActivityMain main;
	Mensa favmensa;	// Mensa object, for which details are being showed
	private AdapterCustomFragmentPager pagerAdapter;
    private ViewPager pager;
	private AdapterCustomMenulist adapter;

	public FragmentStart() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// inflate the view
		View rootView = inflater.inflate(R.layout.fragment_mensadetails, container, false);
		
		// initialize the view pager
		//pagerAdapter = new AdapterCustomFragmentPager(getActivity(), getChildFragmentManager());

//        pager = (ViewPager) rootView.findViewById(R.id.pager);
//        pager.setAdapter(pagerAdapter);
		
		// get the model
		this.main = (ActivityMain) this.getActivity();
		Model model = main.model;
		// get the favorite mensa(s)
		// here we need to handle the case for one or multiple favorite mensas
		// for now we just assume the ExWi Mensa with ID=1 is our favorite
		int favMensaID = 1;
		// get the mensa object
		favmensa = model.getMensaById(favMensaID);
		
		inflateHeader(rootView, container);
		
		inflateMenus(inflater, rootView);

		Toast toast = Toast
				.makeText(
						inflater.getContext(),
						"Hier werden entweder alle Mensas im Überblick angezeigt oder im Fall einer gewählten Lieblingsmensa deren Menuansicht",
						Toast.LENGTH_LONG);
		toast.show();
		return rootView;
	}
	
	/** private method to show all menus of the current mensa
	 * 	in a listview under the header
	 * 
	 * @param inflater
	 * @param rootView
	 */
	private void inflateMenus(LayoutInflater inflater, View rootView) {
		// get the list view from the layout into a variable, it's important
		// to fetch it from the rootView
		final ListView listview = (ListView) rootView.findViewById(R.id.menus);
		
		// get todays name to just ask the menus for the current day
		String today = "";
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
	    if (Calendar.MONDAY == dayOfWeek) today = "Monday";
	    else if (Calendar.TUESDAY == dayOfWeek) today = "Tuesday";
	    else if (Calendar.WEDNESDAY == dayOfWeek) today = "Wednesday";
	    else if (Calendar.THURSDAY == dayOfWeek) today = "Thursday";
	    else if (Calendar.FRIDAY == dayOfWeek) today = "Friday";
	    else if (Calendar.SATURDAY == dayOfWeek) today = "Friday";
	    else if (Calendar.SUNDAY == dayOfWeek) today = "Friday";
		// and ask the menus from it / create the menulist for it
		ArrayList<Menu> menus = new ArrayList<Menu>();
		menus.addAll(favmensa.getMenus(today));
		
		adapter = new AdapterCustomMenulist(inflater.getContext(), menus, R.layout.list_menulist_item);

		// setting the adapter for the ListView
		listview.setAdapter(adapter);
	}
	
	/** private method to show the mensa-name and -address in the
	 *  view on top of the fragment
	 * 
	 * @param inflater
	 * @param container
	 */
	private void inflateHeader(View rootView, ViewGroup container) {
		//you have to get the whole view so you can get 
		//the textview with findViewById(int id)
		
		TextView mensaName = (TextView) rootView.findViewById(R.id.mdname);
		mensaName.setText(favmensa.getName());
		
		TextView mensaAddress = (TextView) rootView.findViewById(R.id.mdaddress);
		mensaAddress.setText(favmensa.getAddress());
		
		TextView mensaPlz = (TextView) rootView.findViewById(R.id.mdplz);
		mensaPlz.setText(favmensa.getCity());
	}
}
