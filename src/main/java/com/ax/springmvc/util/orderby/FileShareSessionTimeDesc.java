package com.ax.springmvc.util.orderby;

import java.util.Comparator;

import com.ax.springmvc.vo.FileShareSession;

public class FileShareSessionTimeDesc implements Comparator<FileShareSession> {

	public int compare(FileShareSession arg0, FileShareSession arg1) {
		return arg1.getSharingTime().compareTo(arg0.getSharingTime());
	}

}
