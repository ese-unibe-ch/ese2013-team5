package com.mensaunibe.app.model;

import java.io.Serializable;
import java.util.Locale;

import com.google.gson.annotations.SerializedName;

public class Menu implements Serializable {

	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = Menu.class.getSimpleName();
	private static final long serialVersionUID = 3314650930637294888L;
	
	@SerializedName("id")
	private final int id;
	@SerializedName("mensaid")
	private final int mensaid;
	@SerializedName("title")
	private final String title;
	@SerializedName("title_en")
	private final String title_en;
	@SerializedName("type")
	private final String type;
	@SerializedName("desc")
	private final String desc;
	@SerializedName("desc_en")
	private final String desc_en;
	@SerializedName("price")
	private final String price;
	@SerializedName("price_en")
	private final String price_en;
	@SerializedName("date")
	private final String date;
	@SerializedName("date_en")
	private final String date_en;
	@SerializedName("day")
	private final String day;
	@SerializedName("week")
	private final int week;
	@SerializedName("rating")
	private final Double rating;
	@SerializedName("votes")
	private final int votes;
	
	private String locale;

	public Menu(int id, int mensaid, String title, String title_en, String type, String desc, String desc_en, String price, String price_en, String date, String date_en, String day, int week, Double rating, int votes) {
		this.id = id;
		this.mensaid = mensaid;
		this.title = title;
		this.title_en = title_en;
		this.type = type;
		this.desc = desc;
		this.desc_en = desc_en;
		this.price = price;
		this.price_en = price_en;
		this.date = date;
		this.date_en = date_en;
		this.day = day;
		this.week = week;
		this.rating = rating;
		this.votes = votes;
	}

	public int getMenuID() {
		return id;
	}
	
	public int getMensaID() {
		return mensaid;
	}

	public String getTitle() {
		if (getLocale().equals("de")) {
			return title;
		} else {
			return title_en;
		}
	}
	
	public String getTitle(String lang) {
		if (lang.equals("de")) {
			return title;
		} else {
			return title_en;
		}
	}
	
	public String getType() {
		return type;
	}
	
	public String getDesc() {
		if (getLocale().equals("de")) {
			return desc;
		} else {
			return desc_en;
		}
	}
	
	public String getDesc(String lang) {
		if (lang.equals("de")) {
			return desc;
		} else{
			return desc_en;
		}
	}
	

	
	public String getPrice() {
		if (getLocale().equals("de")) {
			return price;
		} else {
			return price_en;
		}
	}
	
	public String getPrice(String lang) {
		if (lang.equals("de")) {
			return price;
		} else {
			return price_en;
		}
	}
	
	public String getDate() {
		if (getLocale().equals("de")) {
			return date;
		} else {
			return date_en;
		}
	}

	public String getDate(String lang) {
		if (lang.equals("de")) {
			return date;
		} else {
			return date_en;
		}
	}
	
	public String getDay() {
		return day;
	}
	
	public int getWeek() {
		return week;
	}
	
	public Double getRating() {
		return rating;
	}
	
	public Integer getVotes() {
		return votes;
	}
	
	private String getLocale() {
		if (locale == null) {
			return Locale.getDefault().getLanguage();
		} else {
			return locale;
		}
	}
	
	public String toString() {
		return "\n\n" + title + " (" + date + ")\n" + desc + "\n" + price + "\n";
	}
}
