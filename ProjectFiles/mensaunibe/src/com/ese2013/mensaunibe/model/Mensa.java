package com.ese2013.mensaunibe.model;

import java.util.ArrayList;

import com.ese2013.mensaunibe.util.BuilderMensa;

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
	private String address;
	private String city;
	private float lat;
	private float lon;
	private WeeklyPlan weeklyPlan;
	private boolean isFavorite = false;

	public Mensa(BuilderMensa builder) {
		this.id = builder.getId();
		this.name = builder.getName();
		this.address = builder.getAddress();
		this.city = builder.getCity();
		this.lat = builder.getLat();
		this.lon = builder.getLon();
	}

	public WeeklyPlan getWeeklyPlan() {
		return weeklyPlan;
	}

	public ArrayList<Menu> getMenus(String day) {
		return weeklyPlan.getDailyPlan(day).getMenus();
	}
	
	public ArrayList<Menu> getAllMenus(){
		ArrayList<Menu> allMenus = new ArrayList<Menu>();
		allMenus.addAll(getMenus("Monday"));
		allMenus.addAll(getMenus("Tuesday"));
		allMenus.addAll(getMenus("Wednesday"));
		allMenus.addAll(getMenus("Thursday"));
		allMenus.addAll(getMenus("Friday"));
		return allMenus;
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

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	/**
	 * Sets whether this mensa is a favorite or not
	 * 
	 * @param isFavorite
	 *            true if favorite else false
	 */
	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
		Model.getInstance().updateLocalData();
	}

}
