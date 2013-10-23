package com.ese2013.mensaunibe;

import java.util.ArrayList;
import java.util.HashMap;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Menu;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MensaDetailsFragment extends Fragment {
	
	Activity home;
	Mensa mensa;		//Mensa object, whose details are being showed
	private SimpleAdapter adapter;

	
	public MensaDetailsFragment(){
		//empty constructor required for Fragment subclasses
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mensadetails,
				container, false);
		
		home = this.getActivity();
		mensa = ((Home)	home).currentMensa;
		
		inflateHeader(rootView, container);
		
		inflateMenus(inflater, rootView);
		
		return rootView;
	}

	/** private method to show all menus of the current mensa
	 * 	in a listview under the header
	 * 
	 * @param inflater
	 * @param rootView
	 */
	private void inflateMenus(LayoutInflater inflater, View rootView) {
		ArrayList<Menu> menus = new ArrayList<Menu>();
		menus.addAll(mensa.getAllMenus());
		
		//TODO: make the list items look better; it looks awful like that
		final ListView listview = (ListView) rootView
			.findViewById(R.id.menus);
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item;
		for (int i = 0; i < menus.size(); i++) {
			item = new HashMap<String, String>();
			item.put("line1", menus.get(i).getMenuDetails());
			item.put("line2", menus.get(i).getDay());
			list.add(item);
		}
		
		adapter = new SimpleAdapter(inflater.getContext(), list,
				R.layout.list_menulist_item, new String[] { "line1",
						"line2" }, new int[] { R.id.line1, R.id.line2 });

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
		mensaName.setText(mensa.getName());
		
		TextView mensaAddress = (TextView) rootView.findViewById(R.id.mdaddress);
		mensaAddress.setText(mensa.getStreet());
		
		TextView mensaPlz = (TextView) rootView.findViewById(R.id.mdplz);
		mensaPlz.setText(mensa.getPlz());
	}
	
	//TODO: implement method, so you return to the mensa list if you 
	// click return
	
}
