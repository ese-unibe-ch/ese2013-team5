package com.ese2013.mensaunibe.test;

import junit.framework.TestCase;

import com.mensaunibe.app.model.UserFriend;

public class UserFriendTest extends TestCase {

	private UserFriend friend1, friendEqualTo1;

	public UserFriendTest() {
		super();
	}

	@Override
	protected void setUp() throws Exception {
		friend1 = new UserFriend(0, 0, "Name");
		friendEqualTo1 = new UserFriend(0, 0, "Name");
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPreconditions() {
		assertNotNull(friend1);
		assertNotNull(friendEqualTo1);
	}

	public void testFriendEqualsItself() {
		assertEquals(friend1, friend1);
	}

	public void testEqualFriendsShouldBeEqual() {
		assertEquals(friend1, friendEqualTo1);
	}

	public void testFriendsDifferingInIdShouldBeUnequal() {
		UserFriend other = new UserFriend(1, 0, "Name");
		assertFalse(friend1.equals(other));
	}

	public void testFriendsDifferingInNameShouldBeUnequal() {
		UserFriend other = new UserFriend(0, 0, "Other Name");
		assertFalse(friend1.equals(other));
	}

	public void testFriendsDifferingInMensaIdShouldBeEqual() {
		UserFriend other = new UserFriend(0, 1, "Name");
		assertEquals(friend1, other);
	}

}
