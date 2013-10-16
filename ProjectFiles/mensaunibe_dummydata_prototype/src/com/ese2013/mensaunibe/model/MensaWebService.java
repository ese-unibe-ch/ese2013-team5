package com.ese2013.mensaunibe.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import android.os.StrictMode;

/**
 * This class is responsible for connecting to the mensa web service and
 * requesting the information of the mensas
 * 
 * @author ese2013-team5
 * 
 */
public class MensaWebService {

	private final String BASE_URL = "http://mensa.xonix.ch/v1/";
	private final String MENSAS = "mensas";
	private final String WEEKLYPLAN = "/weeklyplan";
	private final String TOKEN = "?tok=6112255ca02b3040711015bbbda8d955";

	public MensaWebService() {
		HttpParams myParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(myParams, 10000);
		HttpConnectionParams.setSoTimeout(myParams, 10000);

		// TODO this is absolutely horrible -> server requests must be in a
		// seperate thread
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	/**
	 * Requests all the mensas from the web service
	 * 
	 * @return The answer from the service JSONObject
	 */
	public JSONObject requestAllMensas() {
		InputStream in;
		JSONObject json = null;
		try {
			in = getHttpStream(BASE_URL + MENSAS + TOKEN);
			json = convertStreamToJSON(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
		InputStream in;
		JSONObject json = null;
		try {
			in = getHttpStream(BASE_URL + MENSAS + "/" + id + WEEKLYPLAN
					+ TOKEN);
			json = convertStreamToJSON(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
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

	private JSONObject convertStreamToJSON(InputStream in) throws IOException,
			JSONException {
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