package com.ax.springmvc.pojo;

import java.io.Serializable;

public class ShareUser implements Serializable{
	private int id;
	private int beUserId;
	private int fileShareId;

	public ShareUser() {
	}

	public ShareUser(int beUserId, int fileShareId) {
		this.beUserId = beUserId;
		this.fileShareId = fileShareId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBeUserId() {
		return beUserId;
	}

	public void setBeUserId(int beUserId) {
		this.beUserId = beUserId;
	}

	public int getFileShareId() {
		return fileShareId;
	}

	public void setFileShareId(int fileShareId) {
		this.fileShareId = fileShareId;
	}

	@Override
	public String toString() {
		return "ShareUser [id=" + id + ", beUserId=" + beUserId + ", fileShareId=" + fileShareId + "]";
	}

}
