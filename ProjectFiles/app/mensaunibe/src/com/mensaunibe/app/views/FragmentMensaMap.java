package com.mensaunibe.app.views;

import java.util.List;

import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mensaunibe.R;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.app.controller.Controller.ControllerListener;
import com.mensaunibe.app.model.DataHandler;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;
import com.mensaunibe.lib.routing.Routing;
import com.mensaunibe.lib.routing.RoutingListener;
import com.mensaunibe.util.tasks.TaskProgress;

/**
 * Fragment that appears in the "content_frame", shows mensamap
 */
public class FragmentMensaMap extends Fragment implements OnMapLoadedCallback, OnMarkerClickListener, OnInfoWindowClickListener, LocationListener, RoutingListener, ControllerListener {
	
	// for logging and debugging purposes
	private static final String TAG = FragmentMensaMap.class.getSimpleName();
	
	private Controller mController;
	private DataHandler mDataHandler;
	private MensaList mModel;
	private List<Mensa> mMensaList;
	
	private ProgressBar mProgressBar;
	private SupportMapFragment mMapFragment;
	private GoogleMap mMap;
	private LocationClient mLocationClient;
	private Location mLocation;
	private LatLng mLocationTarget;
	private Polyline mRoute;
	private TaskProgress mProgressTask;
	
	// needed to communicate back from an async task to the invoking fragment
    public interface FragmentCallback {
        public void onTaskDone(PolylineOptions options);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		this.mController = Controller.getController();
		this.mDataHandler = Controller.getDataHandler();
		this.mModel = mDataHandler.getModel();
		this.mMensaList = mModel.getMensas(); // TODO: find the reason why mModel is sometimes null after not using the app for some time
		this.mLocationClient = Controller.getLocationClient();
		this.mLocation = mDataHandler.getLocation();
		this.mLocationTarget = mDataHandler.getLocationTarget();

		View rootView = inflater.inflate(R.layout.fragment_mensamap, container, false);
		
		// set up the progress bar
        mProgressBar = (ProgressBar) rootView.findViewById (R.id.progressbar);
        // add custom color to the progress bar
        mProgressBar.getProgressDrawable().setColorFilter(0xffE3003D, Mode.SRC_IN);
		
	    if (mMap == null) {
	    	Log.i(TAG, "onCreateView(): mMap was null, creating new mMapFragment instance");
	    	
	        mMapFragment = SupportMapFragment.newInstance();
	        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
	        transaction.add(R.id.frame_map, mMapFragment).commit();
	        
	        // show fake progress while the map is rendering / loading
	        mProgressTask = new TaskProgress(mProgressBar, 60);
	    }
	    
	    // register ControllerListener to get noticed when the location client has connected
	    // to get location updates again after the app was paused or stopped
	    mController.addListener(this);

		return rootView;
	}

	@Override
	public void onPause() {
		Log.i(TAG, "onPause(): remove location updates listener");
		super.onPause();
		
		if (mLocationClient != null) {
			mLocationClient.removeLocationUpdates(this);
		}
	}
	
	@Override
    public void onResume() {
		Log.i(TAG, "onResume()");
        super.onResume();
        // the map can only be fetched after onActivityCreated() from the SupportMapFragment, e.g. here...
        this.mMap = mMapFragment.getMap();
		this.setUpMap();
    }
	
	@Override
	public void onStop() {
		Log.i(TAG, "onStop()");
		super.onStop();
	}
		
	@Override
	public void onDestroyView() {
		Log.i(TAG, "onDestroyView()");
		super.onDestroyView();
	}
	
