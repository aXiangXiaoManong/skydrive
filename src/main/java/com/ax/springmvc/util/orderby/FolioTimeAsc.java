package com.ax.springmvc.util.orderby;

import java.util.Comparator;

import com.ax.springmvc.pojo.Folio;

public class FolioTimeAsc implements Comparator<Folio> {

	public int compare(Folio o1, Folio o2) {
		return o1.getFileUpdateTime().compareTo(o2.getFileUpdateTime());
	}
}
