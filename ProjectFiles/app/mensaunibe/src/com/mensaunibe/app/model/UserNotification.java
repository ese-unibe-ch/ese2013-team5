package com.mensaunibe.app.model;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;
 
public class UserNotification implements Serializable {
 
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = UserNotification.class.getSimpleName();
	private static final long serialVersionUID = 5044388694003985280L;
	
	@SerializedName("id")
	private final int id;
	@SerializedName("from")
	private final String from;
	@SerializedName("fromid")
	private final int fromid;
	@SerializedName("date")
	private final String date;
	@SerializedName("message")
	private final String message;
	@SerializedName("read")
	private final int read;
 
	public UserNotification(int id, String from, int fromid, String date, String message, int read) {
		this.id = id;
		this.from = from;
		this.fromid = fromid;
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
	
	public int getFromID(){
		return fromid;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getRead() {
		return read;
	}
	
	public boolean isRead() {
		if (read == 1) {
			return true;
		} else {
			return false;
		}
	}

	public String getMessageShortened() {
		if(message.length() > 40){
			return message.substring(0, 40) + "...";
		} else {
			return message;
		}
	}
}