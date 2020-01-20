package com.inn.foresight.module.nv.bbm.utils;

import java.util.Comparator;

import com.inn.foresight.module.nv.bbm.utils.wrapper.BBMDetailWrapper;

public class BBMComparator implements Comparator<BBMDetailWrapper> {
	public int compare(BBMDetailWrapper s1, BBMDetailWrapper s2) {
		if (s1.getTime() != null && s2.getTime() != null) {
			return s2.getTime().compareTo(s1.getTime());
		} else {
			return -1;
		}
	}
}
