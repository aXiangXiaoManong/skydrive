package com.ax.springmvc.vo;

import java.util.List;

public class FileShareVo {
	private int id;
	private int userId;
	private int isNotEffective;
	private String sharingTime;
	private String outTime;
	private int downloadNum;

	private List<FolioInfoVo> folioInfoVos;

	public FileShareVo() {
	}

	public FileShareVo(int id, int userId, int isNotEffective, String sharingTime, String outTime, int downloadNum,
			List<FolioInfoVo> folioInfoVos) {
		this.id = id;
		this.userId = userId;
		this.isNotEffective = isNotEffective;
		this.sharingTime = sharingTime;
		this.outTime = outTime;
		this.downloadNum = downloadNum;
		this.folioInfoVos = folioInfoVos;
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
		this.sharingTime = sharingTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public int getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(int downloadNum) {
		this.downloadNum = downloadNum;
	}

	public List<FolioInfoVo> getFolioInfoVos() {
		return folioInfoVos;
	}

	public void setFolioInfoVos(List<FolioInfoVo> folioInfoVos) {
		this.folioInfoVos = folioInfoVos;
	}

}
