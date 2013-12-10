package com.mensaunibe.app.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * !!The User model is not completed and thus it is not used in this version of
 * the application!! 
 *
 */
public class User implements Serializable {

	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = User.class.getSimpleName();
	private static final long serialVersionUID = 6187547612092352521L;
	
	@SerializedName("id")
	private final int id;
	@SerializedName("mensaid")
	private final int mensaid;
	@SerializedName("deviceid")
	private final String deviceid;
	@SerializedName("name")
	private final String name;
	@SerializedName("lastupdate")
	private final int lastupdate;
	@SerializedName("friendlist")
	private final UserFriendList friendlistObj;
	@SerializedName("notificationlist")
	private final UserNotificationList notificationlistObj;
 
	public User(int id, int mensaid, String deviceid, String name, int lastupdate, UserFriendList friendlistObj, UserNotificationList notificationlistObj) {
		this.id = id;
		this.mensaid = mensaid;
		this.deviceid = deviceid;
		this.name = name;
		this.lastupdate = lastupdate;
		this.friendlistObj = friendlistObj;
		this.notificationlistObj = notificationlistObj;
	}
	
	public int getId() {
		return id;
	}
	
	public int getMensaId() {
		return mensaid;
	}
	
	public String getDeviceId() {
		return deviceid;
	}
	
	public String getName() {
		return name;
	}
	
	public int getLastUpdate() {
		return lastupdate;
	}
	
	public UserFriendList getFriendList() {
		return friendlistObj;
	}
	
	public UserNotificationList getNotificationList() {
		return notificationlistObj;
	}
}