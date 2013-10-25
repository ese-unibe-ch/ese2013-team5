package com.ese2013.mensaunibe;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Model;
import com.ese2013.mensaunibe.util.AdapterCustomMensalist;

/**
 * Fragment that appears in the "content_frame", shows mensalist
 */
public class FragmentMensaList extends Fragment {
	private AdapterCustomMensalist adapter;
	private FragmentActivity main;

	public FragmentMensaList() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mensalist,
				container, false);
		main = this.getActivity();
		Model model = ((ActivityMain) main).model;
	
		ArrayList <Mensa> mensalist = model.getMensas();
		
//		Toast toast = Toast.makeText(main.getBaseContext(),
//				"Hier werden alle Mensas im †berblick angezeigt",
//				Toast.LENGTH_LONG);
//		toast.show();
		// get the list view from the layout into a variable, it's important
		// to fetch it from the rootView
		final ListView listview = (ListView) rootView.findViewById(R.id.mensalist);

		adapter = new AdapterCustomMensalist(getActivity(), this, mensalist, R.layout.list_mensalist_item);

		listview.setAdapter(adapter);
		//listview.setOnItemClickListener(new MensalistItemClickListener());
		((ActivityMain) main).listView = listview;
		//for(int i = 0; i < listview.getCount(); i++){
			//Object view = listview.getItemAtPosition(i);
			//((Home) home).mensalistItemIds.add(view.getId());
		//}
		
		return rootView;
	}
	
//	private class MensalistItemClickListener implements OnItemClickListener {
//
//		@Override
//		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
//			selectItem(position);
//		}
//
//	}

	public void selectItem(int position) {
		((ActivityMain) main).currentMensa = ((ActivityMain) main).model.getMensas().get(position);
		Fragment fragment = new FragmentMensaDetails();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.replace(R.id.content_frame, fragment).commit();
	}


}

