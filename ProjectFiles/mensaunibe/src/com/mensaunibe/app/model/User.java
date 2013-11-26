package com.mensaunibe.app.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
 
public class User implements Serializable {

	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = User.class.getSimpleName();
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
 
	public User(int id, String name, String name_en, String desc, String desc_en, String address, String city, float lat, float lon, MenuList menulistObj) {
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
		return name;
	}
	
	public String getDesc() {
		return desc;
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
	
	public MenuList getMenuList() {
		return menulistObj;
	}	 
}