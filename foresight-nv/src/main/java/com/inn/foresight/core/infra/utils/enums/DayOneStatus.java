package com.inn.foresight.core.infra.utils.enums;

import com.inn.foresight.core.generic.utils.DisplayName;

public enum DayOneStatus implements DisplayName {
	PENDING("PENDING"), PARTIAL("PARTIAL"), COMPLETE("COMPLETE"), GENERATED("Generated"), UPLOADED("Uploaded"), GENERATION_REQUIRED("Generation Required");
	private String displayName;

	private DayOneStatus(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Display name.
	 *
	 * @return the string
	 */
	@Override
	public String displayName() {
		return displayName;
	}
}
