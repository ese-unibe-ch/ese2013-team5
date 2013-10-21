package com.ese2013.mensaunibe;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Model;

/**
 * Fragment that appears in the "content_frame", shows mensalist
 */
public class MensaFragment extends Fragment {
	private SimpleAdapter adapter;

	public MensaFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mensalist,
				container, false);
		Activity home = this.getActivity();
		Model model = ((Home) home).model;
	
		ArrayList <Mensa> mensalist = model.getMensas();
		

		// get the list view from the layout into a variable, it's important
		// to fetch it from the rootView
		final ListView listview = (ListView) rootView
				.findViewById(R.id.mensalist);

		// Fetch the string array from resouce arrays.xml > mensalist
		// String[] mensas =
		// getResources().getStringArray(R.array.mensalist);

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		HashMap<String, String> item;
		for (Mensa mensa : mensalist) {
			item = new HashMap<String, String>();
			item.put("line1", mensa.getName());
			item.put("line2", mensa.getStreet() + " | " + mensa.getPlz());
			list.add(item);
		}

		adapter = new SimpleAdapter(inflater.getContext(), list,
				R.layout.list_mensalist_item, new String[] { "line1",
						"line2" }, new int[] { R.id.line1, R.id.line2 });

		listview.setAdapter(adapter);

		Toast toast = Toast.makeText(inflater.getContext(),
				"Hier werden alle Mensas im †berblick angezeigt",
				Toast.LENGTH_LONG);
		toast.show();

		// int imageId =
		// getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
		// "drawable", getActivity().getPackageName());
		// ((ImageView)
		// rootView.findViewById(R.id.image)).setImageResource(imageId);
		// getActivity().setTitle(planet);
		return rootView;
	}
}

