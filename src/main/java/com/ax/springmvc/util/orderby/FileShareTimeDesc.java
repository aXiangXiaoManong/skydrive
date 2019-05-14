package com.ax.springmvc.util.orderby;

import java.util.Comparator;

import com.ax.springmvc.pojo.FileShare;

public class FileShareTimeDesc implements Comparator<FileShare> {

	@Override
	public int compare(FileShare arg0, FileShare arg1) {
		return arg1.getSharingTime().compareTo(arg0.getSharingTime());
	}

}
