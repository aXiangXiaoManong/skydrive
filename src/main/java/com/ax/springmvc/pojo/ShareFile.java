package com.ax.springmvc.pojo;

import java.io.Serializable;

public class ShareFile implements Serializable {
	private int id;
	private int folioType;
	private int fileId;
	private int fileShareId;

	public ShareFile() {
	}

	public ShareFile(int folioType, int fileId, int fileShareId) {
		this.folioType = folioType;
		this.fileId = fileId;
		this.fileShareId = fileShareId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFolioType() {
		return folioType;
	}

	public void setFolioType(int folioType) {
		this.folioType = folioType;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getFileShareId() {
		return fileShareId;
	}

	public void setFileShareId(int fileShareId) {
		this.fileShareId = fileShareId;
	}

}
