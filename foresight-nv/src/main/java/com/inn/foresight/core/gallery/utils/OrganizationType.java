package com.inn.foresight.core.gallery.utils;

public enum OrganizationType {
	SMILE("SMILE"), GALLERY("GALLERY");
	private final String value;

	public String getValue() {
		return value;
	}

	private OrganizationType(String value) {
		this.value = value;
	}
}
