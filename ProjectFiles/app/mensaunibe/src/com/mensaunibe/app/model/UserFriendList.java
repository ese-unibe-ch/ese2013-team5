package com.mensaunibe.app.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
 
/**
 * The User functionality is not completed and thus it is not active
 * in this version of the app 
 */
public class UserFriendList implements Serializable {
	
	// for logging and debugging purposes
	@SuppressWarnings("unused")
	private static final String TAG = UserFriendList.class.getSimpleName();
	private static final long serialVersionUID = -389359696350666865L;

	@SerializedName( "friends" )
	private final List<UserFriend> friends;

	public UserFriendList(final List<UserFriend> friends) {
		this.friends = friends;
	}

	public List<UserFriend> getFriends() {
		return friends;
	}
}
