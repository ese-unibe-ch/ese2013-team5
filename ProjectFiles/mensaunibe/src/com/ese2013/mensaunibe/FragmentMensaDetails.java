package com.ese2013.mensaunibe;

import java.util.ArrayList;
import java.util.HashMap;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Menu;
import com.ese2013.mensaunibe.util.AdapterCustomMenulist;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentMensaDetails extends Fragment {
	
	Activity main;
	Mensa mensa; // Mensa object, for which details are being showed
	private AdapterCustomMenulist adapter;

	
	public FragmentMensaDetails(){
		//empty constructor required for Fragment subclasses
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mensadetails, container, false);
		
		main = this.getActivity();
		mensa = ((ActivityMain)	main).currentMensa;
		
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
		
		final ListView listview = (ListView) rootView.findViewById(R.id.menus);

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
		mensaName.setText(mensa.getName());
		
		TextView mensaAddress = (TextView) rootView.findViewById(R.id.mdaddress);
		mensaAddress.setText(mensa.getAddress());
		
		TextView mensaPlz = (TextView) rootView.findViewById(R.id.mdplz);
		mensaPlz.setText(mensa.getCity());
	}
	
}
