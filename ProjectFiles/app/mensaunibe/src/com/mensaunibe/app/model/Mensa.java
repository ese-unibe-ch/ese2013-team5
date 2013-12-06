package com.mensaunibe.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.mensaunibe.app.controller.Controller;
 
public class Mensa implements Serializable {
 
	// for logging and debugging purposes
	private static final String TAG = Mensa.class.getSimpleName();
	private static final long serialVersionUID = 3220517130960952034L;
	
	@SerializedName("id")
	private final int id;
	@SerializedName("name")
	private final String name;
	@SerializedName("name_en")
	private final String name_en;
	@SerializedName("desc")
	private final String desc;
	@SerializedName("desc_en")
	private final String desc_en;
	@SerializedName("address")
	private final String address;
	@SerializedName("city")
	private final String city;
	@SerializedName("lat")
	private final float lat;
	@SerializedName("lon")
	private final float lon;
	@SerializedName("menulist")
	private MenuList menulistObj;
	
	private boolean isFavorite;
	
	private String locale;
 
	public Mensa(int id, String name, String name_en, String desc, String desc_en, String address, String city, float lat, float lon, MenuList menulistObj) {
		this.id = id;
		this.name = name;
		this.name_en = name_en;
		this.desc = desc;
		this.desc_en = desc_en;
		this.address = address;
		this.city = city;
		this.lat = lat;
		this.lon = lon;
		this.menulistObj = menulistObj;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		if (getLocale().equals("de")) {
			return name;
		} else {
			return name_en;
		}
	}
	
	public String getName(String lang) {
		if (lang.equals("de")) {
			return name;
		} else {
			return name_en;
		}
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
		} else {
			return desc_en;
		}
	}
	
	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}
	
	public LatLng getLocation() {
		return new LatLng(lat, lon);
	}
	
	public MenuList getMenuList() {
		return menulistObj;
	}
	
	public void setMenuList(MenuList list) {
		this.menulistObj = list;
		
	}
	
	private String getLocale() {
		if (locale == null) {
			locale = Controller.getLanguage();
			return locale;
		} else {
			return locale;
		}
	}
	
	public List<Menu> getAllMenus(){
		List<Menu> allMenus = new ArrayList<Menu>();
		allMenus.addAll(getDailyMenus("Monday"));
		allMenus.addAll(getDailyMenus("Tuesday"));
		allMenus.addAll(getDailyMenus("Wednesday"));
		allMenus.addAll(getDailyMenus("Thursday"));
		allMenus.addAll(getDailyMenus("Friday"));
		return allMenus;
	}
	
	public List<Menu> getDailyMenus(String day) {
//		Log.i(TAG, "getDailyMenus(String), day = " + day);
		List<Menu> dailymenus = new ArrayList<Menu>();
//		Log.i(TAG, "getDailyMenus(String), amount of menus = " + menus.size());
		if (menulistObj != null) {
			for (Menu menu : menulistObj.getAllMenus()) {
//				Log.i(TAG, "menu mensaid" + menu.getMensaID());
				if (menu.getDay().equals(day)) {
					dailymenus.add(menu);
				} else {
//					Log.i(TAG, "menu day (" + menu.getDay() + ") != " + day);
				}
			}
			return dailymenus;
		} else {
			Log.e(TAG, "getDailyMenus(String): menulistObj was null");
			return new ArrayList<Menu>();
		}
	}

	public boolean isFavorite() {
		return isFavorite;
	}
	
	public void setFavorite(boolean b){
		isFavorite = b;
	}
}