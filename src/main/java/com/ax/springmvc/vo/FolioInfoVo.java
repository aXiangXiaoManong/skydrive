package com.ax.springmvc.vo;

public class FolioInfoVo {
	private int id;
	private String fileName;
	private String showName;
	private int parentDir;
	private String createTime;
	private String fileUpdateTime;
	private int userId;
	private String size;
	private int children;// 文件夹的子文件数量
	private String fileType;
	private String clazz;
	private int type; // 1:文件夹，2：文件

	public FolioInfoVo() {
	}

	public FolioInfoVo(int id, String fileName, String showName, int parentDir, String createTime,
			String fileUpdateTime, int userId, String size, int children, String fileType, String clazz, int type) {
		this.id = id;
		this.fileName = fileName;
		this.showName = showName;
		this.parentDir = parentDir;
		this.createTime = createTime;
		this.fileUpdateTime = fileUpdateTime;
		this.userId = userId;
		this.size = size;
		this.children = children;
		this.fileType = fileType;
		this.clazz = clazz;
		this.type = type;
	}

	@Override
	public String toString() {
		return "FolioInfoVo [id=" + id + ", fileName=" + fileName + ", showName=" + showName + ", parentDir="
				+ parentDir + ", createTime=" + createTime + ", fileUpdateTime=" + fileUpdateTime + ", userId=" + userId
				+ ", size=" + size + ", children=" + children + ", fileType=" + fileType + ", clazz=" + clazz
				+ ", type=" + type + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public int getParentDir() {
		return parentDir;
	}

	public void setParentDir(int parentDir) {
		this.parentDir = parentDir;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFileUpdateTime() {
		return fileUpdateTime;
	}

	public void setFileUpdateTime(String fileUpdateTime) {
		this.fileUpdateTime = fileUpdateTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getChildren() {
		return children;
	}

	public void setChildren(int children) {
		this.children = children;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
