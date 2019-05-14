package com.ax.springmvc.util.orderby;

import java.util.Comparator;

import com.ax.springmvc.pojo.FileInfo;

public class FileTimeAsc implements Comparator<FileInfo> {

	public int compare(FileInfo o1, FileInfo o2) {
		return o1.getFileUpdateTime().compareTo(o2.getFileUpdateTime());
	}
}
