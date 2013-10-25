package com.ese2013.mensaunibe;

import java.util.ArrayList;
import java.util.Calendar;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Menu;
import com.ese2013.mensaunibe.model.Model;
import com.ese2013.mensaunibe.util.AdapterCustomMenulist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Fragment that appears in the "content_frame", shows Menulist
 */
public class FragmentMenuList extends Fragment {
	
	//private SimpleAdapter adapter;
	private AdapterCustomMenulist adapter;

	public FragmentMenuList() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_menulist,
				container, false);

		// get the list view from the layout into a variable, it's important
		// to fetch it from the rootView
		final ListView listview = (ListView) rootView.findViewById(R.id.menulist);

		// Fetch the string array from resource arrays.xml > mensalist
		// String[] menus = getResources().getStringArray(R.array.menulist);
		FragmentActivity main = this.getActivity();
		Model model = ((ActivityMain) main).model;
		
		//initializing arraylists of menus for each day of the week
		ArrayList<Mensa> mensas = model.getMensas();
//		ArrayList<Menu> mondayMenus = new ArrayList<Menu>();
//		ArrayList<Menu> tuesdayMenus = new ArrayList<Menu>();
//		ArrayList<Menu> wednesdayMenus = new ArrayList<Menu>();
//		ArrayList<Menu> thursdayMenus = new ArrayList<Menu>();
//		ArrayList<Menu> fridayMenus = new ArrayList<Menu>();
		ArrayList<Menu> todayMenus = new ArrayList<Menu>();
		
		// get todays date and format it to fetch the right menus for today
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
		
		//fill the arraylists of menus for each day of the week
		for (Mensa mensa : mensas){
//			mondayMenus.addAll(mensa.getMenus("Monday"));
//			tuesdayMenus.addAll(mensa.getMenus("Tuesday"));
//			wednesdayMenus.addAll(mensa.getMenus("Wednesday"));
//			thursdayMenus.addAll(mensa.getMenus("Thursday"));
//			fridayMenus.addAll(mensa.getMenus("Friday"));
			todayMenus.addAll(mensa.getMenus(today));
		}
		
		adapter = new AdapterCustomMenulist(inflater.getContext(), todayMenus, R.layout.list_menulist_mensa_item);

		// setting the adapter for the ListView
		listview.setAdapter(adapter);

		Toast toast = Toast.makeText(inflater.getContext(),
				"Hier werden alle Menus im †berblick angezeigt",
				Toast.LENGTH_LONG);
		toast.show();
		return rootView;
	}
}