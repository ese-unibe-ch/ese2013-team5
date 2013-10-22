package com.ese2013.mensaunibe;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MensaDetailsFragment extends Fragment {
	
	public MensaDetailsFragment(){
		//empty constructor required for Fragment subclasses
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mensadetails,
				container, false);
		
	final ListView listview = (ListView) rootView
			.findViewById(R.id.menus);
	
	return rootView;
	}
}
