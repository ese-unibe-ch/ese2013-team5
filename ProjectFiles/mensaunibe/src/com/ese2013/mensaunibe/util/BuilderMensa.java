package com.ese2013.mensaunibe.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.ese2013.mensaunibe.model.Mensa;

/**
 * Builder for mensa objects
 * 
 * @author ese2013-team5
 * 
 */
public class BuilderMensa {
	private final String DEFAULT = "not available";
	private int id = 0;
	private String name = DEFAULT;
	private String address = DEFAULT;
	private String city = DEFAULT;
	private float lat = 0;
	private float lon = 0;
	private boolean isFavorite = false;

	public BuilderMensa() {
		
	}
	// TODO push this somewhere else!
	/**
	 * Constructor to create builder directly from JSONObject
	 * 
	 * @param json
	 *            JSONObject representing the mensa that should be built
	 */
	public BuilderMensa(JSONObject json, boolean isFavorite) {
		try {
			parseJSONtoMensa(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.isFavorite = isFavorite;
	}

	/**
	 * Call this after setting up the builder to get the mensa object
	 * 
	 * @return the new mensa object created accordingly to the setup of the
	 *         builder
	 */
	public Mensa build() {
		return new Mensa(this);
	}

	private void parseJSONtoMensa(JSONObject json) throws JSONException {
		this.id = json.getInt("id");
		this.name = json.getString("name");
		this.address = json.getString("address");
		this.city = json.getString("city");
		this.lat = Float.parseFloat(json.getString("lat"));
		this.lon = Float.parseFloat(json.getString("lon"));
	}

	public BuilderMensa setId(int id) {
		this.id = id;
		return this;
	}

	public int getId() {
		return id;
	}

	public BuilderMensa setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public BuilderMensa setAddress(String address) {
		this.address = address;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public BuilderMensa setCity(String city) {
		this.city = city;
		return this;
	}

	public String getCity() {
		return city;
	}

	public BuilderMensa setLat(float lat) {
		this.lat = lat;
		return this;
	}

	public float getLat() {
		return lat;
	}

	public BuilderMensa setLon(float lon) {
		this.lon = lon;
		return this;
	}

	public float getLon() {
		return lon;
	}
	
	public BuilderMensa setIsFavorite(boolean isfavorite) {
		this.isFavorite = isfavorite;
		return this;
	}
	
	public boolean isFavorite() {
		return isFavorite;
	}

}
