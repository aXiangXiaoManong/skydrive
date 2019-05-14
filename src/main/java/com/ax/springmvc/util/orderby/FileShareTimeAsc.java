package com.ax.springmvc.util.orderby;

import java.util.Comparator;

import com.ax.springmvc.pojo.FileShare;

public class FileShareTimeAsc implements Comparator<FileShare> {

	@Override
	public int compare(FileShare arg0, FileShare arg1) {
		return arg0.getSharingTime().compareTo(arg1.getSharingTime());
	}


}
