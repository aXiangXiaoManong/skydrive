package com.ax.springmvc.util.orderby;

import java.util.Comparator;

import com.ax.springmvc.pojo.Folio;

public class FolioTimeDesc implements Comparator<Folio> {

	public int compare(Folio o1, Folio o2) {
		return o2.getFileUpdateTime().compareTo(o1.getFileUpdateTime());
	}

}
