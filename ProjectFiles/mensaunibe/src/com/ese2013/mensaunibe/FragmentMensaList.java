package com.ese2013.mensaunibe;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ese2013.mensaunibe.gui_util.AdapterCustomMensalist;
import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Model;

/**
 * Fragment that appears in the "content_frame", shows mensalist
 */
public class FragmentMensaList extends Fragment {
	private ActivityMain main;
	private AdapterCustomMensalist adapter;

	public FragmentMensaList() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mensalist,
				container, false);
		this.main = (ActivityMain) this.getActivity();
		Model model = main.model;
	
		ArrayList <Mensa> mensalist = model.getMensas();
		
//		Toast.makeText(main.getBaseContext(), "Hier werden alle Mensas im †berblick angezeigt", Toast.LENGTH_LONG).show();
		// get the list view from the layout into a variable, it's important
		// to fetch it from the rootView
		final ListView listview = (ListView) rootView.findViewById(R.id.mensalist);

		adapter = new AdapterCustomMensalist(getActivity(), this, mensalist, R.layout.list_mensalist_item);

		listview.setAdapter(adapter);

		main.listView = listview;
		
		return rootView;
	}

	public void selectItem(int position) {
		((ActivityMain) main).currentMensa = ((ActivityMain) main).model.getMensas().get(position);
		Fragment fragment = new FragmentMensaDetails();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.replace(R.id.content_frame, fragment).commit();
	}


}

