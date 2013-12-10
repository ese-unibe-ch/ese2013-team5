package com.mensaunibe.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mensaunibe.app.controller.Controller;
import com.mensaunibe.lib.dialogs.SimpleDialogFragment;
import com.mensaunibe.lib.dialogs.SimpleDialogFragment.SimpleDialogBuilder;

public class ServiceRequestManager {
	
	private static final String TAG = ServiceRequestManager.class.getSimpleName();
	
	private static Controller mController;

	private boolean mConnectionReady;
	
	public ServiceRequestManager(Controller controller) {
		mController = controller;
	}
	
	public int getLastUpdate() {
		Log.i(TAG, "getLastUpdate()");
		
		JsonObject jsonObj = getJSON("http://api.031.be/mensaunibe/v1/?type=lastupdate", 5000);
		
		if (jsonObj != null && jsonObj.isJsonObject()) {
			Log.i(TAG, "response is = " +jsonObj.get("lastupdate").getAsInt());
			return jsonObj.get("lastupdate").getAsInt();
		} else {
			Log.i(TAG, "response is -1");
			return -1;
		}
	}
	
	public JsonObject getJSON(String url, int timeout) {
		Log.i(TAG, "getJSON(" + url + ", " + timeout + ")");
		
		// TODO: possible test: remove all shared prefs (or just the key "model") and kill the database
		// to see if everything works on a new installation
		Log.e(TAG, "getJSON(): mConnectionReady = " + mConnectionReady);
		if (mConnectionReady || hasConnection(2000)) {	  
			
			HttpURLConnection c = getConnection(url, timeout);

			try {
				if (c != null) {
					int status = c.getResponseCode();
					switch (status) {
						case 200:
						case 201:                    
		                    // parse input stream to string (with Scanner)
							String response = new Scanner(new BufferedInputStream(c.getInputStream())).useDelimiter("\\A").next();
							
		                	// parse string to JsonObject with Gson JsonParser
							if ( response != null) {
								JsonObject jsonObj = new JsonParser().parse(response).getAsJsonObject();
								
								if (jsonObj.isJsonObject()) {
									return jsonObj;
								} else {
									Log.e(TAG, "getJSON(): jsonObj was not a JsonObject");
								}
							} else {
								Log.e(TAG, "getJSON(): Scanner next() was null!");
							}
						default:
		                	// something went wrong
						Log.e(TAG, "getJSON(): Request Error, Status Code: " + status);
					}
				}
			} catch (NoSuchElementException ex) {
				Log.e(TAG, "getJSON Exception: " + ex);
			} catch (IOException ex) {
				Log.e(TAG, "getJSON() Exception: " + ex);
			} finally {
				if (c != null) { 
					c.disconnect();
					Log.i(TAG, "getJSON(): Connection closed");
				}
			}
		} else {
			Log.e(TAG, "getJSON(): Internet not available, ask to turn of Wifi");
			// Prevent dialog from popping up multiple times
			if (!mController.hasDialog()) {
				getDialog();
			}
		}
		
		return null;
	}
	
	public HttpURLConnection getConnection(String url, int timeout) {
		Log.i(TAG, "getConnection()");
		HttpURLConnection c = null;
		try {
			URL u = new URL(url);
			c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setRequestProperty("Content-length", "0");
			c.setUseCaches(false);
			c.setAllowUserInteraction(false);
			c.setConnectTimeout(timeout);
			c.setReadTimeout(timeout);
			c.connect();
			
			return c;
			
		} catch (MalformedURLException ex) {
			Log.e(TAG, "getJSON() Exception: " + ex);
			if (c != null) { c.disconnect(); }
		} catch (ConnectException ex) {
			Log.e(TAG, "getJSON() Exception: " + ex);
			if (c != null) { c.disconnect(); }
			retryRequest(url, timeout, ex);
		} catch (SocketTimeoutException ex) {
			Log.e(TAG, "getJSON() Exception: " + ex);
			if (c != null) { c.disconnect(); }
			retryRequest(url, timeout, ex);
		} catch (IOException ex) {
			Log.e(TAG, "getJSON() Exception: " + ex);
			if (c != null) { c.disconnect(); }
		}
		
		return null;
	}
	
