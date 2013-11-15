package com.ese2013.mensaunibe.model;

import java.util.ArrayList;
import java.util.List;

import com.ese2013.mensaunibe.util.BuilderMenu;
import com.google.gson.JsonArray;

/**
 * Class modeling the menu plan of one week
 * 
 * @author ese2013-team5
 * 
 */
public class ModelWeeklyPlan {

	private ModelDailyPlan[] dailyPlans = new ModelDailyPlan[5];
	
	public ModelWeeklyPlan() {
		makeDailyPlans();
	}

	public ModelWeeklyPlan(JsonArray menus, ModelMensa mensa) {
		makeDailyPlans();
		getMenusFromJSON(menus, mensa);
	}

	public ModelDailyPlan getMonday() {
		return dailyPlans[0];
	}

	public ModelDailyPlan getTuesday() {
		return dailyPlans[1];
	}

	public ModelDailyPlan getWednesday() {
		return dailyPlans[2];
	}

	public ModelDailyPlan getThursday() {
		return dailyPlans[3];
	}

	public ModelDailyPlan getFriday() {
		return dailyPlans[4];
	}
	
	public ArrayList<ModelMenu> getAllMenus() {
		ArrayList<ModelMenu> result = new ArrayList<ModelMenu>();
		for (ModelDailyPlan p : dailyPlans) {
			result.addAll(p.getMenus());
		}
		return result;
	}

	public ModelDailyPlan getDailyPlan(String day) {
		if(day.equals("Monday")) {return getMonday();}
		if(day.equals("Tuesday")) {return getTuesday();}
		if(day.equals("Wednesday")) {return getWednesday();}
		if(day.equals("Thursday")) {return getThursday();}
		if(day.equals("Friday")) {return getFriday();}
		return null;
	}
	
	public void setMenusForDay(String day, List<ModelMenu> menus) {
		ModelDailyPlan p = getDailyPlan(day);
		for (ModelMenu m : menus) {
			p.add(m);
		}
	}

	private void getMenusFromJSON(JsonArray menus, ModelMensa mensa) {
		for (int i = 0; i < menus.size(); i++) {
			ModelMenu menu = new BuilderMenu(menus.get(i).getAsJsonObject(), mensa).build();
			for (int j = 0; j < dailyPlans.length; j++) {
				if (menu.getDay().equals(dailyPlans[j].getDay())) {
					dailyPlans[j].add(menu);
				}
			}
		}
	}

	private void makeDailyPlans() {
		this.dailyPlans[0] = new ModelDailyPlan("Monday");
		this.dailyPlans[1] = new ModelDailyPlan("Tuesday");
		this.dailyPlans[2] = new ModelDailyPlan("Wednesday");
		this.dailyPlans[3] = new ModelDailyPlan("Thursday");
		this.dailyPlans[4] = new ModelDailyPlan("Friday");
	}

}
