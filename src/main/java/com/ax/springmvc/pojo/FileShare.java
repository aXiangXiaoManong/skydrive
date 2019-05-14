package com.ax.springmvc.pojo;

import java.io.Serializable;
import java.util.List;

import com.ax.springmvc.vo.FolioInfoVo;

public class FileShare implements Serializable {
	private int id;
	private int userId;
	private int isNotEffective;
	private String sharingTime;
	private String outTime;
	private int downloadNum;

	private FolioInfoVo folioInfoVo;

	private User user;

	private ShareUser shareUser;

	public FileShare() {
	}

	public FileShare(int userId, int isNotEffective, int downloadNum) {
		this.userId = userId;
		this.isNotEffective = isNotEffective;
		this.downloadNum = downloadNum;
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

	public int getIsNotEffective() {
		return isNotEffective;
	}

	public void setIsNotEffective(int isNotEffective) {
		this.isNotEffective = isNotEffective;
	}

	public String getSharingTime() {
		return sharingTime;
	}

	public void setSharingTime(String sharingTime) {
		if (sharingTime != null) {
			sharingTime = sharingTime.substring(0, 19);
		}
		this.sharingTime = sharingTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		if (outTime != null) {
			outTime = outTime.substring(0, 19);
		}
		this.outTime = outTime;
	}

	public int getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(int downloadNum) {
		this.downloadNum = downloadNum;
	}

	public FolioInfoVo getFolioInfoVo() {
		return folioInfoVo;
	}

	public void setFolioInfoVo(FolioInfoVo folioInfoVo) {
		this.folioInfoVo = folioInfoVo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ShareUser getShareUser() {
		return shareUser;
	}

	public void setShareUser(ShareUser shareUser) {
		this.shareUser = shareUser;
	}

	@Override
	public String toString() {
		return "FileShare [id=" + id + ", userId=" + userId + ", isNotEffective=" + isNotEffective + ", sharingTime="
				+ sharingTime + ", outTime=" + outTime + ", downloadNum=" + downloadNum + ", folioInfoVo=" + folioInfoVo
				+ ", user=" + user + ", shareUser=" + shareUser + "]";
	}

}
