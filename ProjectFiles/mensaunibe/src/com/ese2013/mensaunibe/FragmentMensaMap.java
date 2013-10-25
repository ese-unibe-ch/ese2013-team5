package com.ese2013.mensaunibe;


import java.util.ArrayList;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Model;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Fragment that appears in the "content_frame", shows mensamap
 */
public class FragmentMensaMap extends Fragment {

	private GoogleMap map;

	public FragmentMensaMap() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_mensamap, container, false);
		
		setUpMap(inflater);

		// Do a null check to confirm that we haven't already instantiated the map.
//		if (map == null) {
//			// Try to obtain the map from the MapFragment.
//			//map = ((MapFragment) getFragmentManager().findFragmentById(
//				//	R.id.mensamap)).getMap();
//			// Check if we were successful in obtaining the map.
//			if (map != null) {
//				map.setMapType(GoogleMap.MAP_TYPE_NORMAL); // added line
//				map.setMyLocationEnabled(true);
//				map.getUiSettings().setMyLocationButtonEnabled(true);
//				map.addMarker(new MarkerOptions()
//						.position(new LatLng(0, 0)).title("Marker"));
//				Toast toast = Toast.makeText(inflater.getContext(),
//						"Map NOT null", Toast.LENGTH_LONG);
//				toast.show();
//			} else {
//				Toast toast = Toast.makeText(inflater.getContext(),
//						"Map null", Toast.LENGTH_LONG);
//				toast.show();
//			}
//		}

		// if (map !=null){
//		 Toast toast = Toast.makeText(inflater.getContext(),
//		 "Map not null", Toast.LENGTH_LONG);
//		 toast.show();
		//
		// } else {
//		 Toast toast = Toast.makeText(inflater.getContext(), "Map null",
//		 Toast.LENGTH_LONG);
//		 toast.show();
		// }
		// Gets to GoogleMap from the MapView and does initialization stuff
		// GoogleMap map = mapView.getMap();
		// map.getUiSettings().setMyLocationButtonEnabled(false);
		// map.setMyLocationEnabled(true);
		// map.addMarker(new MarkerOptions().position(new
		// LatLng(50.167003,19.383262)));

		// myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		// myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		// myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

		// myMap.setOnMapClickListener(this);

		// Needs to call MapsInitializer before doing any
		// CameraUpdateFactory calls
		// try {
		// MapsInitializer.initialize(inflater.getContext());
		// } catch (GooglePlayServicesNotAvailableException e) {
		// e.printStackTrace();
		// }

		// Updates the location and zoom of the MapView
		// CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new
		// LatLng(43.1, -87.9), 10);
		// map.animateCamera(cameraUpdate);
		// GoogleMap map = ((MapFragment)
		// getFragmentManager().findFragmentById(R.id.mensamap)).getMap();
		// int i = getArguments().getInt(ARG_PLANET_NUMBER);
		// String mensas =
		// getResources().getStringArray(R.array.mensa_list)[i];

		Toast toast = Toast.makeText(inflater.getContext(),
				"Hier werden alle Mensas auf einer Karte angezeigt",
				Toast.LENGTH_LONG);
		toast.show();
		return rootView;
	}
		
	// Getting Google Play availability status
	//int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

		
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		//map = null; // tried to prevent the app from crashing when map
					// fragment is called twice, didn't work...
	}
	
	private void setUpMap(LayoutInflater inflater) {
		// get all mensas for their coordinates to set markers
		FragmentActivity main = this.getActivity();
		Model model = ((ActivityMain) main).model;
		ArrayList <Mensa> mensalist = model.getMensas();
		
	    // Do a null check to confirm that we have not already instantiated the map
		// doesn't really work, when the map item in the nav is tapped twice, the app crashes...
	    if (map == null) {
			Toast toast = Toast.makeText(inflater.getContext(), "Map null", Toast.LENGTH_LONG);
			toast.show();
	        map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.mensamap)).getMap();
	        // Check if we were successful in obtaining the map.
	        if (map != null) {
	        	map.getUiSettings().setMyLocationButtonEnabled(true);
	        	map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
	        	map.setMyLocationEnabled(true);
	        	for (Mensa mensa : mensalist) {
	        		map.addMarker(new MarkerOptions().position(new LatLng(mensa.getLat(),mensa.getLon())));
	        	}
	            // The Map is verified. It is now safe to manipulate the map.
	   		 	Toast toast1 = Toast.makeText(inflater.getContext(), "Map not null", Toast.LENGTH_LONG);
				toast1.show();
	        }
	    }
	}
}