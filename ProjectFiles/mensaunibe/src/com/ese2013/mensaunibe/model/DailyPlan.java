package com.ese2013.mensaunibe.model;

import java.util.ArrayList;
import java.util.Date;

public class DailyPlan {
	
	private String day;
	private Date date;
	ArrayList<Menu> Menus;
	
	DailyPlan(String tDay){
		setDay(tDay);
	}
	
	public void add(Menu menu){
		Menus.add(menu);
	}
	
	public Menu get(int i){
		return Menus.get(i);
	}
	
	public Menu get(String title){
		for(Menu menu : Menus){
			if(menu.getTitle().equals(title)){
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

}
