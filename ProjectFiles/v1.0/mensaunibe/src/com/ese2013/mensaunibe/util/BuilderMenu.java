package com.ese2013.mensaunibe.util;

import com.ese2013.mensaunibe.model.ModelMensa;
import com.ese2013.mensaunibe.model.ModelMenu;
import com.google.gson.JsonObject;

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
	private ModelMensa mensa = null;
	private int id;
	private int mensaid;
	
	public BuilderMenu(JsonObject jsonObject, ModelMensa mensa) {
		this.mensa = mensa;
		parseJSONtoMenu(jsonObject);
	}
	
	public BuilderMenu(ModelMensa mensa) {
		this.mensa = mensa;
	}

	/**
	 * Call this after setting up the builder to get the menu object
	 * 
	 * @return the new menu object created accordingly to the setup of the
	 *         builder
	 */
	public ModelMenu build() {
		return new ModelMenu(this);
	}

	private void parseJSONtoMenu(JsonObject jsonObject) {
		this.id = jsonObject.get("id").getAsInt();
		this.mensaid = jsonObject.get("mensaid").getAsInt();
		this.title = jsonObject.get("title").getAsString();
		this.desc = jsonObject.get("desc").getAsString();
		this.price = jsonObject.get("price").getAsString();
		this.rating = jsonObject.get("rating").getAsDouble();
		this.date = jsonObject.get("date").getAsString();
		this.day = jsonObject.get("day").getAsString();
		this.week =jsonObject.get("week").getAsInt();
	}
	
	public BuilderMenu setMensaID(int mensaid) {
		this.mensaid = mensaid;
		return this;
	}

	public int getMensaID() {
		return mensaid;
	}
	
    public BuilderMenu setMensa(ModelMensa mensa) {
        this.mensa = mensa;
        return this;
    }

	public ModelMensa getMensa() {
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
