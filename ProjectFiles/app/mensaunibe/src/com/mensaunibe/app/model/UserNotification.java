package com.mensaunibe.app.model;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

/**
 * The User functionality is not completed and thus it is not active
 * in this version of the app 
 */
public class UserNotification implements Serializable {

	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = UserNotification.class.getSimpleName();
	private static final long serialVersionUID = 5044388694003985280L;
	
	@SerializedName("id")
	private final int id;
	@SerializedName("from")
	private final String from;
	@SerializedName("date")
	private final String date;
	@SerializedName("message")
	private final String message;
	@SerializedName("read")
	private final int read;

	public UserNotification(int id, String from, String date, String message, int read) {
		this.id = id;
		this.from = from;
		this.date = date;
		this.message = message;
		this.read = read;
	}
	
	public int getId() {
		return id;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean isRead() {
		if (read == 1) {
			return true;
		} else {
			return false;
		}
	}
}