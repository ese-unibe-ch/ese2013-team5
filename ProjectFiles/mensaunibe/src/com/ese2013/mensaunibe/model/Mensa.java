package com.ese2013.mensaunibe.model;

import java.util.ArrayList;

import com.ese2013.mensaunibe.util.MensaBuilder;

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

	public Mensa(MensaBuilder builder) {
		this.id = builder.getId();
		this.name = builder.getName();
		this.street = builder.getStreet();
		this.plz = builder.getPlz();
		this.lat = builder.getLat();
		this.lon = builder.getLon();
	}

	public WeeklyPlan getWeeklyPlan() {
		return weeklyPlan;
	}
	
	public ArrayList<Menu> getMenus(String day){
		return weeklyPlan.get(day).getMenus();
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

}
