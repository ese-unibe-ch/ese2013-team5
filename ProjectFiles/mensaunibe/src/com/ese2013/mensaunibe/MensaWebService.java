package com.ese2013.mensaunibe;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

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
	}

	/**
	 * Requests all the mensas from the web service
	 * 
	 * @return The answer from the service (JSON) as an input stream
	 * @throws IOException
	 *             when the connection failed
	 */
	public InputStream requestAllMensas() throws IOException {
		return getHttpStream(BASE_URL + MENSAS + TOKEN);
	}

	/**
	 * Requests the weekly menu for a given mensa from the web service
	 * 
	 * @param id
	 *            The id of the mensa in the webservice
	 * @return The answer from the service (JSON) as an input stream
	 * @throws IOException
	 *             when the connection failed or an invalid id was used
	 */
	public InputStream requestMenusForMensa(int id) throws IOException {
		return getHttpStream(BASE_URL + MENSAS + "/" + id + WEEKLYPLAN + TOKEN);
	}

	private InputStream getHttpStream(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.connect();

			response = httpConn.getResponseCode();

			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception e) {
			throw new IOException("Error connecting");
		}
		return in;
	}
}