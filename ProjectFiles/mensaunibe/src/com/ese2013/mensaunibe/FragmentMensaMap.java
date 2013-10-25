package com.ese2013.mensaunibe;


import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Model;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Fragment that appears in the "content_frame", shows mensamap
 */
public class FragmentMensaMap extends Fragment implements OnMarkerClickListener {

	private GoogleMap map;
	private Context context;
	private ActivityMain main;
	private ArrayList<Mensa> mensalist;
	private Location location;

	public FragmentMensaMap() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_mensamap, container, false);
		
		// make the app context available
		this.context = inflater.getContext();
		// get all mensas for their coordinates to set markers
		this.main = (ActivityMain) this.getActivity();
		Model model = main.model;
		this.mensalist = model.getMensas();
		
		setUpMap();


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

//		Toast toast = Toast.makeText(inflater.getContext(), "Hier werden alle Mensas auf einer Karte angezeigt", Toast.LENGTH_LONG);
//		toast.show();
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
	
	private void setUpMap() {
//		// get the app context
//		Context context = inflater.getContext();
//		// get all mensas for their coordinates to set markers
//		FragmentActivity main = this.getActivity();
//		Model model = ((ActivityMain) main).model;
//		ArrayList <Mensa> mensalist = model.getMensas();
		
	    // Do a null check to confirm that we have not already instantiated the map
		// doesn't really work, when the map item in the nav is tapped twice, the app crashes...
	    if (map == null) {
//			Toast toast = Toast.makeText(context, "Map null", Toast.LENGTH_LONG);
//			toast.show();
	        map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.mensamap)).getMap();
	        // Check if we were successful in obtaining the map.
	        if (map != null) {
	        	final LatLng BERN = new LatLng(46.955535,7.43474);
	        	map.getUiSettings().setZoomControlsEnabled(true);
	        	map.getUiSettings().setCompassEnabled(true); // only showed when not in default orientation!
	        	map.getUiSettings().setMyLocationButtonEnabled(true);
	        	map.setMyLocationEnabled(true);
	        	map.setMapType(GoogleMap.MAP_TYPE_NORMAL); // options: MAP_TYPE_HYBRID, MAP_TYPE_NORMAL, MAP_TYPE_SATELLITE, MAP_TYPE_TERRAIN
	        	for (Mensa mensa : mensalist) {
	        		map.addMarker(
	        				new MarkerOptions()
	        				.position(new LatLng(mensa.getLat(),mensa.getLon()))
	        				.title(mensa.getName())
	        				.snippet(mensa.getAddress())
	        		);
	        	}
	        	// Move the camera instantly to LŠnggasse with a zoom of 15.
	        	map.moveCamera(CameraUpdateFactory.newLatLngZoom(BERN, 15));
	        	
	        	// enable clicks on the markers
	        	map.setOnMarkerClickListener(this);
	        	
	        	// Get the current or last best known position from the main activity
	        	this.location = main.getLocation();
	        	
	        	if ( location != null ){
	                // just for testing purposes, set the marker where the blue dot is (should be like that)
	            	map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));
	            }
	        	
	        	
	        	// get the current users location from the best provider
	        	// Getting LocationManager object from System Service LOCATION_SERVICE
	        	// this is the old way I did it, the current position is not as accureate as with LocationClient
	        	// which is now implemented in the ActivityMain class and already has the position before
	        	// the map is loaded, but the LocationManager can do proximity notifications, so I kept the code
	        	// in case we need that somehow...
//	            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

	            // Creating a criteria object to retrieve provider
//	            Criteria criteria = new Criteria();

	            // Getting the name of the best provider
//	            String provider = locationManager.getBestProvider(criteria, true);

	            // Getting Current Location
	            //this.location = map.getMyLocation();
//	            if ( this.location == null ) {
//	            	this.location = locationManager.getLastKnownLocation(provider);
//	            }	            

//	            LocationListener locationListener = new LocationListener() {
//		            public void onLocationChanged(Location location) {
//		            // redraw the marker when get location update.
//		            	Toast toast = Toast.makeText(context, "Location changed", Toast.LENGTH_SHORT);
//						toast.show();
//		            	map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));
//		            }
//
//					@Override
//					public void onProviderDisabled(String arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//
//					@Override
//					public void onProviderEnabled(String arg0) {
//						// TODO Auto-generated method stub
//						
//					}
//
//					@Override
//					public void onStatusChanged(String arg0, int arg1,
//							Bundle arg2) {
//						// TODO Auto-generated method stub
//						
//					}
//	            };
//	            locationManager.requestLocationUpdates(provider, 1000, 0, locationListener);
	            // The Map is verified. It is now safe to manipulate the map.
	   		 	Toast toast1 = Toast.makeText(context, "Map not null, location = " + location, Toast.LENGTH_LONG);
				toast1.show();
	        }
	    }
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		map.addPolyline(
				new PolylineOptions()
				.add(new LatLng(location.getLatitude(), location.getLongitude()), marker.getPosition())
				.geodesic(true)
		);
		Toast toast = Toast.makeText(context, "Clicked a marker, draw route..." + marker.getPosition(), Toast.LENGTH_LONG);
		toast.show();
		return false;
	}
}