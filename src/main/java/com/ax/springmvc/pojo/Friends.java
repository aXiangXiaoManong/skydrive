package com.ax.springmvc.pojo;

import java.io.Serializable;

public class Friends implements Serializable {
	private int id;
	private int userId;
	private String addTime;
	private int friendId;

	public Friends() {
	}

	public Friends(int userId, int friendId) {
		this.userId = userId;
		this.friendId = friendId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public int getFriendId() {
		return friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}

}
