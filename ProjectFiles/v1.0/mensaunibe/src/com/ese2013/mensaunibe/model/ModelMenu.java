package com.ese2013.mensaunibe.model;

import com.ese2013.mensaunibe.util.BuilderMenu;

/**
 * Class modeling a menu. Needs to be instantiated via the provided builder.
 * 
 * @author ese2013-team5
 * 
 */
public class ModelMenu {

	private int id;
	private int mensaid;
	private ModelMensa mensa;
	private String title;
	private String desc;
	private String price;
	private String date;
	private String day;
	private int week;
	private Double rating;

	public ModelMenu(BuilderMenu builder) {
		this.id = builder.getMenuID();
		this.mensaid = builder.getMensaID();
		this.mensa = builder.getMensa();
		this.title = builder.getTitle();
		this.desc = builder.getDesc();
		this.price = builder.getPrice();
		this.date = builder.getDate();
		this.day = builder.getDay();
		this.week = builder.getWeek();
		this.rating = builder.getRating();
	}
	
	public int getMenuID() {
		return id;
	}
	
	public int getMensaID() {
		return mensaid;
	}
	
    public ModelMensa getMensa() {
        return mensa;
    }

	public String getTitle() {
		return title;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getPrice() {
		return price;
	}
	
	public String getDate() {
		return date;
	}

	public String getDay() {
		return day;
	}
	
	public int getWeek() {
		return week;
	}
	
	public double getRating() {
		return rating;
	}

//	public String getMenuDetails() {
//		return menuDetails;
//	}

}
