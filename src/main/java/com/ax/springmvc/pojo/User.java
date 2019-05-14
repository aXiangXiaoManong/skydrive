package com.ax.springmvc.pojo;

import java.io.Serializable;

public class User implements Serializable {
	private int id;
	private String nickname;
	private String headImg;
	private String phone;
	private String pwd;

	public User() {
		this.headImg = "001.png";
	}

	public User(int id, String nickname, String headImg, String phone, String pwd) {
		this.id = id;
		this.nickname = nickname;
		this.headImg = headImg;
		this.phone = phone;
		this.pwd = pwd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", nickname=" + nickname + ", headImg=" + headImg + ", phone=" + phone + ", pwd="
				+ pwd + "]";
	}

}
