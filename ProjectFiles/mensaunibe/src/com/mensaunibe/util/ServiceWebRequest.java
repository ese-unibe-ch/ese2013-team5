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
import java.util.Scanner;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mensaunibe.app.controller.ActivityMain;
import com.mensaunibe.lib.dialogs.SimpleDialogFragment;
import com.mensaunibe.lib.dialogs.SimpleDialogFragment.SimpleDialogBuilder;

public class ServiceWebRequest {
	
	private static final String TAG = ServiceWebRequest.class.getSimpleName();
	
	private static ActivityMain mController;
	
	public ServiceWebRequest(ActivityMain controller) {
		mController = controller;
	}
	
    public JsonObject getJSON(String url, int timeout) {
    	Log.i(TAG, "getJSON(" + url + ", " + timeout + ")");
    	
    	if (this.hasConnection(2000)) {
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
	            int status = c.getResponseCode();
	
	            switch (status) {
	                case 200:
	                case 201:                    
	                    // parse input stream to string (with Scanner)
	                	// parse string to JsonObject with Gson JsonParser
	                	JsonObject jsonObj = new JsonParser()
	                		.parse(new Scanner(new BufferedInputStream(c.getInputStream())).useDelimiter("\\A").next())
	                		.getAsJsonObject();
	                	
	                	if (jsonObj.isJsonObject()) {
	                		return jsonObj;
	                	} else {
	                		Log.e(TAG, "getJSON(): jsonObj was not a JsonObject");
	                	}
	                default:
	                	// something went wrong
	                	Log.e(TAG, "getJSON(): Request Error, Status Code: " + status);
	            }
	
	        } catch (MalformedURLException ex) {
	            Log.e(TAG, "getJSON() Exception: " + ex);
	        } catch (ConnectException ex) {
	        	Log.e(TAG, "getJSON() Exception: " + ex);
	        	c.disconnect();
	        	retryRequest(url, timeout, ex);
	        } catch (SocketTimeoutException ex) {
	        	Log.e(TAG, "getJSON() Exception: " + ex);
	        	c.disconnect();
	        	retryRequest(url, timeout, ex);
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
			// ask to turn of wifi and fall trough to mobile network check
			SimpleDialogBuilder dialog = getDialog();
			dialog.setTitle("Internet not reachable!")
			.setMessage("Could not reach the internet through wifi, would you like to turn Wifi off and use the mobile network instead?")
			.setPositiveButtonText("Yes")
			.setNegativeButtonText("No")
			.setCancelableOnTouchOutside(false)
			.setRequestCode(444)
			.show();
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
    	ConnectivityManager cm = mController.getConnectivityManager();
	
    	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    	int timer = 0;
    	while(activeNetwork == null && timer <= 2000) {
    		Log.e(TAG, "hasConnection(): No active network is available");
    		activeNetwork = cm.getActiveNetworkInfo();
    		timer++;
    	}
    	
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
        	// wifi connection detected, let's try to reach google to see if the internet really works
        	Log.i(TAG, "hasConnection(): Wifi connected, checking internet access...");
        	if (isReachableByTcp("www.google.com", 80, timeout)) {
				Log.i(TAG, "hasConnection(): Google reachable over Wifi");
				return true;
			} else {
				Log.i(TAG, "hasConnection(): Google not reachable over Wifi");
				return false;
			}
        } else {
        	Log.i(TAG, "hasConnection(): Wifi not connected!");
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
        	Log.i(TAG, "hasConnection(): Mobile internet connected");
            return true;
        } else {
        	Log.i(TAG, "hasConnection(): No mobile internet connected!");
        }

        if (activeNetwork != null && activeNetwork.isConnected()) {
        	Log.i(TAG, "hasConnection(): Network connected");
            return true;
        } else {
        	Log.i(TAG, "hasConnection(): No network connected");
        }
        
        // if no network is available at all
        return false;
    }
    
    /**
     * As the connectivity manager only checks if wifi is turned on we need another check
     * to verify that the wifi network is also working btw. the internet is reachable.
     * 
     * InetAddress.getByName("www.domain.tld").isReachable(timeout) is not working because
     * it tries to establish a ICMP (which needs root access) or TCP Port 7 (which is usually not open)
     * so we need another way to check if the internet is really reachable.
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
    private SimpleDialogBuilder getDialog() {
    	Log.i(TAG, "getDialog()");
    	return SimpleDialogFragment.createBuilder(mController.getContext(), mController.getSupportFragmentManager());
    }
}
