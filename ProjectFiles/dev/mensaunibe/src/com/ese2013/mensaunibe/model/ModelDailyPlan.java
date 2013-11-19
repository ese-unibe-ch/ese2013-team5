package com.ese2013.mensaunibe.model;

import java.util.ArrayList;

/**
 * Class modeling the menu plan of one day
 * 
 * @author ese2013-team5
 * 
 */
public class ModelDailyPlan {

	private String day;
	private ArrayList<ModelMenu> menus;

	ModelDailyPlan(String tDay) {
		menus = new ArrayList<ModelMenu>();
		setDay(tDay);
	}

	public void add(ModelMenu menu) {
		menus.add(menu);
	}

	public ModelMenu get(int i) {
		return menus.get(i);
	}

	public ModelMenu get(String title) {
		for (ModelMenu menu : menus) {
			if (menu.getTitle().equals(title)) {
				return menu;
			}
		}
		return null;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDateAsString() {
		return menus.get(0).getDate();
	}
	
	public int size(){
		return menus.size();
	}

	public ArrayList<ModelMenu> getMenus() {
		return menus;
	}
	

}
