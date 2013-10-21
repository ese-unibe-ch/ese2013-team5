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
public class MenuBuilder {
	private final String DEFAULT = "not available";
	private String title = DEFAULT;
	private String date = DEFAULT;
	private String day = DEFAULT;
	private String menuDetails = DEFAULT;
	private Mensa mensa = null;

	/**
	 * Constructor to create a builder directly from a JSONObject
	 * 
	 * @param json
	 *            JSONObject representing the menu that should be built
	 * @param tMensa 
	 */
	public MenuBuilder(JSONObject json, Mensa tMensa) {
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
		this.title = json.getString("title");
		this.date = json.getString("date");
		this.setDay(json.getString("day"));
		this.menuDetails = json.getJSONArray("menu").join(" ")
				.replace('"', ' ').replace('\\', ' ');
		System.out.println(menuDetails);
		// TODO mensa id is NOT in the menu JSON -> must b retrieved
		// elsewhere, if this is even needed
		// this.mensaId = json.getInt("id");
	}

	public MenuBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public MenuBuilder setDate(String date) {
		this.date = date;
		return this;
	}

	public String getDate() {
		return date;
	}

	public MenuBuilder setMenuDetails(String menuDetails) {
		this.menuDetails = menuDetails;
		return this;
	}

	public String getMenuDetails() {
		return menuDetails;
	}

	public MenuBuilder setMensa(Mensa mensa) {
		this.mensa = mensa;
		return this;
	}

	public Mensa getMensa() {
		return mensa;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDay() {
		return day;
	}

}
