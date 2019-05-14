package com.ax.springmvc.pojo;

import java.io.Serializable;

public class Folio implements Serializable {
	private int id;
	private int parentDir;
	private String folioName;
	private String createTime;
	private String fileUpdateTime;
	private int isNotDel;
	private int userId;
	private String showName;
	private int isNotShare;

	private int children;

	public Folio() {
	}

	public Folio(int parentDir, String folioName, int userId, int isNotShare) {
		this.parentDir = parentDir;
		this.folioName = folioName;
		this.userId = userId;
		this.showName = folioName;
		this.isNotShare = isNotShare;
	}

	@Override
	public String toString() {
		return "Folio [id=" + id + ", parentDir=" + parentDir + ", folioName=" + folioName + ", createTime="
				+ createTime + ", fileUpdateTime=" + fileUpdateTime + ", isNotDel=" + isNotDel + ", userId=" + userId
				+ ", showName=" + showName + ", isNotShare=" + isNotShare + ", children=" + children + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentDir() {
		return parentDir;
	}

	public void setParentDir(int parentDir) {
		this.parentDir = parentDir;
	}

	public String getFolioName() {
		return folioName;
	}

	public void setFolioName(String folioName) {
		this.folioName = folioName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		if (createTime != null) {
			createTime = createTime.substring(0, 19);
		}
		this.createTime = createTime;
	}

	public String getFileUpdateTime() {
		return fileUpdateTime;
	}

	public void setFileUpdateTime(String fileUpdateTime) {
		if (fileUpdateTime != null) {
			fileUpdateTime = fileUpdateTime.substring(0, 19);
		}
		this.fileUpdateTime = fileUpdateTime;
	}

	public int getIsNotDel() {
		return isNotDel;
	}

	public void setIsNotDel(int isNotDel) {
		this.isNotDel = isNotDel;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getChildren() {
		return children;
	}

	public void setChildren(int children) {
		this.children = children;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public int getIsNotShare() {
		return isNotShare;
	}

	public void setIsNotShare(int isNotShare) {
		this.isNotShare = isNotShare;
	}

}
