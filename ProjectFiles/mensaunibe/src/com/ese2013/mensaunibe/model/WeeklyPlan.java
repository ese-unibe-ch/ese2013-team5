package com.ese2013.mensaunibe.model;

import org.json.JSONArray;
import org.json.JSONException;

import com.ese2013.mensaunibe.util.BuilderMenu;

/**
 * Class modeling the menu plan of one week
 * 
 * @author ese2013-team5
 * 
 */
public class WeeklyPlan {

	private DailyPlan[] dailyPlans = new DailyPlan[5];

	public WeeklyPlan(JSONArray array, Mensa mensa) {
		makeDailyPlans();
		try {
			getMenusFromJSON(array, mensa);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public DailyPlan getMonday() {
		return dailyPlans[0];
	}

	public DailyPlan getTuesday() {
		return dailyPlans[1];
	}

	public DailyPlan getWednesday() {
		return dailyPlans[2];
	}

	public DailyPlan getThursday() {
		return dailyPlans[3];
	}

	public DailyPlan getFriday() {
		return dailyPlans[4];
	}

	private void getMenusFromJSON(JSONArray array, Mensa mensa) throws JSONException {
		for (int i = 0; i < array.length(); i++) {
			Menu menu = new BuilderMenu(array.getJSONObject(i), mensa).build();
			for (int j = 0; j < dailyPlans.length; j++) {
				if (menu.getDay().equals(dailyPlans[j].getDay())) {
					dailyPlans[j].add(menu);
				}
			}
		}
	}

	private void makeDailyPlans() {
		this.dailyPlans[0] = new DailyPlan("Monday");
		this.dailyPlans[1] = new DailyPlan("Tuesday");
		this.dailyPlans[2] = new DailyPlan("Wednesday");
		this.dailyPlans[3] = new DailyPlan("Thursday");
		this.dailyPlans[4] = new DailyPlan("Friday");
	}

	public DailyPlan getDailyPlan(String day) {
		if(day.equals("Monday")) {return getMonday();}
		if(day.equals("Tuesday")) {return getTuesday();}
		if(day.equals("Wednesday")) {return getWednesday();}
		if(day.equals("Thursday")) {return getThursday();}
		if(day.equals("Friday")) {return getFriday();}
		return null;
	}

}
