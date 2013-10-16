package com.ese2013.mensaunibe.model;

import org.json.JSONObject;

/**
 * Class modeling the menu plan of one week
 * 
 * @author ese2013-team5
 * 
 */
public class WeeklyPlan {

	private DailyPlan[] dailyPlans = new DailyPlan[5];

	public WeeklyPlan(JSONObject json) {
		makeDailyPlans();
		getMenusFromJSON(json);
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

	private void getMenusFromJSON(JSONObject json) {
		// TODO Parse JSON for the JSONObject for one menu here then add it to
		// the corresponding plan
	}

	private void makeDailyPlans() {
		this.dailyPlans[0] = new DailyPlan("Monday");
		this.dailyPlans[1] = new DailyPlan("Tuesday");
		this.dailyPlans[2] = new DailyPlan("Wednesday");
		this.dailyPlans[3] = new DailyPlan("Thursday");
		this.dailyPlans[4] = new DailyPlan("Friday");
	}

}
