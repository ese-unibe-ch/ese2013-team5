package com.ese2013.mensaunibe;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * Fragment that appears in the "content_frame", shows mensalist or
 * favourite mensa menus
 */
public class StartFragment extends Fragment {
	private SimpleAdapter adapter;

	public StartFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_start,
				container, false);
		// int i = getArguments().getInt(ARG_PLANET_NUMBER);
		// String mensas =
		// getResources().getStringArray(R.array.mensa_list)[i];

		// get the list view from the layout into a variable, it's important
		// to fetch it from the rootView
		final ListView listview = (ListView) rootView
				.findViewById(R.id.menulist);

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		final String[][] menus = {
				{ "Menu natürlich vegi",
						"Gemüse Schnitzel «Wiener Art», Grillgemüse, Bratkartoffeln" },
				{ "Menu einfach gut",
						"Kalbsfleischkäse an Zwiebelsauce, Bratkartoffeln, Wirsing" },
				{
						"Menu voll anders",
						"Paniertes Schweinsschnitzel mit Zitronenschnitz, Pommes Frites, Tagesgemüse, Menüsalat" } };

		// Creating an array adapter to store the list of countries
		// ArrayAdapter<String> adapter = new
		// ArrayAdapter<String>(inflater.getContext(),
		// R.layout.list_menulist_item, menus);
		HashMap<String, String> item;
		for (int i = 0; i < menus.length; i++) {
			item = new HashMap<String, String>();
			item.put("line1", menus[i][0]);
			item.put("line2", menus[i][1]);
			list.add(item);
		}

		adapter = new SimpleAdapter(inflater.getContext(), list,
				R.layout.list_menulist_item, new String[] { "line1",
						"line2" }, new int[] { R.id.line1, R.id.line2 });

		// setting the adapter for the ListView
		listview.setAdapter(adapter);

		Toast toast = Toast
				.makeText(
						inflater.getContext(),
						"Hier werden entweder alle Mensas im Überblick angezeigt oder im Fall einer gewählten Lieblingsmensa deren Menuansicht",
						Toast.LENGTH_LONG);
		toast.show();
		return rootView;
	}
}
