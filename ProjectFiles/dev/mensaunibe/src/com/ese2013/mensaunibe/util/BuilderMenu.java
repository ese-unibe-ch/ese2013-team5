package com.ese2013.mensaunibe.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.ese2013.mensaunibe.model.Mensa;
import com.ese2013.mensaunibe.model.Menu;

/**
 * Builder for menu objects
 * 
 * @author ese2013-team5
 * 
 */
public class BuilderMenu {
	private final String DEFAULT = "not available";
	private String title = DEFAULT;
	private String desc = DEFAULT;
	private String price = DEFAULT;
	private Integer week = 0;
	private String date = DEFAULT;
	private String day = DEFAULT;
	private double rating = 0;
	private Mensa mensa = null;
	private int id;
	private int mensaid;
	
	public BuilderMenu(Mensa mensa) {
		this.mensa = mensa;
	}

	// TODO push this somewhere else
	/**
	 * Constructor to create a builder directly from a JSONObject
	 * 
	 * @param json
	 *            JSONObject representing the menu that should be built
	 * @param tMensa 
	 */
	public BuilderMenu(JSONObject json, Mensa tMensa) {
		this.mensa = tMensa;
		try {
			parseJSONtoMenu(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Call this after setting up the builder to get the menu object
	 * 
	 * @return the new menu object created accordingly to the setup of the
	 *         builder
	 */
	public Menu build() {
		return new Menu(this);
	}

	private void parseJSONtoMenu(JSONObject json) throws JSONException {
		this.id = json.getInt("id");
		this.mensaid = json.getInt("mensaid");
		this.title = json.getString("title");
		this.desc = json.getString("desc");
		this.price = json.getString("price");
		this.rating = json.getDouble("rating");
		this.date = json.getString("date");
		this.day = json.getString("day");
		this.week =json.getInt("week");
	}
	
	public BuilderMenu setMensaID(int mensaid) {
		this.mensaid = mensaid;
		return this;
	}

	public int getMensaID() {
		return mensaid;
	}
	
    public BuilderMenu setMensa(Mensa mensa) {
        this.mensa = mensa;
        return this;
    }

	public Mensa getMensa() {
	        return mensa;
	}
	
	public BuilderMenu setMenuID(int id) {
		this.id = id;
		return this;
	}

	public int getMenuID() {
		return id;
	}

	public BuilderMenu setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getTitle() {
		return title;
	}
	
	public BuilderMenu setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	public String getDesc() {
		return desc;
	}
	
	public BuilderMenu setPrice(String price) {
		this.price = price;
		return this;
	}

	public String getPrice() {
		return price;
	}
	
	public BuilderMenu setRating(Double rating) {
		this.rating = rating;
		return this;
	}

	public Double getRating() {
		return rating;
	}

	public BuilderMenu setDate(String date) {
		this.date = date;
		return this;
	}

	public String getDate() {
		return date;
	}

	public BuilderMenu setDay(String day) {
		this.day = day;
		return this;
	}

	public String getDay() {
		return day;
	}
	
	public BuilderMenu setWeek(int week) {
		this.week = week;
		return this;
	}

	public int getWeek() {
		return week;
	}

}
