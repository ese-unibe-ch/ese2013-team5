package com.ese2013.mensaunibe.util;

import com.ese2013.mensaunibe.model.ModelMensa;
import com.google.gson.JsonObject;

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
	
	/**
	 * Constructor to create builder directly from JsonObject with direct setting of favorite
	 * 
	 * @param json JsonObject representing the mensa that should be built
	 */
	public BuilderMensa(JsonObject json, boolean isFavorite) {
		parseJSONtoMensa(json);
		this.isFavorite = isFavorite;
	}
	
	public BuilderMensa(JsonObject json) {
		parseJSONtoMensa(json);
	}

	public BuilderMensa() {
		
	}

	/**
	 * Call this after setting up the builder to get the mensa object
	 * 
	 * @return the new mensa object created accordingly to the setup of the
	 *         builder
	 */
	public ModelMensa build() {
		return new ModelMensa(this);
	}

	private void parseJSONtoMensa(JsonObject json) {
		this.id = json.get("id").getAsInt();
		this.name = json.get("name").getAsString();
		this.address = json.get("address").getAsString();
		this.city = json.get("city").getAsString();
		this.lat = json.get("lat").getAsFloat();
		this.lon = json.get("lon").getAsFloat();
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
