package com.ax.springmvc.pojo;

import java.io.Serializable;
import java.text.NumberFormat;

public class FileInfo implements Serializable {
	private int id;
	private int userId;
	private String fileName;
	private String fileSrc;
	private int parentDir;
	private String fileType;
	private String size;
	private int isNotDel;
	private String upLoadTime;
	private String fileUpdateTime;
	private String showName;
	private int isNotShare;

	public FileInfo() {
	}

	public FileInfo(int userId, String fileName, String fileSrc, int parentDir, String fileType, String size,
			int isNotDel, int isNotShare) {
		this.userId = userId;
		this.fileName = fileName;
		this.fileSrc = fileSrc;
		this.parentDir = parentDir;
		this.fileType = fileType;
		this.size = size;
		this.isNotDel = isNotDel;
		this.showName = fileName;
		this.isNotShare = isNotShare;
	}

	@Override
	public String toString() {
		return "FileInfo [id=" + id + ", userId=" + userId + ", fileName=" + fileName + ", fileSrc=" + fileSrc
				+ ", parentDir=" + parentDir + ", fileType=" + fileType + ", size=" + size + ", isNotDel=" + isNotDel
				+ ", upLoadTime=" + upLoadTime + ", fileUpdateTime=" + fileUpdateTime + ", showName=" + showName
				+ ", isNotShare=" + isNotShare + "]";
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSrc() {
		return fileSrc;
	}

	public void setFileSrc(String fileSrc) {
		this.fileSrc = fileSrc;
	}

	public int getParentDir() {
		return parentDir;
	}

	public void setParentDir(int parentDir) {
		this.parentDir = parentDir;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		if (size.indexOf("B") < 0) {
			double fileSize = Double.parseDouble(size);
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(2);
			if ((fileSize / 1024) / 1024 > 1) {
				size = nf.format((fileSize / 1024) / 1024) + "MB";
			} else if ((fileSize / 1024) > 1) {
				size = nf.format(fileSize / 1024) + "KB";
			} else {
				size = size + "B";
			}
		}
		this.size = size;
	}

	public int getIsNotDel() {
		return isNotDel;
	}

	public void setIsNotDel(int isNotDel) {
		this.isNotDel = isNotDel;
	}

	public String getUpLoadTime() {
		return upLoadTime;
	}

	public void setUpLoadTime(String upLoadTime) {
		if (upLoadTime != null) {
			upLoadTime = upLoadTime.substring(0, 19);
		}
		this.upLoadTime = upLoadTime;
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