    /**
    * Checks if the device has Internet connection.
    * 
    * @return true if the phone is really connected to the Internet
    */
    public boolean hasConnection(int timeout) {
    	Log.i(TAG, "hasConnection(" + timeout +")");
    	ConnectivityManager cm = Controller.getConnectivityManager();
    	
    	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    	while(activeNetwork == null) {
    		Log.e(TAG, "hasConnection(): No active network is available, loop till its available!");
    		activeNetwork = cm.getActiveNetworkInfo();
    	}
    	
    	NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    	if (wifiNetwork != null && wifiNetwork.isConnected()) {
        	// wifi connection detected, let's try to reach google to see if the internet really works
    		Log.i(TAG, "hasConnection(): Wifi connected, checking internet access...");
    		if (verifyConnection("http://www.google.ch", timeout)) {
    			Log.i(TAG, "Google.ch reachable");
	    		if (isReachableByTcp("api.031.be", 80, timeout)) {
	    			Log.i(TAG, "hasConnection(): API reachable over Wifi");
	    			mConnectionReady = true;
	    			return true;
	    		} else {
	    			Log.e(TAG, "hasConnection(): API seems to be down, not reachable by TCP!");
	    			mConnectionReady = false;
	    			return false;
	    		}
    		} else {
    			Log.i(TAG, "hasConnection(): API not reachable over Wifi");
    			return false;
    		}
    	} else {
    		Log.i(TAG, "hasConnection(): Wifi not connected!");
    	}

    	NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    	if (mobileNetwork != null && mobileNetwork.isConnected()) {
    		Log.i(TAG, "hasConnection(): Mobile internet connected");
    		mConnectionReady = true;
    		return true;
    	} else {
    		Log.i(TAG, "hasConnection(): No mobile internet connected!");
    	}

    	if (activeNetwork != null && activeNetwork.isConnected()) {
    		Log.i(TAG, "hasConnection(): Network connected");
    		mConnectionReady = true;
    		return true;
    	} else {
    		Log.i(TAG, "hasConnection(): No network connected");
    	}
    	
        // if no network is available at all
    	mConnectionReady = false;
    	return false;
    }
    
    public boolean verifyConnection(String url, int timeout) {
    	Log.i(TAG, "verifyConnection(" + url + ", " + timeout + ")");
    	HttpURLConnection c = getConnection(url, timeout);

    	if (c != null) {
    		int status;
    		try {
    			status = c.getResponseCode();
    			switch (status) {
    				case 200:
    				case 201:
    				return true;
    				case 307:
    				Log.i(TAG, "verifyConnection(): Probably a walled garden portal!");
    				default:
    				return false;
    			}
    		} catch (IOException ex) {
    			Log.e(TAG, "verifyConnection() Exception: " + ex);
    			if (c != null) { c.disconnect(); }
    		} finally {
    			if (c != null) { c.disconnect(); }
    		}
    	}
    	
    	return false;
    }
    
    /**
     * As the connectivity manager only checks if wifi is turned on we need another check
     * to verify that the wifi network is also working btw. the internet is reachable.
     * 
     * InetAddress.getByName("www.domain.tld").isReachable(timeout) is not working because
     * it tries to establish a ICMP (which needs root access) or TCP Port 7 (which is usually not open)
     * so we need another way to check if the internet is really reachable. This actually doesn't work in
     * capitve portals like the one from unibe or swisscom bc they send back a 307 (temporary redirect)
     * so we basically use this method to check if the api is up and for the real check of
     * full internet access via wifi we use the new method verifyConnection();
     * 
     * @param host the address to verify
     * @param port the port to connect to, we use 80 as the HTTP port is what we need
     * @param timeout the timeout to wait until returning false
     * @return true when the internet is reachable
     */
    public static boolean isReachableByTcp(String host, int port, int timeout) {
    	Log.i(TAG, "isReachableByTcp(" + host + ", " + port + ", " + timeout + ")");
    	try {
    		Socket socket = new Socket();
    		SocketAddress socketAddress = new InetSocketAddress(host, port);
    		socket.connect(socketAddress, timeout);
    		socket.close();
    		Log.i(TAG, host + " is reachable by TCP");
    		return true;
    	} catch (IOException ex) {
    		Log.e(TAG, "isReachableByTcp() Exception: " + ex);
    		return false;
    	}
    }
    
    /**
     * Retries a request after an Exception
     * @param url to request
     * @param timeout for the request
     * @param ex exception that caused the method call
     */
    private void retryRequest(String url, int timeout, Exception ex) {
    	Log.i(TAG, "retryRequest(" + url + ", " + timeout + ", " + ex + ")");
    	getJSON(url, timeout);
    }
    
    /**
     * Gets a dialog to confirm / deny
     * @return an instance of SimpleDialogBuilder
     */
    private static void getDialog() {
    	// TODO: Put the strings into the xml file and translate them!
    	Log.i(TAG, "getDialog()");
		// ask to turn of wifi and fall trough to mobile network check
    	SimpleDialogBuilder dialog = SimpleDialogFragment.createBuilder(mController, mController.getSupportFragmentManager());
   
    	dialog.setTitle("Internet not reachable!")
    	.setMessage("Could not reach the internet through wifi, would you like to turn Wifi off and use the mobile network instead?")
    	.setPositiveButtonText("Yes")
    	.setNegativeButtonText("No")
    	.setCancelableOnTouchOutside(false)
    	.setRequestCode(444)
    	.show();
    }
}
