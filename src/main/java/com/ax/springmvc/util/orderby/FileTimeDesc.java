package com.ax.springmvc.util.orderby;

import java.util.Comparator;

import com.ax.springmvc.pojo.FileInfo;

public class FileTimeDesc implements Comparator<FileInfo> {

	public int compare(FileInfo o1, FileInfo o2) {
		return o2.getFileUpdateTime().compareTo(o1.getFileUpdateTime());
	}
}
