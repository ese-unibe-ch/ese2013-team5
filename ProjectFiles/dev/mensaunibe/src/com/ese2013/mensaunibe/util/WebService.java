package com.ese2013.mensaunibe.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;

/**
 * This class is responsible for connecting to the mensa web service and
 * requesting the information of the mensas
 * 
 * @author ese2013-team5
 * 
 */
public class WebService {

	public WebService() {
		HttpParams myParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(myParams, 10000);
		HttpConnectionParams.setSoTimeout(myParams, 10000);
	}
	
	/**
	 * Requests all the mensas including the menus from the web service
	 * 
	 * @return The answer from the service JSONObject
	 * @throws IOException when the request to the server failed
	 */
	public JSONObject requestAllData() throws IOException {
		return requestFromURL("http://api.031.be/mensaunibe/v1/?type=mensafull");
	}

	/**
	 * Requests all the mensas from the web service
	 * 
	 * @return The answer from the service JSONObject
	 * @throws IOException when the request to the server failed
	 */
	public JSONObject requestAllMensas() throws IOException {
		return requestFromURL("http://api.031.be/mensaunibe/v1/?type=mensa");
	}

	/**
	 * Requests the weekly menu for a given mensa from the web service
	 * 
	 * @param id
	 *            The id of the mensa in the webservice
	 * @return The answer from the service as a JSONObject
	 * @throws IOException when the request to the server failed
	 */
	public JSONObject requestMenusForMensa(int id) throws IOException { 
		int currentWeek = calcWeekNumber();
		return requestFromURL("http://api.031.be/mensaunibe/v1/?type=menu&query[mensaid]=" + id + "&query[week]=" + currentWeek);
	}
	
	/**
	 * Requests json from any url
	 * 
	 * @return The answer from the service JSONObject
	 * @throws IOException when the request to the server failed
	 */
	public JSONObject requestFromURL(String url) throws IOException {
		try {
			return convertStreamToJSON(getHttpStream(url));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("bad request to the server");
		}
	}
	
	private int calcWeekNumber() {
		Calendar calendar = new GregorianCalendar();
		Date now = new Date();   
		calendar.setTime(now);     
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	private InputStream getHttpStream(String urlString) throws IOException {
		Log.i("WebService", "making request to " + urlString);
		String userAgent = "MensaUniBe Android App dev version";
		HttpClient client = new DefaultHttpClient();
		Uri uri = Uri.parse(urlString);
		Builder uriBuilder = uri.buildUpon();
		HttpGet httpGet = new HttpGet(uriBuilder.build().toString());
		httpGet.setHeader("User-Agent", userAgent);
		HttpResponse response = client.execute(httpGet);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode == 200) {
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			return content;
		} else {
			throw new IOException("Invalid answer from server");
		}
	}

	private JSONObject convertStreamToJSON(InputStream in) throws IOException, JSONException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(in));
		String line;
		StringBuilder sb = new StringBuilder();
		while ((line = bf.readLine()) != null) {
			sb.append(line);
		}
		bf.close();
		return new JSONObject(sb.toString());
	}
}