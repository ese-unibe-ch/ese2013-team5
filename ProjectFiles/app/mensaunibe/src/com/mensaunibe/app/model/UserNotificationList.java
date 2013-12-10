package com.mensaunibe.app.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * The User functionality is not completed and thus it is not active
 * in this version of the app 
 */
public class UserNotificationList implements Serializable {

	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = UserNotificationList.class.getSimpleName();
	private static final long serialVersionUID = 4615245585464040353L;

	@SerializedName( "notifications" )
	private final List<UserNotification> notifications;

	public UserNotificationList(final List<UserNotification> notifications) {
		this.notifications = notifications;
	}

	public List<UserNotification> getNotifications() {
		return notifications;
	} 
}