	private void setUpMap() {
		Log.i(TAG, "setUpMap()");
		// TODO: implement check for availability of GooglePlayServices
		// Getting Google Play availability status
		//int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		
        // Check if we were successful in obtaining the map.
        if (mMap != null) {
        	final LatLng BERN = new LatLng(46.955535,7.43474);
        	
        	// Set the listener that will notice the app when the map finished rendering (for fake progress to stop) 
        	mMap.setOnMapLoadedCallback(this);
        	
        	mMap.getUiSettings().setZoomControlsEnabled(true);
        	mMap.getUiSettings().setCompassEnabled(true); // only showed when not in default orientation!
        	mMap.getUiSettings().setMyLocationButtonEnabled(true);
        	mMap.setMyLocationEnabled(true);
        	mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); // options: MAP_TYPE_HYBRID, MAP_TYPE_NORMAL, MAP_TYPE_SATELLITE, MAP_TYPE_TERRAIN
        	for (Mensa mensa : mMensaList) {
        		mMap.addMarker(
        				new MarkerOptions()
        				.position(new LatLng(mensa.getLat(),mensa.getLon()))
        				.title(mensa.getName())
        				.snippet(mensa.getAddress())
        		);
        	}
        	// Move the camera instantly to LŠnggasse with a zoom of 15.
        	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BERN, 15));
        	
    		// Updates the location and zoom of the MapView
    		// CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new
    		// mMap.animateCamera(cameraUpdate);
        	
        	// enable clicks on the markers
        	mMap.setOnMarkerClickListener(this);
        	// enable clicks on info windows
        	mMap.setOnInfoWindowClickListener(this);
        	
        	// initialize the location updates if the location client is connected
        	// otherwise wait for the location client to connect again in the controller
        	if (mLocationClient != null && mLocationClient.isConnected()) {
        		this.getLocationUpdates();
        	}
        	
        	if (mLocationTarget != null && mLocation != null) {
        		Log.i(TAG, "Drawing route to  clicked or closest mensa");
        		displayRoute(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), mLocationTarget, false);
        	} else {
        		Log.i(TAG, "mLocationTarget (" + mLocationTarget + ") or mLocation (" + mLocation + ") is null");
        	}
        } else {
        	Log.e(TAG, "setUpMap(): mMap was null when trying to set up the map");
        }
	}
	
	private void getLocationUpdates() {
//		Log.i(TAG, "getLocationUpdates(), smallest displacement = " + mLocationRequest.getSmallestDisplacement());
    	if (mLocationClient != null && mLocationClient.isConnected()) {
        	// request location updates from the location client when viewing the map fragment
        	LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(3000);
            mLocationRequest.setSmallestDisplacement(5); // default is 0m which triggers updates constantly = bad
			
        	mLocationClient.requestLocationUpdates(mLocationRequest, this);
    	} else {
    		Log.e(TAG, "Could not request location upates, locclient connected? = " + mLocationClient.isConnected() + ", connecting? = " + mLocationClient.isConnecting());
    	}
	}
	
    // create a route and display on the map 
    private void displayRoute(LatLng start, LatLng end, boolean progress) {
    	// show fake progress while fetching the route
    	if (progress) {
    		mProgressTask = new TaskProgress(mProgressBar, 20);
    	}
        
    	Routing routing = new Routing(mController, Routing.TravelMode.WALKING);
        routing.registerListener(this);
        routing.execute(start, end);
    }

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO: remove debugging toast
		//Toast.makeText(mContext, "Clicked a marker, draw route..." + marker.getPosition(), Toast.LENGTH_LONG).show();
		
		// set the target mensa for location updates
		mLocationTarget = marker.getPosition();
		if (mLocation != null) {
			displayRoute(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), mLocationTarget, true);
		} else {
			Log.e(TAG, "mLocation was null, should retry to get it");
		}

		return false;
	}
	
	@Override
	public void onInfoWindowClick(Marker marker) {
		Mensa mensa = mModel.getMensaByLocation(marker.getPosition());
		Log.e(TAG, "mensa = " + mensa);
		if (mensa != null) {
			Fragment fragment = FragmentMensaDetails.newInstance(mensa);
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.replace(R.id.frame_content, fragment).commit();
		}
		// TODO: remove dev toast
		//Toast.makeText(mController, "Clicked a window, open mensadetail view..." + marker.getPosition(), Toast.LENGTH_SHORT).show();
	}
    
    // callback from the maps api when the map has loaded, this is very new
	@Override
	public void onMapLoaded() {
		if (mMap != null) {
			// notify the progress task that the map is rendered (to speed up the fake progress)
			// fake progress necessary as we almost never get a real "completion" of whatever percentage
			mProgressTask.onRendered();
		}
	}
	
	// callback from the location listener
	@Override
    public void onLocationChanged(Location location) {
		// TODO: remove dev toast
		Toast.makeText(mController, "Location updated", Toast.LENGTH_SHORT).show();
		
    	// set new current location
    	mLocation = location;
	    // redraw the route when location update is received
    	if (mLocation != null && mLocationTarget != null) {
    		displayRoute(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), mLocationTarget, false);
    	}
	}

	// routing listener callbacks
    @Override
    public void onRoutingStart() {
      // The Routing Request starts
    }

    @Override
    public void onRoutingSuccess(PolylineOptions mPolyOptions) {
    	// end the fake progress
    	mProgressTask.onRendered();
		PolylineOptions polyoptions = new PolylineOptions();
		polyoptions.color(Color.RED);
		polyoptions.width(5);
		polyoptions.addAll(mPolyOptions.getPoints());
		if ( mRoute != null ) {
			// remove any existing routes to other markers
			mRoute.remove();
		}
		  
		mRoute = mMap.addPolyline(polyoptions);

//      // Start marker
//      MarkerOptions options = new MarkerOptions();
//      options.position(start);
//      options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
//      map.addMarker(options);
//
//      // End marker
//      options = new MarkerOptions();
//      options.position(end);
//      options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));  
//      map.addMarker(options);
    }
    
    @Override
    public void onRoutingFailure() {
    	// The Routing request failed
    	Log.e(TAG, "onRoutingFailure()");
    	// end the fake progress
    	mProgressTask.onRendered();
    }

    // callbacks controller listener
	@Override
	public void onModelChanged() {
		Log.i(TAG, "onModelChanged");
	}

	@Override
	public void onLocationClientConnected() {
		Log.i(TAG, "onLocationClientConnected()");
		this.getLocationUpdates();
	}
}