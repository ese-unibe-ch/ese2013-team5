package com.ese2013.mensaunibe.model;

import java.util.ArrayList;

/**
 * Class modeling the menu plan of one day
 * 
 * @author ese2013-team5
 * 
 */
public class DailyPlan {

	private String day;
	private ArrayList<Menu> menus;

	DailyPlan(String tDay) {
		menus = new ArrayList<Menu>();
		setDay(tDay);
	}

	public void add(Menu menu) {
		menus.add(menu);
	}

	public Menu get(int i) {
		return menus.get(i);
	}

	public Menu get(String title) {
		for (Menu menu : menus) {
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

	public ArrayList<Menu> getMenus() {
		return menus;
	}
	

}
