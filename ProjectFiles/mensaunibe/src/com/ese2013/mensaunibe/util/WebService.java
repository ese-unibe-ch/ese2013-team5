package com.ese2013.mensaunibe.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

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
import android.os.AsyncTask;

/**
 * This class is responsible for connecting to the mensa web service and
 * requesting the information of the mensas
 * 
 * @author ese2013-team5
 * 
 */
public class WebService {

	private final String BASE_URL = "http://mensa.xonix.ch/v1/";
	private final String MENSAS = "mensas";
	private final String WEEKLYPLAN = "/weeklyplan";
	private final String TOKEN = "?tok=6112255ca02b3040711015bbbda8d955";

	private WebRequest request;

	public WebService() {
		
	}

	/**
	 * Requests all the mensas from the web service
	 * 
	 * @return The answer from the service JSONObject
	 */
	public JSONObject requestAllMensas() {
		JSONObject json = null;
		try {
//			request = new WebRequest(BASE_URL + MENSAS + TOKEN);
			request = new WebRequest("http://api.031.be/mensaunibe/v1/?type=mensa&holder=mensas");
			request.execute();
			json = request.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * Requests the weekly menu for a given mensa from the web service
	 * 
	 * @param id
	 *            The id of the mensa in the webservice
	 * @return The answer from the service as a JSONObject
	 */
	public JSONObject requestMenusForMensa(int id) {
		JSONObject json = null;
		
		// calculate the current week number, e.g. 43 and pass it to the api
		Calendar calendar = new GregorianCalendar();
		Date now = new Date();   
		calendar.setTime(now);     
		int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		
		try {
			//request = new WebRequest(BASE_URL + MENSAS + "/" + id + WEEKLYPLAN + TOKEN);
			request = new WebRequest("http://api.031.be/mensaunibe/v1/?type=menu&query[mensaid]=" + id + "&query[week]=" + currentWeek + "&holder=menus");
			request.execute();
			json = request.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * Requests json from any url
	 * 
	 * @return The answer from the service JSONObject
	 */
	public JSONObject requestFromURL(String url) {
		JSONObject json = null;
		try {
			request = new WebRequest(url);
			request.execute();
			json = request.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return json;
	}

	private class WebRequest extends AsyncTask<String, Void, JSONObject> {

		private String uri;

		public WebRequest(String uri) {
			this.uri = uri;
			HttpParams myParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(myParams, 10000);
			HttpConnectionParams.setSoTimeout(myParams, 10000);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			try {
				return convertStreamToJSON(getHttpStream(uri));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		private InputStream getHttpStream(String urlString) throws IOException {
			HttpClient client = new DefaultHttpClient();
			Uri uri = Uri.parse(urlString);
			Builder uriBuilder = uri.buildUpon();
			HttpGet httpGet = new HttpGet(uriBuilder.build().toString());
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

		private JSONObject convertStreamToJSON(InputStream in)
				throws IOException, JSONException {
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
}