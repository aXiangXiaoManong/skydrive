package com.ax.springmvc.vo;

import java.io.Serializable;
import java.util.List;

public class FileShareSession implements Serializable {
	private int id;
	private int userId;
	private int isNotEffective;
	private String sharingTime;
	private String outTime;
	private int downloadNum;
	private int beUserId; // beUserId
	private String nickname;
	private String headImg;
	private String phone;
	private String pwd;

	private List<FolioInfoVo> folioInfoVos;

	public FileShareSession() {
	}

	public FileShareSession(FileShareSession f) {
		this.id = f.getId();
		this.userId = f.getUserId();
		this.isNotEffective = f.getIsNotEffective();
		this.sharingTime = f.getSharingTime();
		this.outTime = f.getOutTime();
		this.downloadNum = f.getDownloadNum();
		this.beUserId = f.getBeUserId();
		this.nickname = f.getNickname();
		this.headImg = f.getHeadImg();
		this.phone = f.getPhone();
		this.pwd = f.getPwd();
	}

	public FileShareSession(int id, int userId, int isNotEffective, String sharingTime, String outTime, int downloadNum,
			int beUserId, String nickname, String headImg, String phone, String pwd) {
		this.id = id;
		this.userId = userId;
		this.isNotEffective = isNotEffective;
		this.sharingTime = sharingTime;
		this.outTime = outTime;
		this.downloadNum = downloadNum;
		this.beUserId = beUserId;
		this.nickname = nickname;
		this.headImg = headImg;
		this.phone = phone;
		this.pwd = pwd;
	}

	
	@Override
	public String toString() {
		return "FileShareSession [id=" + id + ", userId=" + userId + ", isNotEffective=" + isNotEffective
				+ ", sharingTime=" + sharingTime + ", outTime=" + outTime + ", downloadNum=" + downloadNum
				+ ", beUserId=" + beUserId + ", nickname=" + nickname + ", headImg=" + headImg + ", phone=" + phone
				+ ", pwd=" + pwd + ", \nfolioInfoVos=" + folioInfoVos + "]\n";
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

	public int getBeUserId() {
		return beUserId;
	}

	public void setBeUserId(int beUserId) {
		this.beUserId = beUserId;
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

	public List<FolioInfoVo> getFolioInfoVos() {
		return folioInfoVos;
	}

	public void setFolioInfoVos(List<FolioInfoVo> folioInfoVos) {
		this.folioInfoVos = folioInfoVos;
	}

}
