package com.ese2013.mensaunibe;

import java.util.ArrayList;
import java.util.HashMap;

import com.ese2013.mensaunibe.model.DailyPlan;
import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Menu;
import com.ese2013.mensaunibe.model.Model;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * Fragment that appears in the "content_frame", shows Menulist
 */
public class MenuFragment extends Fragment {
	
	private SimpleAdapter adapter;
	private FragmentTabHost mTabHost;

	public MenuFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_menulist,
				container, false);

		// get the list view from the layout into a variable, it's important
		// to fetch it from the rootView
		final ListView listview = (ListView) rootView
				.findViewById(R.id.menulist);

		// Fetch the string array from resouce arrays.xml > mensalist
		// String[] menus = getResources().getStringArray(R.array.menulist);
		Activity home = this.getActivity();
		Model model = ((Home) home).model;
		
		//initializing arraylists of menus for each day of the week
		ArrayList<Mensa> mensas = model.getMensas();
		ArrayList<Menu> mondayMenus = new ArrayList<Menu>();
		ArrayList<Menu> tuesdayMenus = new ArrayList<Menu>();
		ArrayList<Menu> wednesdayMenus = new ArrayList<Menu>();
		ArrayList<Menu> thursdayMenus = new ArrayList<Menu>();
		ArrayList<Menu> fridayMenus = new ArrayList<Menu>();
		
		//fill the arraylists of menus for each day of the week
		for(Mensa mensa : mensas){
			mondayMenus.addAll(mensa.getMenus("Monday"));
			tuesdayMenus.addAll(mensa.getMenus("Tuesday"));
			wednesdayMenus.addAll(mensa.getMenus("Wednesday"));
			thursdayMenus.addAll(mensa.getMenus("Thursday"));
			fridayMenus.addAll(mensa.getMenus("Friday"));
		}
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item;
		for (int i = 0; i < mondayMenus.size(); i++) {
			item = new HashMap<String, String>();
			item.put("line1", mondayMenus.get(i).getMenuDetails());
			item.put("line2", mondayMenus.get(i).getMensa().getName());
			//item.put("line3", Integer.toString(mondayMenus.get(i).getMensaId()));
			list.add(item);
		}

		adapter = new SimpleAdapter(inflater.getContext(), list,
				R.layout.list_menulist_item, new String[] { "line1",
						"line2" }, new int[] { R.id.line1, R.id.line2 });

		// setting the adapter for the ListView
		listview.setAdapter(adapter);

		Toast toast = Toast.makeText(inflater.getContext(),
				"Hier werden alle Menus im †berblick angezeigt",
				Toast.LENGTH_LONG);
		toast.show();
		return rootView;
	}
}