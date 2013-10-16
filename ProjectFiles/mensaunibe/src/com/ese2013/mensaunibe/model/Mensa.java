package com.ese2013.mensaunibe.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.ese2013.mensaunibe.model.Mensa.MensaBuilder;

/**
 * Class representing a mensa and responsible for its menus. Needs to be
 * instantiated via the provided builder.
 * 
 * @author ese2013-team5
 * 
 */
public class Mensa {

	private int id;
	private String name;
	private String street;
	private String plz;
	private float lat;
	private float lon;
	private WeeklyPlan weeklyPlan;

	private Mensa(MensaBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.street = builder.street;
		this.plz = builder.plz;
		this.lat = builder.lat;
		this.lon = builder.lon;
	}

	public WeeklyPlan getWeeklyPlan() {
		return weeklyPlan;
	}

	public void setWeeklyPlan(WeeklyPlan weeklyPlan) {
		this.weeklyPlan = weeklyPlan;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}

	public String getStreet() {
		return street;
	}

	public String getPlz() {
		return plz;
	}

	public static class MensaBuilder {
		private int id;
		private String name;
		private String street;
		private String plz;
		private float lat;
		private float lon;

		public MensaBuilder(JSONObject json) {
			try {
				parseJSONtoMensa(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		private void parseJSONtoMensa(JSONObject json) throws JSONException {
			this.id = json.getInt("id");
			this.name = json.getString("mensa");
			this.street = json.getString("street");
			this.plz = json.getString("plz");
			this.lat = (Float) json.get("lat");
			this.lon = (Float) json.get("lon");
		}

		public MensaBuilder setId(int id) {
			this.id = id;
			return this;
		}

		public MensaBuilder setName(String name) {
			this.name = name;
			return this;
		}

		public MensaBuilder setStreet(String street) {
			this.street = street;
			return this;
		}

		public MensaBuilder setPlz(String plz) {
			this.plz = plz;
			return this;
		}

		public MensaBuilder setLat(float lat) {
			this.lat = lat;
			return this;
		}

		public MensaBuilder setLon(float lon) {
			this.lon = lon;
			return this;
		}

		public Mensa build() {
			return new Mensa(this);
		}

	}

}
