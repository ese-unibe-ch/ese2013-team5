package com.ese2013.mensaunibe.model;

import com.ese2013.mensaunibe.util.MenuBuilder;

/**
 * Class modeling a menu. Needs to be instantiated via the provided builder.
 * 
 * @author ese2013-team5
 * 
 */
public class Menu {

	private String title;
	private String day;
	private String date;
	private String menuDetails;
	private int mensaId;

	public Menu(MenuBuilder builder) {
		this.title = builder.getTitle();
		this.day = builder.getDay();
		this.date = builder.getDate();
		this.menuDetails = builder.getMenuDetails();
		this.mensaId = builder.getMensaId();
	}

	public String getTitle() {
		return title;
	}

	public String getDay() {
		return day;
	}

	public String getDate() {
		return date;
	}

	public int getMensaId() {
		return mensaId;
	}

	public String getMenuDetails() {
		return menuDetails;
	}

}
