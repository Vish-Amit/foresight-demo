package com.inn.foresight.core.scatterchart.util;

public enum Kpi {
	RSRP("RSRP"),SINR("SINR");
	private final String value;

	public String getValue() {
		return value;
	}
	private Kpi(String value) {
		this.value=value;
	}
}
