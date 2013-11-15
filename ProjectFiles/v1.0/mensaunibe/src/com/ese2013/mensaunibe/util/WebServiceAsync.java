package com.ese2013.mensaunibe.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.ese2013.mensaunibe.ActivityMain.AsyncCallback;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.util.Log;

/**
 * This class is responsible for connecting to the mensa web service and
 * requesting the information of the mensas
 */
public class WebServiceAsync extends AsyncTask<String, String, JsonObject> {
	
	private AsyncCallback callback;

	public WebServiceAsync(AsyncCallback asyncCallback) {
		this.callback = asyncCallback;
		HttpParams myParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(myParams, 10000);
		HttpConnectionParams.setSoTimeout(myParams, 10000);
	}
	

	
	/**
	 * Requests json from any url
	 * 
	 * @return The answer from the service JSONObject
	 * @throws IOException when the request to the server failed
	 */
	public JsonObject requestFromURL(String url) throws IOException {
		try {
			return convertStreamToJSON(getHttpStream(url));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("bad request to the server");
		}
	}
	
//	private int getWeekNumber() {
//		Calendar calendar = new GregorianCalendar();
//		Date now = new Date();   
//		calendar.setTime(now);     
//		return calendar.get(Calendar.WEEK_OF_YEAR);
//	}

	private InputStream getHttpStream(String urlString) throws IOException {
		Log.i("WebService", "making request to " + urlString);
		String userAgent = "MensaUniBe Android App";
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

	private JsonObject convertStreamToJSON(InputStream content) throws IOException {
		InputStreamReader reader = new InputStreamReader(content);
		Type type = new TypeToken<JsonObject>(){}.getType();
		
		return new Gson().fromJson(reader, type);
	}
	
	@Override
    protected JsonObject doInBackground(String...urls) {
        try {
			JsonObject json = this.requestFromURL(urls[0]);
			return json;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
	
    @Override
    protected void onPostExecute(JsonObject json) {
    	callback.onTaskDone(json);

    }
}