package com.mensaunibe.app.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class UserFriend implements Serializable {

	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = UserFriend.class.getSimpleName();
	private static final long serialVersionUID = -7580235806457548893L;

	@SerializedName("id")
	private final int id;
	@SerializedName("mensaid")
	private final int mensaid;
	@SerializedName("name")
	private final String name;

	public UserFriend(int id, int mensaid, String name) {
		this.id = id;
		this.mensaid = mensaid;
		this.name = name;
	}

	public int getFriendID() {
		return id;
	}

	public int getMensaID() {
		return mensaid;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof UserFriend) {
			UserFriend other = (UserFriend) o;
			return this.getFriendID() == other.getFriendID()
					&& this.getName() == other.getName();
		} else {
			return false;
		}
	}

}
