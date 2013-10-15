package com.ese2013.mensaunibe.model;

import java.util.ArrayList;


public class Mensa {
	
	private int id;
	private String name;
	private String street;
	private String plz;
	private float lat;
	private float lon;
	private ArrayList<DailyPlan> WeeklyPlan;
	
	public Mensa(int tId, String tName, String tStreet, String tPlz, float tLat, float tLon){
		setId(tId);
		setName(tName);
		setStreet(tStreet);
		setPlz(tPlz);
		setLat(tLat);
		setLon(tLon);
		setupWeeklyPlan();
	}

	private void setupWeeklyPlan() {
		WeeklyPlan.add(new DailyPlan("Monday"));
		WeeklyPlan.add(new DailyPlan("Tuesday"));
		WeeklyPlan.add(new DailyPlan("Wednesday"));
		WeeklyPlan.add(new DailyPlan("Thursday"));
		WeeklyPlan.add(new DailyPlan("Friday"));
	}
	
	public DailyPlan getDailyPlan(String day){
		for(DailyPlan plan : WeeklyPlan){
			if(plan.getDay().equals(day)){
				return plan;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}
	
	
}
