package com.ese2013.mensaunibe;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Model;

/**
 * Fragment that appears in the "content_frame", shows mensalist
 */
public class FragmentMensaList extends Fragment {
	private SimpleAdapter adapter;
	private Activity home;

	public FragmentMensaList() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mensalist,
				container, false);
		home = this.getActivity();
		Model model = ((ActivityMain) home).model;
	
		ArrayList <Mensa> mensalist = model.getMensas();
		

		// get the list view from the layout into a variable, it's important
		// to fetch it from the rootView
		final ListView listview = (ListView) rootView
				.findViewById(R.id.mensalist);

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		//ArrayList<Integer> mensaIds = new ArrayList<Integer>();
		HashMap<String, String> item;
		for (Mensa mensa : mensalist) {
			item = new HashMap<String, String>();
			item.put("line1", mensa.getName());
			item.put("line2", mensa.getStreet() + " | " + mensa.getPlz());
			//mensaIds.add(mensa.getId());
			list.add(item);
		}

		adapter = new SimpleAdapter(inflater.getContext(), list,
				R.layout.list_mensalist_item, new String[] { "line1",
						"line2"}, new int[] { R.id.line1, R.id.line2});

		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new MensalistItemClickListener());
		((ActivityMain) home).listView = listview;
		//for(int i = 0; i < listview.getCount(); i++){
			//Object view = listview.getItemAtPosition(i);
			//((Home) home).mensalistItemIds.add(view.getId());
		//}
		Toast toast = Toast.makeText(inflater.getContext(),
				"Hier werden alle Mensas im Überblick angezeigt",
				Toast.LENGTH_LONG);
		toast.show();
		return rootView;
	}
	
	private class MensalistItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			selectItem(position);
		}

	}

	public void selectItem(int position) {
		((ActivityMain) home).currentMensa = ((ActivityMain) home).model.getMensas().get(position);
		Fragment fragment = new FragmentMensaDetails();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
	}


}

