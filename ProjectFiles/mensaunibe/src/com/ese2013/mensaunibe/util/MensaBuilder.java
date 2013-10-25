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
public class MensaBuilder {
	private int id;
	private String name;
	private String street;
	private String plz;
	private float lat;
	private float lon;
	private boolean isFavorite;

	/**
	 * Constructor to create builder directly from JSONObject
	 * 
	 * @param json
	 *            JSONObject representing the mensa that should be built
	 */
	public MensaBuilder(JSONObject json, boolean isFavorite) {
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
		this.name = json.getString("mensa");
		this.street = json.getString("street");
		this.plz = json.getString("plz");
		this.lat = Float.parseFloat(json.getString("lat"));
		this.lon = Float.parseFloat(json.getString("lon"));
	}

	public MensaBuilder setId(int id) {
		this.id = id;
		return this;
	}

	public int getId() {
		return id;
	}

	public MensaBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public MensaBuilder setStreet(String street) {
		this.street = street;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public MensaBuilder setPlz(String plz) {
		this.plz = plz;
		return this;
	}

	public String getPlz() {
		return plz;
	}

	public MensaBuilder setLat(float lat) {
		this.lat = lat;
		return this;
	}

	public float getLat() {
		return lat;
	}

	public MensaBuilder setLon(float lon) {
		this.lon = lon;
		return this;
	}

	public float getLon() {
		return lon;
	}
	
	public boolean isFavorite() {
		return isFavorite;
	}

}
